package pl.amosialek.bloody.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.amosialek.bloody.R
import pl.amosialek.bloody.ui.components.RadialCountdownRing
import pl.amosialek.bloody.ui.theme.OnPrimary
import pl.amosialek.bloody.ui.theme.OnSurface
import pl.amosialek.bloody.ui.theme.OnSurfaceVariant
import pl.amosialek.bloody.ui.theme.OutlineVariant
import pl.amosialek.bloody.ui.theme.PrimaryContainer
import pl.amosialek.bloody.ui.theme.PrimaryDarkPlum
import pl.amosialek.bloody.ui.theme.PrimaryFixed
import pl.amosialek.bloody.ui.theme.SecondaryCoral
import pl.amosialek.bloody.ui.theme.SurfaceContainer
import pl.amosialek.bloody.ui.theme.SurfaceContainerHigh
import pl.amosialek.bloody.ui.theme.SurfaceContainerLow
import pl.amosialek.bloody.ui.theme.SurfaceContainerLowest
import pl.amosialek.bloody.ui.theme.SurfaceWarm
import pl.amosialek.bloody.ui.viewmodel.CycleUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TodayScreen(
    uiState: CycleUiState,
    onTogglePeriodToday: (Boolean) -> Unit,
    onOpenCalendar: () -> Unit,
    onOpenLogSymptoms: () -> Unit,
    onOpenDailyNotes: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd")
    val forecastDateFormatter = DateTimeFormatter.ofPattern("MMM dd")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceWarm)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Leading Avatar circle with floral / luxury motif
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PrimaryFixed)
                        .border(1.dp, OutlineVariant.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🌸",
                        fontSize = 18.sp
                    )
                }

                Column {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryDarkPlum
                    )
                    Text(
                        text = "Today, ${today.format(dateFormatter)}",
                        fontSize = 11.sp,
                        color = OnSurfaceVariant
                    )
                }
            }

            // Trailing action: Calendar icon with notification indicator
            Box {
                IconButton(onClick = onOpenCalendar) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar_month),
                        contentDescription = "Calendar",
                        tint = PrimaryDarkPlum,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(SecondaryCoral)
                        .align(Alignment.TopEnd)
                        .padding(top = 6.dp, end = 6.dp)
                )
            }
        }

        // Hero Cycle Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(28.dp),
                    ambientColor = PrimaryDarkPlum.copy(alpha = 0.08f),
                    spotColor = PrimaryDarkPlum.copy(alpha = 0.08f)
                ),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Phase & Fertility Badges Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cycle Day & Phase Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SurfaceContainer)
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(PrimaryContainer)
                        )
                        Text(
                            text = if (uiState.cycleInfo.isPeriodOngoing) {
                                "Period Day ${uiState.cycleInfo.ongoingDays ?: 1} • Ongoing"
                            } else {
                                "Cycle Day ${uiState.cycleInfo.currentCycleDay} • ${uiState.cycleInfo.currentPhase.displayName.substringBefore(" Phase")}"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurfaceVariant,
                            letterSpacing = 0.4.sp
                        )
                    }

                    // Fertility Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SurfaceContainer)
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Fertility:",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                        Text(
                            text = uiState.cycleInfo.fertilityLevel.displayName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDarkPlum
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Radial Countdown Visualizer
                RadialCountdownRing(cycleInfo = uiState.cycleInfo)

                Spacer(modifier = Modifier.height(16.dp))

                // Period Started Interactive Switch Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Period started",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface
                        )
                        Text(
                            text = if (uiState.isPeriodActiveToday) "Tap to log period finished" else "Log start & finish",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                    }

                    Switch(
                        checked = uiState.isPeriodActiveToday,
                        onCheckedChange = { onTogglePeriodToday(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SurfaceContainerLowest,
                            checkedTrackColor = PrimaryContainer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Secondary Action Buttons Cluster
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Log Symptoms
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(PrimaryContainer)
                            .clickable { onOpenLogSymptoms() }
                            .padding(vertical = 11.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_add),
                            contentDescription = null,
                            tint = OnPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Log Symptoms",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnPrimary
                        )
                    }

                    // Daily Notes
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(SurfaceContainer)
                            .clickable { onOpenDailyNotes() }
                            .padding(vertical = 11.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit_note),
                            contentDescription = null,
                            tint = PrimaryDarkPlum,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Daily Notes",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryDarkPlum
                        )
                    }
                }
            }
        }

        // Today's Logged State & Symptom Chips Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Balance",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryDarkPlum
                )
                Text(
                    text = "${uiState.todayLog.symptoms.size} logged",
                    fontSize = 12.sp,
                    color = OnSurfaceVariant
                )
            }

            // Horizontally Scrollable Symptom Chips Carousel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                uiState.todayLog.symptoms.forEach { item ->
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SurfaceContainerLowest)
                            .border(1.dp, OutlineVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = item,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurface
                        )
                    }
                }

                // Add New Symptom Action Button
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(PrimaryFixed.copy(alpha = 0.5f))
                        .border(1.dp, PrimaryFixed, RoundedCornerShape(20.dp))
                        .clickable { onOpenLogSymptoms() }
                        .padding(horizontal = 14.dp, vertical = 9.dp),
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
                        text = "Add Symptom",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDarkPlum
                    )
                }
            }
        }

        // Quick Insights & Forecast Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(PrimaryFixed.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_auto_graph),
                            contentDescription = null,
                            tint = PrimaryDarkPlum,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Cycle Forecast",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.SemiBold,
                            color = PrimaryDarkPlum
                        )
                        Text(
                            text = "Intelligent prediction based on ${uiState.cycleInfo.recordedCyclesCount} cycles",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }

                // Next Period Spec
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_event),
                        contentDescription = null,
                        tint = PrimaryDarkPlum,
                        modifier = Modifier.size(20.dp)
                    )

                    Column {
                        Text(
                            text = "Next Period expected",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                        Text(
                            text = "${uiState.cycleInfo.nextPeriodStartDate.format(forecastDateFormatter)} – ${uiState.cycleInfo.nextPeriodEndDate.format(forecastDateFormatter)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDarkPlum
                        )
                        Text(
                            text = "Approx. ${uiState.cycleInfo.averagePeriodLength} days duration",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }

                // Cycle Consistency Spec
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_vital_signs),
                        contentDescription = null,
                        tint = SecondaryCoral,
                        modifier = Modifier.size(20.dp)
                    )

                    Column {
                        Text(
                            text = "Recent Cycle Length",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = uiState.cycleInfo.cycleRegularity,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            Text(
                                text = "(${uiState.cycleInfo.averageCycleLength} days average)",
                                fontSize = 12.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
