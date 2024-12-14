package Pages

import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.login.ui.theme.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreakSchedulePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel, // ViewModel for sign-out
    onBreaksUpdated: (String, String, List<String>, List<String>, List<String>) -> Unit // Callback to update break data
) {
    // States for user input, reminders, and errors
    var startHour by remember { mutableStateOf("") }
    var endHour by remember { mutableStateOf("") }
    val upcomingBreaks = remember { mutableStateListOf<String>() }
    val breakMessages = remember { mutableStateListOf<String>() }
    var isBreakTimerRunning by remember { mutableStateOf(false) }
    var timeUntilNextBreak by remember { mutableStateOf(0L) }
    var countDownTimer: CountDownTimer? by remember { mutableStateOf(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // State for showing the sign-out confirmation dialog
    var showSignOutDialog by remember { mutableStateOf(false) }

    // Helper function to calculate breaks and reminders
    fun calculateBreaks() {
        upcomingBreaks.clear()
        breakMessages.clear()
        errorMessage = null

        if (startHour.isEmpty() || endHour.isEmpty()) {
            errorMessage = "Both start and end times are required."
            return
        }

        try {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val startTime = format.parse(startHour)
            val endTime = format.parse(endHour)

            if (startTime.after(endTime)) {
                errorMessage = "Start time must be earlier than end time."
                return
            }

            val breakInterval = 2 * 60 * 60 * 1000 // 2-hour intervals for breaks
            val waterInterval = (2.5 * 60 * 60 * 1000).toLong() // 2.5-hour interval for water reminders
            val foodInterval = 5 * 60 * 60 * 1000 // 5-hour interval for food reminders

            var nextBreak = startTime.time + breakInterval
            while (nextBreak < endTime.time) {
                upcomingBreaks.add("Break at ${format.format(Date(nextBreak))}")
                nextBreak += breakInterval
            }

            var nextWaterReminder = startTime.time + waterInterval
            while (nextWaterReminder < endTime.time) {
                breakMessages.add("DRINK WATER at ${format.format(Date(nextWaterReminder))}")
                nextWaterReminder += waterInterval
            }

            var nextFoodReminder = startTime.time + foodInterval
            while (nextFoodReminder < endTime.time) {
                breakMessages.add("FEED YOUR BELLY at ${format.format(Date(nextFoodReminder))}")
                nextFoodReminder += foodInterval
            }

            // Calculate time until the next break
            if (upcomingBreaks.isNotEmpty()) {
                val nextBreakTime = format.parse(upcomingBreaks.first().split("at")[1].trim())
                timeUntilNextBreak = nextBreakTime.time - Date().time
            }

        } catch (e: Exception) {
            errorMessage = "Invalid time format. Please use HH:mm."
        }
    }

    // Function to start the countdown timer
    fun startBreakTimer() {
        if (!isBreakTimerRunning && timeUntilNextBreak > 0) {
            isBreakTimerRunning = true
            countDownTimer?.cancel() // Cancel any existing timer
            countDownTimer = object : CountDownTimer(timeUntilNextBreak, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeUntilNextBreak = millisUntilFinished
                }

                override fun onFinish() {
                    isBreakTimerRunning = false
                    calculateBreaks() // Recalculate breaks after the timer finishes
                }
            }.start()
        }
    }

    // Clean up the timer on disposal
    DisposableEffect(Unit) {
        onDispose {
            countDownTimer?.cancel()
        }
    }

    // Scaffold for layout with top bar
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Break Schedule") },
                actions = {
                    IconButton(onClick = {
                        showSignOutDialog = true // Show the sign-out confirmation dialog
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Sign Out")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Set Working Hours", style = MaterialTheme.typography.headlineMedium)

            // Input fields for start and end hours
            TextField(
                value = startHour,
                onValueChange = { startHour = it },
                label = { Text("Start Hour (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = endHour,
                onValueChange = { endHour = it },
                label = { Text("End Hour (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Display error messages
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Button to calculate breaks and start the timer
            Button(
                onClick = {
                    calculateBreaks()
                    startBreakTimer()
                    // Update the breaks data in the parent composable
                    onBreaksUpdated(
                        startHour,
                        endHour,
                        upcomingBreaks,
                        breakMessages.filter { it.startsWith("DRINK WATER") },
                        breakMessages.filter { it.startsWith("FEED YOUR BELLY") }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Set Working Hours and Start Reminders")
            }

            // Display timer or next break
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

            // Display special reminders
            breakMessages.forEach { message ->
                Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
            }

            // Button to navigate to mindfulness activity
            Button(
                onClick = {
                    navController.navigate("mindfulness")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Mindfulness Activity")
            }

            // Button to navigate to analytics
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

    // Sign-out confirmation dialog
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            title = { Text("Sign Out") },
            text = { Text("Are you sure you want to sign out?") },
            confirmButton = {
                TextButton(onClick = {
                    authViewModel.signout() // Perform the sign-out action
                    navController.navigate("login") {
                        popUpTo("break_schedule") { inclusive = true }
                    }
                    showSignOutDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSignOutDialog = false // Dismiss the dialog and stay on the same page
                }) {
                    Text("No")
                }
            }
        )
    }
}
