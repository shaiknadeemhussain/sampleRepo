package Pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.Canvas
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalyticsPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    startHour: String,
    endHour: String,
    breaks: List<String>,
    waterReminders: List<String>,
    foodReminders: List<String>
) {
    // Calculate analytics
    val totalBreaks = breaks.size
    val totalWaterReminders = waterReminders.size
    val totalFoodReminders = foodReminders.size
    val breakFrequency = if (totalBreaks > 1) {
        try {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val firstBreak = format.parse(breaks.first())
            val secondBreak = format.parse(breaks[1])
            val frequencyMinutes = (secondBreak.time - firstBreak.time) / (60 * 1000)
            "$frequencyMinutes minutes"
        } catch (e: Exception) {
            "N/A"
        }
    } else {
        "N/A"
    }

    // Calculate total minutes and break/work percentages
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    val startTime = format.parse(startHour)
    val endTime = format.parse(endHour)
    val totalMinutes = ((endTime.time - startTime.time) / (60 * 1000)).toInt()
    val breakMinutes = totalBreaks * 10 // Assuming each break is 10 minutes
    val workMinutes = totalMinutes - breakMinutes

    val breakPercentage = (breakMinutes.toFloat() / totalMinutes) * 100
    val workPercentage = (workMinutes.toFloat() / totalMinutes) * 100

    // Pie chart data
    val chartData = listOf(breakPercentage, workPercentage)
    val chartColors = listOf(Color.Red, Color.Blue)

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Break Analytics", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Analytics Data
        Text("Start Time: $startHour", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("End Time: $endHour", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total Breaks Taken: $totalBreaks", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total Water Reminders: $totalWaterReminders", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total Food Reminders: $totalFoodReminders", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Break Frequency: $breakFrequency", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        // Custom Pie Chart
        Text("Time Distribution", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Canvas(modifier = Modifier.height(200.dp).fillMaxWidth()) {
            val total = chartData.sum()
            var startAngle = -90f
            val radius = size.minDimension / 2

            // Draw each slice of the pie chart
            chartData.forEachIndexed { index, value ->
                val sweepAngle = (value / total) * 360f
                drawArc(
                    color = chartColors[index].copy(alpha = 0.8f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                    topLeft = androidx.compose.ui.geometry.Offset((size.width - radius * 2) / 2, (size.height - radius * 2) / 2)
                )
                startAngle += sweepAngle
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Go Back Button
        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Back")
        }
    }
}
