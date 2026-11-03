package pl.amosialek.bloody.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.amosialek.bloody.R
import pl.amosialek.bloody.data.model.FlowIntensity
import pl.amosialek.bloody.data.model.PeriodRecord
import pl.amosialek.bloody.ui.theme.ErrorRed
import pl.amosialek.bloody.ui.theme.OnPrimary
import pl.amosialek.bloody.ui.theme.OnSurface
import pl.amosialek.bloody.ui.theme.OnSurfaceVariant
import pl.amosialek.bloody.ui.theme.OutlineMuted
import pl.amosialek.bloody.ui.theme.OutlineVariant
import pl.amosialek.bloody.ui.theme.PrimaryContainer
import pl.amosialek.bloody.ui.theme.PrimaryDarkPlum
import pl.amosialek.bloody.ui.theme.SurfaceContainer
import pl.amosialek.bloody.ui.theme.SurfaceContainerLow
import pl.amosialek.bloody.ui.theme.SurfaceContainerLowest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPeriodSheet(
    period: PeriodRecord,
    onSave: (PeriodRecord) -> Unit,
    onDelete: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var startDate by remember(period) { mutableStateOf(period.startDate) }
    var isOngoing by remember(period) { mutableStateOf(period.isOngoing) }
    var endDate by remember(period) { mutableStateOf(period.endDate ?: period.startDate.plusDays(4)) }
    var selectedFlow by remember(period) { mutableStateOf(period.flowIntensity) }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("MMM dd, yyyy") }

    // Calculate duration
    val calculatedDuration = if (isOngoing) {
        val today = LocalDate.now()
        val days = if (today.isBefore(startDate)) 1 else ChronoUnit.DAYS.between(startDate, today) + 1
        "$days Days (Ongoing)"
    } else {
        val days = ChronoUnit.DAYS.between(startDate, endDate) + 1
        "${days.coerceAtLeast(1)} Days Total"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceContainerLowest,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (period.id <= 0) "Log Past Period" else "Edit Past Period",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryDarkPlum
                    )
                    Text(
                        text = "Adjust dates and flow intensity",
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Close",
                        tint = OutlineMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ongoing toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SurfaceContainerLow)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Period is currently ongoing",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurface
                    )
                    Text(
                        text = "Without finish date yet",
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                }

                Switch(
                    checked = isOngoing,
                    onCheckedChange = { isOngoing = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = SurfaceContainerLowest,
                        checkedTrackColor = PrimaryContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date Pickers Bento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Start Date
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "START DATE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = OutlineMuted,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceContainer)
                            .border(1.dp, OutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable {
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val picked = LocalDate.of(year, month + 1, dayOfMonth)
                                        startDate = picked
                                        if (!isOngoing && endDate.isBefore(picked)) {
                                            endDate = picked.plusDays(4)
                                        }
                                    },
                                    startDate.year,
                                    startDate.monthValue - 1,
                                    startDate.dayOfMonth
                                ).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = startDate.format(dateFormatter),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_event),
                            contentDescription = null,
                            tint = PrimaryDarkPlum,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // End Date
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "END DATE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = if (isOngoing) OutlineVariant else OutlineMuted,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isOngoing) SurfaceContainerLow else SurfaceContainer)
                            .border(1.dp, OutlineVariant.copy(alpha = if (isOngoing) 0.2f else 0.5f), RoundedCornerShape(12.dp))
                            .clickable(enabled = !isOngoing) {
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val picked = LocalDate.of(year, month + 1, dayOfMonth)
                                        if (!picked.isBefore(startDate)) {
                                            endDate = picked
                                        }
                                    },
                                    endDate.year,
                                    endDate.monthValue - 1,
                                    endDate.dayOfMonth
                                ).show()
                            }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isOngoing) "Ongoing" else endDate.format(dateFormatter),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isOngoing) OutlineMuted else OnSurface
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_event),
                            contentDescription = null,
                            tint = if (isOngoing) OutlineVariant else PrimaryDarkPlum,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flow Intensity Selector
            Text(
                text = "FLOW INTENSITY",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = OutlineMuted,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FlowIntensity.entries.forEach { flow ->
                    val isSelected = flow == selectedFlow
                    val bg = if (isSelected) PrimaryContainer else SurfaceContainerLow
                    val border = if (isSelected) PrimaryContainer else OutlineVariant.copy(alpha = 0.5f)
                    val textCol = if (isSelected) OnPrimary else OnSurfaceVariant

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(bg)
                            .border(1.dp, border, RoundedCornerShape(12.dp))
                            .clickable { selectedFlow = flow }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = flow.displayName,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = textCol
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Duration summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLow)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recorded Duration",
                    fontSize = 13.sp,
                    color = OnSurfaceVariant
                )
                Text(
                    text = calculatedDuration,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryDarkPlum
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Save Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(PrimaryContainer)
                    .clickable {
                        val recordToSave = period.copy(
                            startDate = startDate,
                            endDate = if (isOngoing) null else endDate,
                            flowIntensity = selectedFlow
                        )
                        onSave(recordToSave)
                    }
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = OnPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Save Changes",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnPrimary
                )
            }

            // Remove Button (if amending existing)
            if (period.id > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .clickable { onDelete(period.id) }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Remove Period",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ErrorRed
                    )
                }
            }
        }
    }
}
