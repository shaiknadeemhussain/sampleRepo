package Pages

import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BreakSchedulePage(modifier: Modifier = Modifier, navController: NavController) {
    // States for user input and reminders
    var startHour by remember { mutableStateOf("") }
    var endHour by remember { mutableStateOf("") }
    val upcomingBreaks = remember { mutableStateListOf<String>() }
    var isBreakTimerRunning by remember { mutableStateOf(false) }
    var timeUntilNextBreak by remember { mutableStateOf(0L) }
    var countDownTimer: CountDownTimer? = remember { null }

    // Helper functions
    val calculateBreaks: () -> Unit = {
        upcomingBreaks.clear()
        if (startHour.isNotEmpty() && endHour.isNotEmpty()) {
            try {
                val format = SimpleDateFormat("HH:mm", Locale.getDefault())
                val startTime = format.parse(startHour)
                val endTime = format.parse(endHour)
                val breakInterval = 60 * 60 * 1000 // 1-hour intervals

                var nextBreak = startTime.time + breakInterval
                while (nextBreak < endTime.time) {
                    upcomingBreaks.add(format.format(Date(nextBreak)))
                    nextBreak += breakInterval
                }

                if (upcomingBreaks.isNotEmpty()) {
                    val nextBreakTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(upcomingBreaks.first())
                    timeUntilNextBreak = nextBreakTime.time - Date().time
                }
            } catch (e: Exception) {
                // Handle parsing errors gracefully
            }
        }
    }

    val startBreakTimer: () -> Unit = {
        if (!isBreakTimerRunning && timeUntilNextBreak > 0) {
            isBreakTimerRunning = true
            countDownTimer = object : CountDownTimer(timeUntilNextBreak, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeUntilNextBreak = millisUntilFinished
                }

                override fun onFinish() {
                    isBreakTimerRunning = false
                    // Notify or reset for the next break
                }
            }.start()
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Break Schedule", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // User input for working hours
        TextField(
            value = startHour,
            onValueChange = { startHour = it },
            label = { Text("Start Hour (HH:mm)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = endHour,
            onValueChange = { endHour = it },
            label = { Text("End Hour (HH:mm)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to calculate breaks
        Button(
            onClick = {
                calculateBreaks()
                startBreakTimer()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set Working Hours and Start Reminders")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display next break or timer
        if (isBreakTimerRunning) {
            val minutes = (timeUntilNextBreak / 1000) / 60
            val seconds = (timeUntilNextBreak / 1000) % 60
            Text(
                text = "Time until next break: ${String.format("%02d:%02d", minutes, seconds)}",
                style = MaterialTheme.typography.bodyLarge
            )
        } else if (upcomingBreaks.isNotEmpty()) {
            Text("Next break: ${upcomingBreaks.first()}", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to start mindfulness activity
        Button(
            onClick = {
                navController.navigate("mindfulness")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Mindfulness Activity")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to view analytics
        Button(
            onClick = {
                navController.navigate("analytics")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Break Analytics")
        }
    }
}
