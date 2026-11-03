package pl.amosialek.bloody.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.amosialek.bloody.domain.CycleInfo
import pl.amosialek.bloody.ui.theme.OnSurfaceVariant
import pl.amosialek.bloody.ui.theme.PrimaryContainer
import pl.amosialek.bloody.ui.theme.PrimaryDarkPlum
import pl.amosialek.bloody.ui.theme.SurfaceContainer

@Composable
fun RadialCountdownRing(
    cycleInfo: CycleInfo,
    modifier: Modifier = Modifier
) {
    // Calculate progress fraction: e.g. 24/29 completed or active ongoing days
    val targetProgress = if (cycleInfo.isPeriodOngoing) {
        val days = (cycleInfo.ongoingDays ?: 1).toFloat()
        (days / cycleInfo.averagePeriodLength.coerceAtLeast(1)).coerceIn(0.1f, 1f)
    } else {
        val total = cycleInfo.averageCycleLength.coerceAtLeast(1).toFloat()
        val completed = cycleInfo.currentCycleDay.toFloat()
        (completed / total).coerceIn(0f, 1f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "radial_progress"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(192.dp)
    ) {
        Canvas(modifier = Modifier.size(176.dp)) {
            val strokeWidth = 8.dp.toPx()

            // Background Track
            drawArc(
                color = SurfaceContainer,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            // Active Progress Ring
            drawArc(
                color = PrimaryContainer,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth + 2f,
                    cap = StrokeCap.Round
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (cycleInfo.isPeriodOngoing) "CYCLE ACTIVE" else "COUNTDOWN",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                color = OnSurfaceVariant
            )

            Text(
                text = if (cycleInfo.isPeriodOngoing) {
                    "${cycleInfo.ongoingDays ?: 1}"
                } else {
                    "${cycleInfo.daysUntilNextPeriod}"
                },
                fontSize = 44.sp,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                color = PrimaryDarkPlum,
                letterSpacing = (-1).sp
            )

            Text(
                text = if (cycleInfo.isPeriodOngoing) "Day of Period" else "Days Left",
                fontSize = 15.sp,
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
                color = OnSurfaceVariant,
                modifier = Modifier.offset(y = (-4).dp)
            )
        }
    }
}
