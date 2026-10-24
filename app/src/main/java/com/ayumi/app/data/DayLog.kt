package com.ayumi.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_logs")
data class DayLog(
    @PrimaryKey
    val date: String,
    val text: String
)
