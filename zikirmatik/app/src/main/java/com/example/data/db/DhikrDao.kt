package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface DhikrDao {

    @Query("SELECT * FROM dhikrs ORDER BY isFavorite DESC, orderIndex ASC, id ASC")
    fun getAllDhikrs(): Flow<List<DhikrEntity>>

    @Query("SELECT * FROM dhikrs WHERE id = :id LIMIT 1")
    suspend fun getDhikrById(id: Long): DhikrEntity?

    @Query("SELECT * FROM dhikrs WHERE id = :id LIMIT 1")
    fun getDhikrFlowById(id: Long): Flow<DhikrEntity?>

    @Query("SELECT COUNT(*) FROM dhikrs")
    suspend fun getDhikrCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDhikr(dhikr: DhikrEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(dhikrs: List<DhikrEntity>)

    @Update
    suspend fun updateDhikr(dhikr: DhikrEntity)

    @Delete
    suspend fun deleteDhikr(dhikr: DhikrEntity)

    @Query("UPDATE dhikrs SET currentCount = :count, totalRecited = totalRecited + :addedToTotal WHERE id = :id")
    suspend fun updateCount(id: Long, count: Int, addedToTotal: Long)

    @Query("UPDATE dhikrs SET currentCount = 0 WHERE id = :id")
    suspend fun resetCount(id: Long)

    @Query("UPDATE dhikrs SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    // History queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: DhikrHistoryEntity): Long

    @Query("SELECT * FROM dhikr_history ORDER BY timestamp DESC LIMIT 100")
    fun getRecentHistory(): Flow<List<DhikrHistoryEntity>>

    @Query("SELECT SUM(count) FROM dhikr_history WHERE dateKey = :dateKey")
    fun getDailyCount(dateKey: String): Flow<Int?>

    @Query("SELECT SUM(count) FROM dhikr_history WHERE timestamp >= :sinceTimestamp")
    fun getCountSince(sinceTimestamp: Long): Flow<Int?>

    @Query("SELECT SUM(count) FROM dhikr_history")
    fun getTotalHistoryCount(): Flow<Int?>

    @Query("SELECT dateKey, SUM(count) as totalForDay FROM dhikr_history WHERE timestamp >= :sinceTimestamp GROUP BY dateKey ORDER BY dateKey ASC")
    suspend fun getDailyAggregatesSince(sinceTimestamp: Long): List<DayAggregate>

    @Query("DELETE FROM dhikr_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Long)

    @Query("DELETE FROM dhikr_history")
    suspend fun clearAllHistory()
}

data class DayAggregate(
    val dateKey: String,
    val totalForDay: Int
)
