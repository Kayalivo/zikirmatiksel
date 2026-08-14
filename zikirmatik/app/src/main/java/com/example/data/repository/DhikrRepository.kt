package com.example.data.repository

import com.example.data.db.DayAggregate
import com.example.data.db.DhikrDao
import com.example.data.db.DhikrEntity
import com.example.data.db.DhikrHistoryEntity
import com.example.data.model.PresetDhikrs
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DhikrRepository(private val dhikrDao: DhikrDao) {

    val allDhikrs: Flow<List<DhikrEntity>> = dhikrDao.getAllDhikrs()
    val recentHistory: Flow<List<DhikrHistoryEntity>> = dhikrDao.getRecentHistory()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private fun getTodayDateKey(): String {
        return dateFormat.format(Date())
    }

    suspend fun checkAndSeedPresets() {
        val count = dhikrDao.getDhikrCount()
        if (count == 0) {
            dhikrDao.insertAll(PresetDhikrs.list)
        }
    }

    fun getDhikrFlowById(id: Long): Flow<DhikrEntity?> {
        return dhikrDao.getDhikrFlowById(id)
    }

    suspend fun getDhikrById(id: Long): DhikrEntity? {
        return dhikrDao.getDhikrById(id)
    }

    suspend fun incrementDhikr(dhikrId: Long, currentDhikr: DhikrEntity): Int {
        val newCount = currentDhikr.currentCount + 1
        dhikrDao.updateCount(dhikrId, newCount, 1)

        // Log history entry
        val history = DhikrHistoryEntity(
            dhikrId = dhikrId,
            dhikrTitle = currentDhikr.title,
            count = 1,
            timestamp = System.currentTimeMillis(),
            dateKey = getTodayDateKey()
        )
        dhikrDao.insertHistory(history)
        return newCount
    }

    suspend fun decrementDhikr(dhikrId: Long, currentDhikr: DhikrEntity): Int {
        if (currentDhikr.currentCount <= 0) return 0
        val newCount = currentDhikr.currentCount - 1
        dhikrDao.updateCount(dhikrId, newCount, -1)
        return newCount
    }

    suspend fun resetDhikr(dhikrId: Long) {
        dhikrDao.resetCount(dhikrId)
    }

    suspend fun insertDhikr(dhikr: DhikrEntity): Long {
        return dhikrDao.insertDhikr(dhikr)
    }

    suspend fun updateDhikr(dhikr: DhikrEntity) {
        dhikrDao.updateDhikr(dhikr)
    }

    suspend fun deleteDhikr(dhikr: DhikrEntity) {
        dhikrDao.deleteDhikr(dhikr)
    }

    suspend fun toggleFavorite(dhikrId: Long, currentFav: Boolean) {
        dhikrDao.setFavorite(dhikrId, !currentFav)
    }

    fun getTodayTotalCount(): Flow<Int?> {
        return dhikrDao.getDailyCount(getTodayDateKey())
    }

    fun getAllTimeCount(): Flow<Int?> {
        return dhikrDao.getTotalHistoryCount()
    }

    fun getWeekTotalCount(): Flow<Int?> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        return dhikrDao.getCountSince(calendar.timeInMillis)
    }

    fun getMonthTotalCount(): Flow<Int?> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -30)
        return dhikrDao.getCountSince(calendar.timeInMillis)
    }

    suspend fun getDailyAggregatesForLastDays(days: Int = 7): List<DayStatItem> {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -(days - 1))
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val sinceTimestamp = calendar.timeInMillis
        val dbAggregates = dhikrDao.getDailyAggregatesSince(sinceTimestamp).associateBy { it.dateKey }

        val displayFormat = SimpleDateFormat("EEE", Locale("tr", "TR"))
        val fullFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val result = mutableListOf<DayStatItem>()
        val iterCal = Calendar.getInstance()
        iterCal.timeInMillis = sinceTimestamp

        for (i in 0 until days) {
            val key = fullFormat.format(iterCal.time)
            val dayLabel = displayFormat.format(iterCal.time)
            val total = dbAggregates[key]?.totalForDay ?: 0
            result.add(DayStatItem(dateKey = key, label = dayLabel, count = total))
            iterCal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return result
    }

    suspend fun calculateStreak(): Int {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -60)
        val aggregates = dhikrDao.getDailyAggregatesSince(calendar.timeInMillis).associateBy { it.dateKey }

        var streak = 0
        val checkCal = Calendar.getInstance()
        val todayKey = dateFormat.format(checkCal.time)

        // If today has activity, start streak count from today; else if yesterday had activity, count from yesterday
        if ((aggregates[todayKey]?.totalForDay ?: 0) > 0) {
            streak++
            checkCal.add(Calendar.DAY_OF_YEAR, -1)
        } else {
            checkCal.add(Calendar.DAY_OF_YEAR, -1)
            val yestKey = dateFormat.format(checkCal.time)
            if ((aggregates[yestKey]?.totalForDay ?: 0) == 0) {
                return 0
            }
        }

        while (true) {
            val key = dateFormat.format(checkCal.time)
            val count = aggregates[key]?.totalForDay ?: 0
            if (count > 0) {
                streak++
                checkCal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        return streak
    }

    suspend fun deleteHistoryItem(id: Long) {
        dhikrDao.deleteHistoryById(id)
    }

    suspend fun clearHistory() {
        dhikrDao.clearAllHistory()
    }
}

data class DayStatItem(
    val dateKey: String,
    val label: String,
    val count: Int
)
