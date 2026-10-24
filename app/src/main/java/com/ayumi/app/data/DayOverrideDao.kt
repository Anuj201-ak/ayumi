package com.ayumi.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DayOverrideDao {
    @Query("SELECT * FROM day_overrides WHERE date = :date")
    suspend fun getByDate(date: String): DayOverride?

    @Query("SELECT * FROM day_overrides WHERE date = :date")
    fun getByDateFlow(date: String): Flow<DayOverride?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(override: DayOverride)

    @Query("DELETE FROM day_overrides WHERE date = :date")
    suspend fun deleteByDate(date: String)
}
