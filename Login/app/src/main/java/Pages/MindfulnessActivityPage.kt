package Pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MindfulnessActivityPage(modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Mindfulness Activity", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // A simple timer or meditation guide (this could be dynamic in a real app)
        Text(
            text = "Take a moment to relax and focus on your breath.",
            style = TextStyle(fontSize = 18.sp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Action for starting or continuing the mindfulness session
                // e.g., navigate to a timer page or start a meditation guide
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Meditation")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Navigate back to the BreakSchedulePage or HomePage
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Back")
        }
    }
}
