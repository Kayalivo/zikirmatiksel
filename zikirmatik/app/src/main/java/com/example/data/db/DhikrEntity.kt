package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dhikrs")
data class DhikrEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val arabicText: String = "",
    val transliteration: String = "",
    val meaning: String = "",
    val category: String = "gunluk", // namaz, gunluk, esma, ozel, kisisel
    val targetCount: Int = 33,
    val currentCount: Int = 0,
    val totalRecited: Long = 0,
    val isCustom: Boolean = false,
    val isFavorite: Boolean = false,
    val orderIndex: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
