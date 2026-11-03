package pl.amosialek.bloody.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import pl.amosialek.bloody.data.db.PeriodDatabaseHelper
import pl.amosialek.bloody.data.model.DailySymptomLog
import pl.amosialek.bloody.data.model.FlowIntensity
import pl.amosialek.bloody.data.model.PeriodRecord
import pl.amosialek.bloody.data.repository.PeriodRepository
import pl.amosialek.bloody.data.repository.SQLitePeriodRepository
import pl.amosialek.bloody.domain.CycleCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class PeriodViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: PeriodRepository = SQLitePeriodRepository(PeriodDatabaseHelper(application))
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CycleUiState())
    val uiState: StateFlow<CycleUiState> = _uiState.asStateFlow()

    init {
        observePeriods()
        loadTodayLog()
    }

    private fun observePeriods() {
        viewModelScope.launch {
            repository.periodsFlow.collect { list ->
                val today = LocalDate.now()
                val info = CycleCalculator.calculateCycleInfo(list, today)
                val lengths = CycleCalculator.calculateCycleLengthsMap(list)
                val hasOngoing = list.any { it.isOngoing }
                val activeToday = hasOngoing || list.any { it.contains(today) }

                _uiState.update { current ->
                    current.copy(
                        periods = list,
                        cycleInfo = info,
                        cycleLengths = lengths,
                        isPeriodActiveToday = activeToday
                    )
                }
            }
        }
    }

    private fun loadTodayLog() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val log = repository.getDailyLog(today)
            _uiState.update { it.copy(todayLog = log) }
        }
    }

    fun togglePeriodToday(start: Boolean) {
        viewModelScope.launch {
            repository.togglePeriodToday(start)
        }
    }

    fun openAddPeriodSheet(defaultDate: LocalDate = LocalDate.now()) {
        val newPeriod = PeriodRecord(
            id = 0,
            startDate = defaultDate,
            endDate = defaultDate.plusDays(4),
            flowIntensity = FlowIntensity.MEDIUM
        )
        _uiState.update {
            it.copy(
                editingPeriod = newPeriod,
                isEditingSheetVisible = true
            )
        }
    }

    fun openEditPeriodSheet(period: PeriodRecord) {
        _uiState.update {
            it.copy(
                editingPeriod = period,
                isEditingSheetVisible = true
            )
        }
    }

    fun dismissEditSheet() {
        _uiState.update {
            it.copy(
                editingPeriod = null,
                isEditingSheetVisible = false
            )
        }
    }

    fun savePeriod(period: PeriodRecord) {
        viewModelScope.launch {
            repository.savePeriod(period)
            dismissEditSheet()
        }
    }

    fun deletePeriod(id: Long) {
        viewModelScope.launch {
            repository.deletePeriod(id)
            dismissEditSheet()
        }
    }

    fun nextMonth() {
        _uiState.update { it.copy(currentMonth = it.currentMonth.plusMonths(1)) }
    }

    fun previousMonth() {
        _uiState.update { it.copy(currentMonth = it.currentMonth.minusMonths(1)) }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun toggleSymptom(symptom: String) {
        viewModelScope.launch {
            val currentLog = _uiState.value.todayLog
            val newSymptoms = if (currentLog.symptoms.contains(symptom)) {
                currentLog.symptoms - symptom
            } else {
                currentLog.symptoms + symptom
            }
            val updated = currentLog.copy(symptoms = newSymptoms)
            repository.saveDailyLog(updated)
            _uiState.update { it.copy(todayLog = updated) }
        }
    }

    fun updateNotes(notes: String) {
        viewModelScope.launch {
            val currentLog = _uiState.value.todayLog
            val updated = currentLog.copy(notes = notes)
            repository.saveDailyLog(updated)
            _uiState.update { it.copy(todayLog = updated, showNotesDialog = false) }
        }
    }

    fun setShowSymptomDialog(show: Boolean) {
        _uiState.update { it.copy(showSymptomDialog = show) }
    }

    fun setShowNotesDialog(show: Boolean) {
        _uiState.update { it.copy(showNotesDialog = show) }
    }

    fun resetToSampleData() {
        viewModelScope.launch {
            repository.resetToSampleData()
        }
    }
}
