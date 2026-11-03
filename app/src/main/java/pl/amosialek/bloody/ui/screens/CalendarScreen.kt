package pl.amosialek.bloody.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.amosialek.bloody.R
import pl.amosialek.bloody.data.model.PeriodRecord
import pl.amosialek.bloody.ui.theme.OnPrimary
import pl.amosialek.bloody.ui.theme.OnSurface
import pl.amosialek.bloody.ui.theme.OnSurfaceVariant
import pl.amosialek.bloody.ui.theme.OutlineMuted
import pl.amosialek.bloody.ui.theme.OutlineVariant
import pl.amosialek.bloody.ui.theme.PrimaryContainer
import pl.amosialek.bloody.ui.theme.PrimaryDarkPlum
import pl.amosialek.bloody.ui.theme.PrimaryFixed
import pl.amosialek.bloody.ui.theme.SecondaryContainer
import pl.amosialek.bloody.ui.theme.SecondaryCoral
import pl.amosialek.bloody.ui.theme.SecondaryFixed
import pl.amosialek.bloody.ui.theme.SurfaceContainer
import pl.amosialek.bloody.ui.theme.SurfaceContainerHigh
import pl.amosialek.bloody.ui.theme.SurfaceContainerLow
import pl.amosialek.bloody.ui.theme.SurfaceContainerLowest
import pl.amosialek.bloody.ui.theme.SurfaceWarm
import pl.amosialek.bloody.ui.viewmodel.CycleUiState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    uiState: CycleUiState,
    onNavigateBack: () -> Unit,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onSelectDate: (LocalDate) -> Unit,
    onAddNewPeriod: (LocalDate) -> Unit,
    onEditPeriod: (PeriodRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentMonth = uiState.currentMonth
    val monthTitle = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceWarm)
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top App Bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back to Today",
                            tint = PrimaryDarkPlum,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "Cycle Calendar",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryDarkPlum
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onAddNewPeriod(LocalDate.now()) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_history_edu),
                        contentDescription = null,
                        tint = PrimaryDarkPlum,
                        modifier = Modifier.size(17.dp)
                    )
                    Text(
                        text = "Edit History",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryDarkPlum
                    )
                }
            }
        }

        // Month Navigation Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "CURRENT CYCLE PHASE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = OutlineMuted
                        )
                        Text(
                            text = monthTitle,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryDarkPlum
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SurfaceContainerLow)
                            .border(1.dp, OutlineVariant.copy(alpha = 0.4f), CircleShape)
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onPreviousMonth,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_left),
                                contentDescription = "Previous Month",
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = onNextMonth,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_chevron_right),
                                contentDescription = "Next Month",
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Monthly Calendar Grid View
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Weekday headers: M, T, W, T, F, S, S
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf("M", "T", "W", "T", "F", "S", "S").forEach { day ->
                            Text(
                                text = day,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = OutlineMuted,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(36.dp)
                            )
                        }
                    }

                    // Calendar Day Matrix
                    CalendarMonthGrid(
                        yearMonth = currentMonth,
                        uiState = uiState,
                        onDayClick = { date ->
                            onSelectDate(date)
                            val periodOnDate = uiState.periods.firstOrNull { it.contains(date) }
                            if (periodOnDate != null) {
                                onEditPeriod(periodOnDate)
                            } else {
                                onAddNewPeriod(date)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Legend Chips Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(width = 0.5.dp, color = OutlineVariant.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp))
                            .padding(top = 10.dp, bottom = 4.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Period
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryContainer)
                            )
                            Text(
                                text = "Period",
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }

                        // Ovulation
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(SecondaryContainer)
                            )
                            Text(
                                text = "Ovulation",
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }

                        // Forecast
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, PrimaryDarkPlum, CircleShape)
                                    .background(PrimaryFixed.copy(alpha = 0.4f))
                            )
                            Text(
                                text = "Forecast",
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Past Periods History Section
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Past Cycles & Periods",
                        fontSize = 17.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryDarkPlum
                    )
                    Text(
                        text = "${uiState.periods.size} cycles recorded",
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                        .border(1.dp, OutlineVariant.copy(alpha = 0.5f), CircleShape)
                        .clickable { onAddNewPeriod(LocalDate.now()) }
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null,
                        tint = PrimaryDarkPlum,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "Log Period",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryDarkPlum
                    )
                }
            }
        }

        // List of past periods
        items(uiState.periods, key = { it.id }) { period ->
            val cycleLength = uiState.cycleLengths[period.id]
            val durationText = if (period.isOngoing) {
                "${period.durationDays} days (Ongoing)"
            } else {
                "${period.durationDays} days"
            }

            val rangeText = if (period.isOngoing) {
                "${period.startDate.format(dateFormatter)} – Ongoing"
            } else {
                "${period.startDate.format(dateFormatter)} – ${period.endDate?.format(dateFormatter)}"
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEditPeriod(period) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryContainer)
                            )
                            Text(
                                text = "$rangeText ",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Text(
                                text = "($durationText)",
                                fontSize = 12.sp,
                                color = OnSurfaceVariant
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_water_drop),
                                    contentDescription = null,
                                    tint = PrimaryDarkPlum,
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = "${period.flowIntensity.displayName} Flow",
                                    fontSize = 12.sp,
                                    color = OnSurfaceVariant
                                )
                            }

                            if (cycleLength != null) {
                                Text(
                                    text = "•",
                                    fontSize = 12.sp,
                                    color = OutlineVariant
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SurfaceContainer)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Cycle: $cycleLength days",
                                        fontSize = 11.sp,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    IconButton(
                        onClick = { onEditPeriod(period) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = "Edit Period",
                            tint = PrimaryDarkPlum,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarMonthGrid(
    yearMonth: YearMonth,
    uiState: CycleUiState,
    onDayClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val daysInMonth = yearMonth.lengthOfMonth()

    // Adjust so Monday is column 0, Sunday is column 6
    val firstDayOfWeek = (firstDayOfMonth.dayOfWeek.value - 1) // 0 for Monday, 6 for Sunday

    val prevMonth = yearMonth.minusMonths(1)
    val daysInPrevMonth = prevMonth.lengthOfMonth()

    val totalCells = ((firstDayOfWeek + daysInMonth + 6) / 7) * 7

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        var dayCounter = 1
        var nextMonthDayCounter = 1

        val rows = totalCells / 7
        for (row in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (col in 0 until 7) {
                    val cellIndex = row * 7 + col
                    if (cellIndex < firstDayOfWeek) {
                        // Trailing day from previous month
                        val prevDay = daysInPrevMonth - (firstDayOfWeek - cellIndex - 1)
                        Box(
                            modifier = Modifier
                                .size(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = prevDay.toString(),
                                fontSize = 12.sp,
                                color = OutlineVariant
                            )
                        }
                    } else if (dayCounter <= daysInMonth) {
                        val date = yearMonth.atDay(dayCounter)
                        dayCounter++

                        val isPeriod = uiState.periods.any { it.contains(date) }
                        val isOvulation = date.isEqual(uiState.cycleInfo.ovulationDate)
                        val isFertile = !date.isBefore(uiState.cycleInfo.fertileWindowStart) &&
                                !date.isAfter(uiState.cycleInfo.fertileWindowEnd)
                        val isForecast = !date.isBefore(uiState.cycleInfo.nextPeriodStartDate) &&
                                !date.isAfter(uiState.cycleInfo.nextPeriodEndDate)

                        CalendarDayCell(
                            date = date,
                            isPeriod = isPeriod,
                            isOvulation = isOvulation,
                            isFertile = isFertile,
                            isForecast = isForecast,
                            onClick = { onDayClick(date) }
                        )
                    } else {
                        // Leading day into next month
                        val nextDay = nextMonthDayCounter++
                        Box(
                            modifier = Modifier
                                .size(36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = nextDay.toString(),
                                fontSize = 12.sp,
                                color = OutlineVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalendarDayCell(
    date: LocalDate,
    isPeriod: Boolean,
    isOvulation: Boolean,
    isFertile: Boolean,
    isForecast: Boolean,
    onClick: () -> Unit
) {
    val dayNumber = date.dayOfMonth.toString()

    val boxModifier = when {
        isPeriod -> Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(PrimaryContainer)
        isOvulation -> Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(SecondaryContainer)
            .border(2.dp, SecondaryCoral, CircleShape)
        isFertile -> Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(SecondaryFixed.copy(alpha = 0.35f))
            .border(1.dp, SecondaryContainer.copy(alpha = 0.6f), CircleShape)
        isForecast -> Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(PrimaryFixed.copy(alpha = 0.35f))
            .border(1.5.dp, PrimaryDarkPlum.copy(alpha = 0.5f), CircleShape)
        else -> Modifier
            .size(34.dp)
            .clip(CircleShape)
    }

    val textColor = when {
        isPeriod -> OnPrimary
        isOvulation -> Color.White
        isFertile -> SecondaryCoral
        isForecast -> PrimaryDarkPlum
        else -> OnSurface
    }

    Box(
        modifier = boxModifier.clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayNumber,
            fontSize = 12.sp,
            fontWeight = if (isPeriod || isOvulation || isForecast) FontWeight.Bold else FontWeight.Normal,
            color = textColor
        )
    }
}
