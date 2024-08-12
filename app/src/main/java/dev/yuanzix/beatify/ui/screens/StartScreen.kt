package dev.yuanzix.beatify.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.yuanzix.beatify.navigation.Destination
import dev.yuanzix.beatify.navigation.SubGraph
import dev.yuanzix.beatify.ui.viewmodels.StartViewModel

@Composable
fun StartScreen(
    navController: NavController,
    viewModel: StartViewModel = hiltViewModel()
) {
    val userSettings = viewModel.userSettingsFlow.collectAsState(initial = null)

    LaunchedEffect(key1 = userSettings.value) {
        when {
            userSettings.value == null || userSettings.value?.jwtTokenString.isNullOrEmpty() -> {
                navController.navigate(Destination.AuthLoginScreen) {
                    popUpTo(SubGraph.StartNav) { inclusive = true }
                }
            }
            else -> {
                navController.navigate(Destination.DashHomeScreen) {
                    popUpTo(SubGraph.StartNav) { inclusive = true }
                }
            }
        }
    }
}
