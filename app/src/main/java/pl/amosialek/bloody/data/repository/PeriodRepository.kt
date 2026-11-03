package pl.amosialek.bloody.data.repository

import android.content.Context
import pl.amosialek.bloody.data.db.PeriodDatabaseHelper
import pl.amosialek.bloody.data.model.DailySymptomLog
import pl.amosialek.bloody.data.model.FlowIntensity
import pl.amosialek.bloody.data.model.PeriodRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.time.LocalDate

interface PeriodRepository {
    val periodsFlow: Flow<List<PeriodRecord>>
    suspend fun getAllPeriods(): List<PeriodRecord>
    suspend fun getPeriodById(id: Long): PeriodRecord?
    suspend fun savePeriod(period: PeriodRecord): Long
    suspend fun deletePeriod(id: Long)
    suspend fun getOngoingPeriod(): PeriodRecord?
    suspend fun togglePeriodToday(isStarting: Boolean): PeriodRecord?
    suspend fun getDailyLog(date: LocalDate): DailySymptomLog
    suspend fun saveDailyLog(log: DailySymptomLog)
    suspend fun resetToSampleData()
}

class SQLitePeriodRepository(
    private val dbHelper: PeriodDatabaseHelper
) : PeriodRepository {

    private val _periodsFlow = MutableStateFlow<List<PeriodRecord>>(emptyList())
    override val periodsFlow: Flow<List<PeriodRecord>> = _periodsFlow.asStateFlow()

    init {
        refreshPeriods()
    }

    private fun refreshPeriods() {
        val list = dbHelper.getAllPeriods()
        _periodsFlow.value = list
    }

    override suspend fun getAllPeriods(): List<PeriodRecord> = withContext(Dispatchers.IO) {
        dbHelper.getAllPeriods().also { _periodsFlow.value = it }
    }

    override suspend fun getPeriodById(id: Long): PeriodRecord? = withContext(Dispatchers.IO) {
        dbHelper.getPeriodById(id)
    }

    override suspend fun savePeriod(period: PeriodRecord): Long = withContext(Dispatchers.IO) {
        val id = if (period.id <= 0) {
            dbHelper.insertPeriod(period)
        } else {
            dbHelper.updatePeriod(period)
            period.id
        }
        refreshPeriods()
        id
    }

    override suspend fun deletePeriod(id: Long) = withContext(Dispatchers.IO) {
        dbHelper.deletePeriod(id)
        refreshPeriods()
    }

    override suspend fun getOngoingPeriod(): PeriodRecord? = withContext(Dispatchers.IO) {
        dbHelper.getAllPeriods().firstOrNull { it.isOngoing }
    }

    override suspend fun togglePeriodToday(isStarting: Boolean): PeriodRecord? = withContext(Dispatchers.IO) {
        val today = LocalDate.now()
        val ongoing = getOngoingPeriod()

        if (isStarting) {
            if (ongoing == null) {
                val newPeriod = PeriodRecord(
                    startDate = today,
                    endDate = null,
                    flowIntensity = FlowIntensity.MEDIUM
                )
                val id = dbHelper.insertPeriod(newPeriod)
                refreshPeriods()
                newPeriod.copy(id = id)
            } else {
                ongoing
            }
        } else {
            if (ongoing != null) {
                val updated = ongoing.copy(endDate = today)
                dbHelper.updatePeriod(updated)
                refreshPeriods()
                updated
            } else {
                null
            }
        }
    }

    override suspend fun getDailyLog(date: LocalDate): DailySymptomLog = withContext(Dispatchers.IO) {
        dbHelper.getDailyLog(date) ?: DailySymptomLog(date = date)
    }

    override suspend fun saveDailyLog(log: DailySymptomLog) = withContext(Dispatchers.IO) {
        dbHelper.saveDailyLog(log)
    }

    override suspend fun resetToSampleData() = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        dbHelper.onUpgrade(db, 1, 1)
        refreshPeriods()
    }
}
