package com.ayumi.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routine_slots")
data class RoutineSlot(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val startTime: String,
    val endTime: String,
    val important: Boolean = false,
    val description: String = "",
    val reminderOn: Boolean = true
)
