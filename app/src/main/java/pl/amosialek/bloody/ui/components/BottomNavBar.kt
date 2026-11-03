package pl.amosialek.bloody.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.amosialek.bloody.R
import pl.amosialek.bloody.ui.theme.OnSurfaceVariant
import pl.amosialek.bloody.ui.theme.PrimaryDarkPlum
import pl.amosialek.bloody.ui.theme.PrimaryFixed
import pl.amosialek.bloody.ui.theme.SurfaceWarm

enum class AppDestination(val label: String, val iconRes: Int) {
    TODAY("Today", R.drawable.ic_water_drop),
    CALENDAR("Calendar", R.drawable.ic_calendar_month),
    INSIGHTS("Insights", R.drawable.ic_auto_graph),
    SETTINGS("Settings", R.drawable.ic_tune)
}

@Composable
fun BottomNavBar(
    currentDestination: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(SurfaceWarm)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppDestination.entries.forEach { destination ->
                val selected = destination == currentDestination
                val activeBg = if (selected) PrimaryFixed.copy(alpha = 0.5f) else Color.Transparent
                val contentColor = if (selected) PrimaryDarkPlum else OnSurfaceVariant

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(activeBg)
                        .clickable { onDestinationSelected(destination) }
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(destination.iconRes),
                        contentDescription = destination.label,
                        tint = contentColor,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = destination.label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        color = contentColor,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
