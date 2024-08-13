package dev.yuanzix.beatify.ui.screens.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
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
import dev.yuanzix.beatify.data.auth_repository.utils.LoginResponse
import dev.yuanzix.beatify.ui.screens.auth.utils.EmailInput
import dev.yuanzix.beatify.ui.screens.auth.utils.ErrorDialog
import dev.yuanzix.beatify.ui.screens.auth.utils.LoadingIndicatorDialog
import dev.yuanzix.beatify.ui.screens.auth.utils.LottieSection
import dev.yuanzix.beatify.ui.screens.auth.utils.MainActionButton
import dev.yuanzix.beatify.ui.screens.auth.utils.PasswordInput
import dev.yuanzix.beatify.ui.screens.auth.utils.SubActionButton
import dev.yuanzix.beatify.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onNavigateToVerifyEmail: () -> Unit,
) {
    val loginResult by viewModel.loginResult.collectAsState()
    var isLoginEnabled by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    ErrorDialog(showDialog = viewModel.showErrorDialog, content = viewModel.errorMessage) {
        viewModel.dismissErrorDialog()
    }

    LoadingIndicatorDialog(showDialog = viewModel.showLoadingDialog) {
        viewModel.toggleLoadingDialog()
    }

    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            if (result.error == LoginResponse.NOT_VERIFIED) {
                onNavigateToVerifyEmail()
            } else if (result.error == LoginResponse.SUCCESS) {
                onLoginSuccess()
            }
        }
    }

    LaunchedEffect(viewModel.email, viewModel.password) {
        isLoginEnabled = viewModel.email.isNotEmpty() && viewModel.password.isNotEmpty()
    }

    LaunchedEffect(Unit) {
        delay(250)
        showContent = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieSection(showContent, 400, R.raw.login_signup)
            InputSection(viewModel, showContent)
            MainActionButton(
                {
                    viewModel.login()
                }, "Log In", showContent, isLoginEnabled
            )
            SubActionButton(
                onNavigateToSignup,
                "Not an existing user? Sign Up",
                showContent
            )
        }
    }
}

@Composable
fun InputSection(viewModel: AuthViewModel, showContent: Boolean) {
    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn() + expandVertically(
            expandFrom = Alignment.Bottom,
            animationSpec = tween(durationMillis = 1000)
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EmailInput(viewModel)
            PasswordInput(viewModel)
        }
    }
}

