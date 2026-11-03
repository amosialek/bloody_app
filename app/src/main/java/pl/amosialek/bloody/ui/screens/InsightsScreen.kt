package pl.amosialek.bloody.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.amosialek.bloody.R
import pl.amosialek.bloody.ui.theme.OnSurface
import pl.amosialek.bloody.ui.theme.OnSurfaceVariant
import pl.amosialek.bloody.ui.theme.PrimaryDarkPlum
import pl.amosialek.bloody.ui.theme.PrimaryFixed
import pl.amosialek.bloody.ui.theme.SecondaryCoral
import pl.amosialek.bloody.ui.theme.SecondaryFixed
import pl.amosialek.bloody.ui.theme.SurfaceContainerLow
import pl.amosialek.bloody.ui.theme.SurfaceContainerLowest
import pl.amosialek.bloody.ui.theme.SurfaceWarm
import pl.amosialek.bloody.ui.viewmodel.CycleUiState
import java.time.format.DateTimeFormatter

@Composable
fun InsightsScreen(
    uiState: CycleUiState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceWarm)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = "Biometric Insights",
                fontSize = 24.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryDarkPlum
            )
            Text(
                text = "Clinical and statistical overview of your cycles",
                fontSize = 12.sp,
                color = OnSurfaceVariant
            )
        }

        // Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Average Cycle Length Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(PrimaryFixed.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_vital_signs),
                            contentDescription = null,
                            tint = PrimaryDarkPlum,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "Avg Cycle",
                        fontSize = 11.sp,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = "${uiState.cycleInfo.averageCycleLength} days",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDarkPlum
                    )
                    Text(
                        text = uiState.cycleInfo.cycleRegularity,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SecondaryCoral
                    )
                }
            }

            // Average Period Duration Card
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SecondaryFixed.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_water_drop),
                            contentDescription = null,
                            tint = SecondaryCoral,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = "Avg Duration",
                        fontSize = 11.sp,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = "${uiState.cycleInfo.averagePeriodLength} days",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryDarkPlum
                    )
                    Text(
                        text = "Healthy Range",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SecondaryCoral
                    )
                }
            }
        }

        // Current Cycle Phase In-Depth
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Current Phase: ${uiState.cycleInfo.currentPhase.displayName}",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryDarkPlum
                )
                Text(
                    text = uiState.cycleInfo.currentPhase.description,
                    fontSize = 13.sp,
                    color = OnSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Fertility Status", fontSize = 12.sp, color = OnSurfaceVariant)
                        Text(
                            text = uiState.cycleInfo.fertilityLevel.displayName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryDarkPlum
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Next Estimated Ovulation", fontSize = 12.sp, color = OnSurfaceVariant)
                        Text(
                            text = uiState.cycleInfo.ovulationDate.format(dateFormatter),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SecondaryCoral
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Fertile Window", fontSize = 12.sp, color = OnSurfaceVariant)
                        Text(
                            text = "${uiState.cycleInfo.fertileWindowStart.format(dateFormatter)} – ${uiState.cycleInfo.fertileWindowEnd.format(dateFormatter)}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OnSurface
                        )
                    }
                }
            }
        }

        // Logged History Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "History Summary",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryDarkPlum
                )
                Text(
                    text = "You have ${uiState.periods.size} cycles recorded. With regular logging, predictions become increasingly personalized.",
                    fontSize = 13.sp,
                    color = OnSurfaceVariant
                )
            }
        }
    }
}
