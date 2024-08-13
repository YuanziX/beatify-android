package dev.yuanzix.beatify.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.yuanzix.beatify.R
import dev.yuanzix.beatify.ui.screens.auth.utils.LottieSection
import dev.yuanzix.beatify.ui.viewmodels.StartViewModel
import kotlinx.coroutines.delay

@Composable
fun StartScreen(
    viewModel: StartViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit,
    onNavigateToDash: () -> Unit,
) {
    val userSettings by viewModel.userSettingsFlow.collectAsState(initial = null)
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(250)
        showContent = true
    }

    LaunchedEffect(userSettings) {
        if (userSettings != null) {
            if (userSettings?.jwtTokenString.isNullOrEmpty()) {
                onNavigateToAuth()
            } else {
                onNavigateToDash()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        LottieSection(showContent = showContent, size = 400, resourceID = R.raw.login_signup)
    }
}
