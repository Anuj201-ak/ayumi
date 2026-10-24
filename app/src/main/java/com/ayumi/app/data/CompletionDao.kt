package com.ayumi.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CompletionDao {
    @Query("SELECT * FROM completions WHERE date = :date")
    fun getByDateFlow(date: String): Flow<List<Completion>>

    @Query("SELECT * FROM completions WHERE date = :date")
    suspend fun getByDate(date: String): List<Completion>

    @Query("SELECT * FROM completions WHERE slotId = :slotId AND date = :date")
    suspend fun get(slotId: Long, date: String): Completion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(completion: Completion)

    @Query("DELETE FROM completions WHERE slotId = :slotId AND date = :date")
    suspend fun delete(slotId: Long, date: String)

    @Query("SELECT * FROM completions WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getRange(startDate: String, endDate: String): List<Completion>
}
