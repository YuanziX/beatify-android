package dev.yuanzix.beatify.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dev.yuanzix.beatify.ui.screens.StartScreen
import dev.yuanzix.beatify.ui.screens.auth.login.LoginScreen
import dev.yuanzix.beatify.ui.screens.auth.signup.SignUpScreen
import dev.yuanzix.beatify.ui.screens.home.home.HomeScreen
import dev.yuanzix.beatify.ui.viewmodels.AuthViewModel

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SubGraph.StartNav) {
        navigation<SubGraph.StartNav>(startDestination = Destination.StartScreen) {
            composable<Destination.StartScreen> {
                StartScreen(navController = navController)
            }
        }
        navigation<SubGraph.AuthNav>(startDestination = Destination.AuthLoginScreen) {
            composable<Destination.AuthLoginScreen> { backStackEntry ->
                val authViewModel = backStackEntry.sharedViewModel<AuthViewModel>(navController)
                LoginScreen(navController = navController, viewModel = authViewModel)
            }
            composable<Destination.AuthSignupScreen> { backStackEntry ->
                val authViewModel = backStackEntry.sharedViewModel<AuthViewModel>(navController)
                SignUpScreen(navController = navController, viewModel = authViewModel)
            }
            composable<Destination.AuthVerifyEmailScreen> { backStackEntry ->
                val authViewModel = backStackEntry.sharedViewModel<AuthViewModel>(navController)
            }
        }
        navigation<SubGraph.DashNav>(startDestination = Destination.DashHomeScreen) {
            composable<Destination.DashHomeScreen> {
                HomeScreen()
            }
            composable<Destination.DashSettingsScreen> {
            }
            composable<Destination.DashPlaylistScreen> {
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}