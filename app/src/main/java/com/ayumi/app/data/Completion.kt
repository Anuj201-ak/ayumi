package com.ayumi.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "completions", primaryKeys = ["slotId", "date"])
data class Completion(
    val slotId: Long,
    val date: String,
    val completedAt: Long
)
