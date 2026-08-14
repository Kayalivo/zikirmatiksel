package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dhikr_history")
data class DhikrHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dhikrId: Long,
    val dhikrTitle: String,
    val count: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val dateKey: String // Format: YYYY-MM-DD
)
