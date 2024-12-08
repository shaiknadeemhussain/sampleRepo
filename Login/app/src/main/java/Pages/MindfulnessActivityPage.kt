package Pages

import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MindfulnessActivityPage(modifier: Modifier = Modifier, navController: NavController) {
    // List of activities
    val activities = remember { mutableStateListOf("Meditation", "Go for Walk", "Stretch Body", "Chit Chat", "Blink Exercises", "Hydration") }
    var currentActivity by remember { mutableStateOf(activities.random()) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(300000L) } // Default 5 minutes in milliseconds
    var countDownTimer: CountDownTimer? = remember { null }

    // Timer functionality
    val startTimer: () -> Unit = {
        if (!isTimerRunning) {
            isTimerRunning = true
            countDownTimer = object : CountDownTimer(timeLeft, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft = millisUntilFinished
                }

                override fun onFinish() {
                    isTimerRunning = false
                }
            }.start()
        }
    }

    val stopTimer: () -> Unit = {
        countDownTimer?.cancel()
        isTimerRunning = false
    }

    val swapActivity: () -> Unit = {
        if (activities.size > 1) {
            activities.remove(currentActivity)
            currentActivity = activities.random()
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Mindfulness Activity", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Display current activity or timer
        if (isTimerRunning) {
            Text(
                text = "$currentActivity Timer",
                style = TextStyle(fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            val minutes = (timeLeft / 1000) / 60
            val seconds = (timeLeft / 1000) % 60
            Text(
                text = String.format("%02d:%02d", minutes, seconds),
                style = TextStyle(fontSize = 36.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { stopTimer() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Stop $currentActivity")
            }
        } else {
            // Suggestion View
            Text(
                text = "How about trying \"$currentActivity\"?",
                style = TextStyle(fontSize = 20.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { startTimer() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start $currentActivity")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { swapActivity() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Swap Activity")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate back
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
