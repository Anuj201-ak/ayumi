package com.ayumi.app.export

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate

data class ExportRow(
    val date: String,
    val title: String,
    val start: String,
    val end: String,
    val status: String,
    val completedAt: String
)

object ExportUtils {

    private fun exportDir(context: Context): File =
        File(context.getExternalFilesDir(null), "exports").apply { mkdirs() }

    fun exportExcel(context: Context, rows: List<ExportRow>, rangeLabel: String): File {
        val wb = XSSFWorkbook()
        val sheet = wb.createSheet("Ayumi $rangeLabel")
        val headerFont = wb.createFont().apply { bold = true }
        val headerStyle = wb.createCellStyle().apply { setFont(headerFont) }

        val header = sheet.createRow(0)
        listOf("Date", "Activity", "Start", "End", "Status", "Completed at")
            .forEachIndexed { i, h ->
                header.createCell(i).apply {
                    setCellValue(h)
                    cellStyle = headerStyle
                }
            }
        rows.forEachIndexed { r, row ->
            val cr = sheet.createRow(r + 1)
            cr.createCell(0).setCellValue(row.date)
            cr.createCell(1).setCellValue(row.title)
            cr.createCell(2).setCellValue(row.start)
            cr.createCell(3).setCellValue(row.end)
            cr.createCell(4).setCellValue(row.status)
            cr.createCell(5).setCellValue(row.completedAt)
        }
        // Fixed widths (autoSizeColumn relies on AWT fonts, unreliable on Android)
        sheet.setColumnWidth(0, 14 * 256)
        sheet.setColumnWidth(1, 32 * 256)
        sheet.setColumnWidth(2, 10 * 256)
        sheet.setColumnWidth(3, 10 * 256)
        sheet.setColumnWidth(4, 12 * 256)
        sheet.setColumnWidth(5, 16 * 256)

        val file = File(exportDir(context), "ayumi-${rangeLabel}-${LocalDate.now()}.xlsx")
        FileOutputStream(file).use { wb.write(it) }
        wb.close()
        return file
    }

    fun exportPdf(
        context: Context,
        rows: List<ExportRow>,
        rangeLabel: String,
        summary: List<String>
    ): File {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = doc.startPage(pageInfo)
        var canvas = page.canvas
        val paint = Paint()

        fun newPage() {
            doc.finishPage(page)
            page = doc.startPage(pageInfo)
            canvas = page.canvas
        }

        var y = 64f
        paint.textSize = 22f
        paint.isFakeBoldText = true
        canvas.drawText("Ayumi 歩み — $rangeLabel report", 40f, y, paint)
        y += 36f
        paint.textSize = 13f
        paint.isFakeBoldText = false
        summary.forEach {
            canvas.drawText(it, 40f, y, paint)
            y += 22f
        }
        y += 12f
        paint.isFakeBoldText = true
        canvas.drawText("Date        Activity                          Time        Status      Done at", 40f, y, paint)
        y += 24f
        paint.isFakeBoldText = false
        paint.textSize = 11.5f
        rows.forEach { row ->
            if (y > 800f) {
                newPage()
                y = 64f
            }
            val line = "${row.date.padEnd(10)}${row.title.take(30).padEnd(32)}" +
                "${row.start}-${row.end}  ${row.status.padEnd(8)}${row.completedAt}"
            canvas.drawText(line, 40f, y, paint)
            y += 19f
        }
        doc.finishPage(page)

        val file = File(exportDir(context), "ayumi-${rangeLabel}-${LocalDate.now()}.pdf")
        FileOutputStream(file).use { doc.writeTo(it) }
        doc.close()
        return file
    }

    fun share(context: Context, file: File, mime: String) {
        val uri = FileProvider.getUriForFile(
            context, "${context.packageName}.fileprovider", file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share ${file.name}"))
    }
}
