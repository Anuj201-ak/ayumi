package com.ayumi.app.data

import kotlinx.coroutines.flow.Flow

class AyumiRepository(private val database: AyumiDatabase) {

    // RoutineSlot operations
    fun getAllRoutineSlotsFlow(): Flow<List<RoutineSlot>> =
        database.routineSlotDao().getAllFlow()

    suspend fun getAllRoutineSlots(): List<RoutineSlot> =
        database.routineSlotDao().getAll()

    suspend fun getRoutineSlotById(id: Long): RoutineSlot? =
        database.routineSlotDao().getById(id)

    suspend fun insertRoutineSlot(slot: RoutineSlot): Long =
        database.routineSlotDao().insert(slot)

    suspend fun updateRoutineSlot(slot: RoutineSlot) =
        database.routineSlotDao().update(slot)

    suspend fun deleteRoutineSlot(slot: RoutineSlot) =
        database.routineSlotDao().delete(slot)

    // DayOverride operations
    suspend fun getDayOverride(date: String): DayOverride? =
        database.dayOverrideDao().getByDate(date)

    fun getDayOverrideFlow(date: String): Flow<DayOverride?> =
        database.dayOverrideDao().getByDateFlow(date)

    suspend fun insertDayOverride(override: DayOverride) =
        database.dayOverrideDao().insert(override)

    suspend fun deleteDayOverride(date: String) =
        database.dayOverrideDao().deleteByDate(date)

    // Completion operations
    fun getCompletionsByDateFlow(date: String): Flow<List<Completion>> =
        database.completionDao().getByDateFlow(date)

    suspend fun getCompletionsByDate(date: String): List<Completion> =
        database.completionDao().getByDate(date)

    suspend fun getCompletion(slotId: Long, date: String): Completion? =
        database.completionDao().get(slotId, date)

    suspend fun insertCompletion(completion: Completion) =
        database.completionDao().insert(completion)

    suspend fun deleteCompletion(slotId: Long, date: String) =
        database.completionDao().delete(slotId, date)

    suspend fun getCompletionsRange(startDate: String, endDate: String): List<Completion> =
        database.completionDao().getRange(startDate, endDate)

    // DayLog operations
    suspend fun getDayLog(date: String): DayLog? =
        database.dayLogDao().getByDate(date)

    fun getDayLogFlow(date: String): Flow<DayLog?> =
        database.dayLogDao().getByDateFlow(date)

    suspend fun insertDayLog(log: DayLog) =
        database.dayLogDao().insert(log)

    suspend fun deleteDayLog(date: String) =
        database.dayLogDao().deleteByDate(date)

    // Effective plan: returns DayOverride if exists, otherwise default routine
    suspend fun getEffectivePlan(date: String): List<OverrideSlot> {
        val override = getDayOverride(date)
        if (override != null) return override.slots

        return getAllRoutineSlots().map { slot ->
            OverrideSlot(
                id = slot.id,
                title = slot.title,
                startTime = slot.startTime,
                endTime = slot.endTime,
                important = slot.important,
                description = slot.description,
                reminderOn = slot.reminderOn
            )
        }
    }
}
