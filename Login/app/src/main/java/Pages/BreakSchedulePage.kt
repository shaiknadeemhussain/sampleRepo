package Pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BreakSchedulePage(modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Break Schedule", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder for AI-generated break schedule functionality
        Text("Your next break is in 45 minutes", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Navigate to mindfulness activity page
                navController.navigate("mindfulness")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Mindfulness Activity")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Navigate to analytics page
                navController.navigate("analytics")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("View Break Analytics")
        }
    }
}
