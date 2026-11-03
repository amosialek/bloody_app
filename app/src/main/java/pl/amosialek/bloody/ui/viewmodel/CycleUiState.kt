package pl.amosialek.bloody.ui.viewmodel

import pl.amosialek.bloody.data.model.DailySymptomLog
import pl.amosialek.bloody.data.model.PeriodRecord
import pl.amosialek.bloody.domain.CycleCalculator
import pl.amosialek.bloody.domain.CycleInfo
import java.time.LocalDate
import java.time.YearMonth

data class CycleUiState(
    val periods: List<PeriodRecord> = emptyList(),
    val cycleInfo: CycleInfo = CycleCalculator.calculateCycleInfo(emptyList()),
    val cycleLengths: Map<Long, Int> = emptyMap(),
    val todayLog: DailySymptomLog = DailySymptomLog(date = LocalDate.now()),
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val editingPeriod: PeriodRecord? = null,
    val isEditingSheetVisible: Boolean = false,
    val showSymptomDialog: Boolean = false,
    val showNotesDialog: Boolean = false,
    val isPeriodActiveToday: Boolean = false
)
