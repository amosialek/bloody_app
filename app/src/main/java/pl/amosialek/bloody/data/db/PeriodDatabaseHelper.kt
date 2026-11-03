package pl.amosialek.bloody.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pl.amosialek.bloody.data.model.DailySymptomLog
import pl.amosialek.bloody.data.model.FlowIntensity
import pl.amosialek.bloody.data.model.PeriodRecord
import java.time.LocalDate

class PeriodDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        const val DATABASE_NAME = "yapd_periods.db"
        const val DATABASE_VERSION = 1

        const val TABLE_PERIODS = "periods"
        const val COL_PERIOD_ID = "id"
        const val COL_START_DATE = "start_date"
        const val COL_END_DATE = "end_date"
        const val COL_FLOW_INTENSITY = "flow_intensity"
        const val COL_NOTES = "notes"
        const val COL_SYMPTOMS = "symptoms"

        const val TABLE_DAILY_LOGS = "daily_logs"
        const val COL_LOG_DATE = "log_date"
        const val COL_MOOD = "mood"
        const val COL_ENERGY = "energy"
        const val COL_FLOW = "flow"
        const val COL_LOG_SYMPTOMS = "symptoms"
        const val COL_LOG_NOTES = "notes"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_PERIODS (
                $COL_PERIOD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_START_DATE TEXT NOT NULL,
                $COL_END_DATE TEXT,
                $COL_FLOW_INTENSITY TEXT NOT NULL,
                $COL_NOTES TEXT DEFAULT '',
                $COL_SYMPTOMS TEXT DEFAULT ''
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_DAILY_LOGS (
                $COL_LOG_DATE TEXT PRIMARY KEY,
                $COL_MOOD TEXT,
                $COL_ENERGY TEXT,
                $COL_FLOW TEXT,
                $COL_LOG_SYMPTOMS TEXT,
                $COL_LOG_NOTES TEXT
            )
            """.trimIndent()
        )

        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PERIODS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DAILY_LOGS")
        onCreate(db)
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        val today = LocalDate.now()
        // Relative to current date so today is Cycle Day 24, matching Stitch screen
        val cycle1Start = today.minusDays(23)
        val cycle1End = cycle1Start.plusDays(4)

        val cycle2Start = cycle1Start.minusDays(29)
        val cycle2End = cycle2Start.plusDays(4)

        val cycle3Start = cycle2Start.minusDays(28)
        val cycle3End = cycle3Start.plusDays(4)

        insertPeriodDirect(db, cycle3Start, cycle3End, FlowIntensity.LIGHT, "Regular cycle")
        insertPeriodDirect(db, cycle2Start, cycle2End, FlowIntensity.MEDIUM, "Medium flow, standard cycle")
        insertPeriodDirect(db, cycle1Start, cycle1End, FlowIntensity.MEDIUM, "Normal flow, slight cramps on day 2")

        // Seed today's log
        val logValues = ContentValues().apply {
            put(COL_LOG_DATE, today.toString())
            put(COL_MOOD, "Normal Mood")
            put(COL_ENERGY, "Mild Energy")
            put(COL_FLOW, "Light Flow")
            put(COL_LOG_SYMPTOMS, "Normal Mood,Mild Energy,Light Flow")
            put(COL_LOG_NOTES, "Restful night, feeling balanced")
        }
        db.insert(TABLE_DAILY_LOGS, null, logValues)
    }

    private fun insertPeriodDirect(
        db: SQLiteDatabase,
        start: LocalDate,
        end: LocalDate?,
        flow: FlowIntensity,
        notes: String
    ) {
        val values = ContentValues().apply {
            put(COL_START_DATE, start.toString())
            put(COL_END_DATE, end?.toString())
            put(COL_FLOW_INTENSITY, flow.name)
            put(COL_NOTES, notes)
            put(COL_SYMPTOMS, "")
        }
        db.insert(TABLE_PERIODS, null, values)
    }

    fun getAllPeriods(): List<PeriodRecord> {
        val list = mutableListOf<PeriodRecord>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_PERIODS,
            null,
            null,
            null,
            null,
            null,
            "$COL_START_DATE DESC"
        )

        cursor.use {
            val idIndex = it.getColumnIndexOrThrow(COL_PERIOD_ID)
            val startIndex = it.getColumnIndexOrThrow(COL_START_DATE)
            val endIndex = it.getColumnIndexOrThrow(COL_END_DATE)
            val flowIndex = it.getColumnIndexOrThrow(COL_FLOW_INTENSITY)
            val notesIndex = it.getColumnIndexOrThrow(COL_NOTES)
            val symptomsIndex = it.getColumnIndexOrThrow(COL_SYMPTOMS)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                val start = LocalDate.parse(it.getString(startIndex))
                val endStr = it.getString(endIndex)
                val end = if (!endStr.isNullOrBlank()) LocalDate.parse(endStr) else null
                val flow = FlowIntensity.fromString(it.getString(flowIndex))
                val notes = it.getString(notesIndex) ?: ""
                val sympStr = it.getString(symptomsIndex) ?: ""
                val symptoms = if (sympStr.isNotBlank()) sympStr.split(",") else emptyList()

                list.add(
                    PeriodRecord(
                        id = id,
                        startDate = start,
                        endDate = end,
                        flowIntensity = flow,
                        notes = notes,
                        symptoms = symptoms
                    )
                )
            }
        }
        return list
    }

    fun getPeriodById(id: Long): PeriodRecord? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PERIODS,
            null,
            "$COL_PERIOD_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                val start = LocalDate.parse(it.getString(it.getColumnIndexOrThrow(COL_START_DATE)))
                val endStr = it.getString(it.getColumnIndexOrThrow(COL_END_DATE))
                val end = if (!endStr.isNullOrBlank()) LocalDate.parse(endStr) else null
                val flow = FlowIntensity.fromString(it.getString(it.getColumnIndexOrThrow(COL_FLOW_INTENSITY)))
                val notes = it.getString(it.getColumnIndexOrThrow(COL_NOTES)) ?: ""
                val sympStr = it.getString(it.getColumnIndexOrThrow(COL_SYMPTOMS)) ?: ""
                val symptoms = if (sympStr.isNotBlank()) sympStr.split(",") else emptyList()

                PeriodRecord(
                    id = id,
                    startDate = start,
                    endDate = end,
                    flowIntensity = flow,
                    notes = notes,
                    symptoms = symptoms
                )
            } else null
        }
    }

    fun insertPeriod(period: PeriodRecord): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_START_DATE, period.startDate.toString())
            put(COL_END_DATE, period.endDate?.toString())
            put(COL_FLOW_INTENSITY, period.flowIntensity.name)
            put(COL_NOTES, period.notes)
            put(COL_SYMPTOMS, period.symptoms.joinToString(","))
        }
        return db.insert(TABLE_PERIODS, null, values)
    }

    fun updatePeriod(period: PeriodRecord): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_START_DATE, period.startDate.toString())
            put(COL_END_DATE, period.endDate?.toString())
            put(COL_FLOW_INTENSITY, period.flowIntensity.name)
            put(COL_NOTES, period.notes)
            put(COL_SYMPTOMS, period.symptoms.joinToString(","))
        }
        return db.update(
            TABLE_PERIODS,
            values,
            "$COL_PERIOD_ID = ?",
            arrayOf(period.id.toString())
        )
    }

    fun deletePeriod(id: Long): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_PERIODS,
            "$COL_PERIOD_ID = ?",
            arrayOf(id.toString())
        )
    }

    fun getDailyLog(date: LocalDate): DailySymptomLog? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_DAILY_LOGS,
            null,
            "$COL_LOG_DATE = ?",
            arrayOf(date.toString()),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                val mood = it.getString(it.getColumnIndexOrThrow(COL_MOOD)) ?: "Normal Mood"
                val energy = it.getString(it.getColumnIndexOrThrow(COL_ENERGY)) ?: "Mild Energy"
                val flow = it.getString(it.getColumnIndexOrThrow(COL_FLOW)) ?: "Light Flow"
                val sympStr = it.getString(it.getColumnIndexOrThrow(COL_LOG_SYMPTOMS)) ?: ""
                val symptoms = if (sympStr.isNotBlank()) sympStr.split(",").toSet() else emptySet()
                val notes = it.getString(it.getColumnIndexOrThrow(COL_LOG_NOTES)) ?: ""

                DailySymptomLog(
                    date = date,
                    mood = mood,
                    energy = energy,
                    flow = flow,
                    symptoms = symptoms,
                    notes = notes
                )
            } else null
        }
    }

    fun saveDailyLog(log: DailySymptomLog) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_LOG_DATE, log.date.toString())
            put(COL_MOOD, log.mood)
            put(COL_ENERGY, log.energy)
            put(COL_FLOW, log.flow)
            put(COL_LOG_SYMPTOMS, log.symptoms.joinToString(","))
            put(COL_LOG_NOTES, log.notes)
        }
        db.insertWithOnConflict(
            TABLE_DAILY_LOGS,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }
}
