package com.example.login

import Pages.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login.ui.theme.AuthViewModel
import com.example.login.ui.theme.AuthState
import androidx.navigation.NavController

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    // Observe the login status from the AuthViewModel using observeAsState
    val authState by authViewModel.authState.observeAsState(initial = AuthState.Unauthenticated)

    // States to share data between BreakSchedulePage and AnalyticsPage
    var startHour by remember { mutableStateOf("") }
    var endHour by remember { mutableStateOf("") }
    val upcomingBreaks = remember { mutableStateListOf<String>() }
    val waterReminders = remember { mutableStateListOf<String>() }
    val foodReminders = remember { mutableStateListOf<String>() }

    // Set the start destination based on whether the user is logged in
    NavHost(
        navController = navController,
        startDestination = if (authState is AuthState.Authenticated) "break_schedule" else "login", // If logged in, go to BreakSchedulePage, else LoginPage
        builder = {
            composable("login") {
                LoginPage(modifier, navController, authViewModel)
            }
            composable("signup") {
                SignupPage(modifier, navController, authViewModel)
            }
            composable("break_schedule") {
                // Make sure to pass the authViewModel here
                BreakSchedulePage(
                    modifier = modifier,
                    navController = navController,
                    authViewModel = authViewModel,
                    onBreaksUpdated = { newStartHour, newEndHour, breaks, water, food ->
                        startHour = newStartHour
                        endHour = newEndHour
                        upcomingBreaks.clear()
                        upcomingBreaks.addAll(breaks)
                        waterReminders.clear()
                        waterReminders.addAll(water)
                        foodReminders.clear()
                        foodReminders.addAll(food)
                    }
                )
            }
            composable("mindfulness") {
                MindfulnessActivityPage(modifier, navController)
            }
            composable("analytics") {
                AnalyticsPage(
                    modifier = modifier,
                    navController = navController,
                    startHour = startHour,
                    endHour = endHour,
                    breaks = upcomingBreaks,
                    waterReminders = waterReminders,
                    foodReminders = foodReminders
                )
            }
        }
    )

    // Listen for the log out event and navigate to login screen
    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            navController.navigate("login") {
                popUpTo("break_schedule") { inclusive = true }  // Clear back stack after logging out
            }
        }
    }
}
