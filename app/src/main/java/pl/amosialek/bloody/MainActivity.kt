package pl.amosialek.bloody

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.amosialek.bloody.ui.components.AppDestination
import pl.amosialek.bloody.ui.components.BottomNavBar
import pl.amosialek.bloody.ui.screens.CalendarScreen
import pl.amosialek.bloody.ui.screens.EditPeriodSheet
import pl.amosialek.bloody.ui.screens.InsightsScreen
import pl.amosialek.bloody.ui.screens.SettingsScreen
import pl.amosialek.bloody.ui.screens.SymptomLogDialog
import pl.amosialek.bloody.ui.screens.TodayScreen
import pl.amosialek.bloody.ui.theme.YAPDTheme
import pl.amosialek.bloody.ui.viewmodel.PeriodViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YAPDTheme {
                PeriodTrackerApp()
            }
        }
    }
}

@Composable
fun PeriodTrackerApp(
    viewModel: PeriodViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentDestination by rememberSaveable { mutableStateOf(AppDestination.TODAY) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomNavBar(
                currentDestination = currentDestination,
                onDestinationSelected = { destination ->
                    currentDestination = destination
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = currentDestination,
                label = "navigation_crossfade"
            ) { destination ->
                when (destination) {
                    AppDestination.TODAY -> {
                        TodayScreen(
                            uiState = uiState,
                            onTogglePeriodToday = { viewModel.togglePeriodToday(it) },
                            onOpenCalendar = { currentDestination = AppDestination.CALENDAR },
                            onOpenLogSymptoms = { viewModel.setShowSymptomDialog(true) },
                            onOpenDailyNotes = { viewModel.setShowNotesDialog(true) }
                        )
                    }
                    AppDestination.CALENDAR -> {
                        CalendarScreen(
                            uiState = uiState,
                            onNavigateBack = { currentDestination = AppDestination.TODAY },
                            onNextMonth = { viewModel.nextMonth() },
                            onPreviousMonth = { viewModel.previousMonth() },
                            onSelectDate = { viewModel.selectDate(it) },
                            onAddNewPeriod = { viewModel.openAddPeriodSheet(it) },
                            onEditPeriod = { viewModel.openEditPeriodSheet(it) }
                        )
                    }
                    AppDestination.INSIGHTS -> {
                        InsightsScreen(uiState = uiState)
                    }
                    AppDestination.SETTINGS -> {
                        SettingsScreen(
                            uiState = uiState,
                            onResetData = { viewModel.resetToSampleData() }
                        )
                    }
                }
            }

            // Edit / Add Period Bottom Sheet
            if (uiState.isEditingSheetVisible && uiState.editingPeriod != null) {
                EditPeriodSheet(
                    period = uiState.editingPeriod!!,
                    onSave = { viewModel.savePeriod(it) },
                    onDelete = { viewModel.deletePeriod(it) },
                    onDismiss = { viewModel.dismissEditSheet() }
                )
            }

            // Symptom / Notes Logging Dialog
            if (uiState.showSymptomDialog || uiState.showNotesDialog) {
                SymptomLogDialog(
                    currentLog = uiState.todayLog,
                    onToggleSymptom = { viewModel.toggleSymptom(it) },
                    onSaveNotes = { viewModel.updateNotes(it) },
                    onDismiss = {
                        viewModel.setShowSymptomDialog(false)
                        viewModel.setShowNotesDialog(false)
                    }
                )
            }
        }
    }
}