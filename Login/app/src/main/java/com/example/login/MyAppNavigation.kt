package com.example.login

import Pages.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.livedata.observeAsState
import com.example.login.ui.theme.AuthViewModel
import com.example.login.ui.theme.AuthState // Ensure this import is correct

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    // Observe the login status from the AuthViewModel using observeAsState
    val authState = authViewModel.authState.observeAsState(initial = AuthState.Unauthenticated).value

    // Set the start destination based on whether the user is logged in
    NavHost(
        navController = navController,
        startDestination = if (authState is AuthState.Authenticated) "home" else "login", // If logged in, go to HomePage, else LoginPage
        builder = {
            composable("login") {
                LoginPage(modifier, navController, authViewModel)
            }
            composable("signup") {
                SignupPage(modifier, navController, authViewModel)
            }
            composable("home") {
                HomePage(modifier, navController, authViewModel)
            }
            composable("break_schedule") {
                BreakSchedulePage(modifier, navController)
            }
            composable("mindfulness") {
                MindfulnessActivityPage(modifier, navController)
            }
            composable("analytics") {
                AnalyticsPage(modifier, navController)
            }
        }
    )
}
