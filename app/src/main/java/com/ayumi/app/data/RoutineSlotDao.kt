package com.ayumi.app.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineSlotDao {
    @Query("SELECT * FROM routine_slots ORDER BY startTime ASC")
    fun getAllFlow(): Flow<List<RoutineSlot>>

    @Query("SELECT * FROM routine_slots ORDER BY startTime ASC")
    suspend fun getAll(): List<RoutineSlot>

    @Query("SELECT * FROM routine_slots WHERE id = :id")
    suspend fun getById(id: Long): RoutineSlot?

    @Insert
    suspend fun insert(slot: RoutineSlot): Long

    @Update
    suspend fun update(slot: RoutineSlot)

    @Delete
    suspend fun delete(slot: RoutineSlot)
}
