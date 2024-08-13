package dev.yuanzix.beatify.ui.screens.auth.verify_email

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.yuanzix.beatify.R
import dev.yuanzix.beatify.ui.screens.auth.utils.LottieSection
import dev.yuanzix.beatify.ui.screens.auth.utils.MainActionButton
import dev.yuanzix.beatify.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun VerifyEmailScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onVerificationSuccess: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        var showContent by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(Unit) {
            delay(250)
            showContent = true
        }

        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieSection(
                showContent = showContent,
                size = 400,
                resourceID = R.raw.verify_email
            )
            MainActionButton(
                onClick = {
                    onVerificationSuccess()
                },
                text = "Verified? Click here",
                showContent = showContent,
                isEnabled = true
            )
        }
    }
}