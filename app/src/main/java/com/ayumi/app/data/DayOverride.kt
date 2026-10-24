package com.ayumi.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "day_overrides")
@TypeConverters(Converters::class)
data class DayOverride(
    @PrimaryKey
    val date: String,
    val slots: List<OverrideSlot>
)

data class OverrideSlot(
    val id: Long = 0,
    val title: String,
    val startTime: String,
    val endTime: String,
    val important: Boolean = false,
    val description: String = "",
    val reminderOn: Boolean = true
)
