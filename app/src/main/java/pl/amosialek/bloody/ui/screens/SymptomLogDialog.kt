package pl.amosialek.bloody.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.amosialek.bloody.R
import pl.amosialek.bloody.data.model.DailySymptomLog
import pl.amosialek.bloody.ui.theme.OnPrimary
import pl.amosialek.bloody.ui.theme.OnSurface
import pl.amosialek.bloody.ui.theme.OnSurfaceVariant
import pl.amosialek.bloody.ui.theme.OutlineMuted
import pl.amosialek.bloody.ui.theme.OutlineVariant
import pl.amosialek.bloody.ui.theme.PrimaryContainer
import pl.amosialek.bloody.ui.theme.PrimaryDarkPlum
import pl.amosialek.bloody.ui.theme.PrimaryFixed
import pl.amosialek.bloody.ui.theme.SurfaceContainerLowest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SymptomLogDialog(
    currentLog: DailySymptomLog,
    onToggleSymptom: (String) -> Unit,
    onSaveNotes: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val commonSymptoms = listOf(
        "😊 Normal Mood",
        "⚡ Mild Energy",
        "💧 Light Flow",
        "🌸 Calm",
        "⚡ High Energy",
        "😴 Low Energy",
        "🤕 Headache",
        "🍫 Cravings",
        "💢 Cramps",
        "🎈 Bloating",
        "✨ Glowing Skin"
    )

    var notesText by remember(currentLog) { mutableStateOf(currentLog.notes) }

    BasicAlertDialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(SurfaceContainerLowest)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Daily Balance & Log",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryDarkPlum
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Close",
                        tint = OutlineMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tap to toggle symptoms for today:",
                fontSize = 12.sp,
                color = OnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                commonSymptoms.forEach { item ->
                    val cleanName = item.substringAfter(" ")
                    val isSelected = currentLog.symptoms.contains(cleanName) || currentLog.symptoms.contains(item)
                    val bg = if (isSelected) PrimaryFixed else SurfaceContainerLowest
                    val border = if (isSelected) PrimaryContainer else OutlineVariant.copy(alpha = 0.5f)
                    val textCol = if (isSelected) PrimaryDarkPlum else OnSurface

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(bg)
                            .border(1.dp, border, RoundedCornerShape(20.dp))
                            .clickable {
                                onToggleSymptom(cleanName)
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = item,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = textCol
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Daily Notes:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = notesText,
                onValueChange = { notesText = it },
                placeholder = { Text("How are you feeling today?", fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .background(PrimaryContainer)
                    .clickable {
                        onSaveNotes(notesText)
                        onDismiss()
                    }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = null,
                    tint = OnPrimary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Done",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnPrimary
                )
            }
        }
    }
}
