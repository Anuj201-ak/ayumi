package com.ayumi.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DayLogDao {
    @Query("SELECT * FROM day_logs WHERE date = :date")
    suspend fun getByDate(date: String): DayLog?

    @Query("SELECT * FROM day_logs WHERE date = :date")
    fun getByDateFlow(date: String): Flow<DayLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: DayLog)

    @Query("DELETE FROM day_logs WHERE date = :date")
    suspend fun deleteByDate(date: String)
}
