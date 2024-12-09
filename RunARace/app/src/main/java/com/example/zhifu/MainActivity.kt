package com.example.zhifu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RaceApp()
        }
    }
}

@Composable
fun RaceApp() {
    val player1Progress = remember { mutableFloatStateOf(0f) }
    val player2Progress = remember { mutableFloatStateOf(0f) }
    val isRunning = remember { mutableStateOf(false) }
    val winner = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Run a race",
            fontSize = 24.sp,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_run),
            contentDescription = "Running Person Icon",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        PlayerProgress(
            stringResource(R.string.zhifu_1),
            player1Progress,
            MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        PlayerProgress(
            stringResource(R.string.zhifu_2),
            player2Progress,
            MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(48.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (!isRunning.value) {
                        isRunning.value = true
                        scope.launch {
                            simulateRace(player1Progress, player2Progress, isRunning, winner)
                        }
                    }
                },
                enabled = !isRunning.value,
                modifier = Modifier.width(160.dp)
            ) {
                Text(stringResource(R.string.start))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    player1Progress.floatValue = 0f
                    player2Progress.floatValue = 0f
                    isRunning.value = false
                    winner.value = ""
                },
                enabled = isRunning.value,
                modifier = Modifier.width(160.dp)
            ) {
                Text(stringResource(R.string.reset))
            }

            Spacer(modifier = Modifier.height(8.dp))
            if (winner.value.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.winner, winner.value),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun PlayerProgress(
    name: String,
    playerProgress: MutableState<Float>,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(end = 8.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = { playerProgress.value },
                color = color,
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string._0),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(R.string._100),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

fun getRandomFloat(min: Float, max: Float): Float {
    return Random.nextFloat() * (max - min) + min
}

suspend fun simulateRace(
    player1Progress: MutableState<Float>,
    player2Progress: MutableState<Float>,
    isRunning: MutableState<Boolean>,
    winner: MutableState<String>
) {
    val player1SpeedFactor = getRandomFloat(0.01f, 0.1f)
    val player2SpeedFactor = getRandomFloat(0.01f, 0.1f)
    while (player1Progress.value < 1f && player2Progress.value < 1f && isRunning.value) {
        player1Progress.value = minOf(player1Progress.value + player1SpeedFactor, 1f)
        player2Progress.value = minOf(player2Progress.value + player2SpeedFactor, 1f)
        delay(100)
    }
    if (player1Progress.value >= 1f && player2Progress.value < 1f) {
        winner.value = "Zhifu 1"
    } else if (player2Progress.value >= 1f && player1Progress.value < 1f) {
        winner.value = "Zhifu 2"
    } else {
        winner.value = "It's a tie!"
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RaceApp()
}