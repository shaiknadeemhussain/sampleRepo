package Pages

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AnalyticsPage(modifier: Modifier = Modifier, navController: NavController) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Break Analytics", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Example analytics data (could be dynamic in a real app)
        Text("Total Breaks Taken: 5", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Total Time Spent on Breaks: 30 minutes", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Break Frequency: Every 60 minutes", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Action to go back to the BreakSchedulePage or HomePage
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Back")
        }
    }
}
