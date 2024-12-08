package Pages

import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MindfulnessActivityPage(modifier: Modifier = Modifier, navController: NavController) {
    // State to control the timer and its UI
    var isTimerRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(300000L) } // 5 minutes in milliseconds
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

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Mindfulness Activity", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Display the timer or message based on the state
        if (isTimerRunning) {
            val minutes = (timeLeft / 1000) / 60
            val seconds = (timeLeft / 1000) % 60
            Text(
                text = String.format("%02d:%02d", minutes, seconds),
                style = TextStyle(fontSize = 36.sp)
            )
        } else {
            Text(
                text = "Take a moment to relax and focus on your breath.",
                style = TextStyle(fontSize = 18.sp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Display button to start or stop the meditation
        if (!isTimerRunning) {
            Button(
                onClick = { startTimer() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Meditation")
            }
        } else {
            // Display the stop button when the timer is running
            Button(
                onClick = { stopTimer() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Stop Meditation")
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
