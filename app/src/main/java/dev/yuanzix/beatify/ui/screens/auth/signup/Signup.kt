package dev.yuanzix.beatify.ui.screens.auth.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import dev.yuanzix.beatify.R
import dev.yuanzix.beatify.data.authRepository.utils.CreateUserResponse
import dev.yuanzix.beatify.ui.screens.auth.utils.EmailInput
import dev.yuanzix.beatify.ui.screens.auth.utils.ErrorDialog
import dev.yuanzix.beatify.ui.screens.auth.utils.LoadingIndicatorDialog
import dev.yuanzix.beatify.ui.screens.auth.utils.LottieSection
import dev.yuanzix.beatify.ui.screens.auth.utils.MainActionButton
import dev.yuanzix.beatify.ui.screens.auth.utils.PasswordInput
import dev.yuanzix.beatify.ui.screens.auth.utils.SubActionButton
import dev.yuanzix.beatify.ui.theme.Padding
import dev.yuanzix.beatify.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToVerifyEmail: () -> Unit,
) {
    val signupResult by viewModel.signupResult.collectAsState()
    var isSignupEnabled by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(signupResult) {
        signupResult?.let { result ->
            if (result.response == CreateUserResponse.SUCCESS) {
                onNavigateToVerifyEmail()
            }
        }
    }

    LoadingIndicatorDialog(showDialog = viewModel.showLoadingDialog) {
        viewModel.toggleLoadingDialog()
    }

    ErrorDialog(showDialog = viewModel.showErrorDialog, content = viewModel.errorMessage) {
        viewModel.dismissErrorDialog()
    }

    LaunchedEffect(
        viewModel.email,
        viewModel.password,
        viewModel.firstName,
        viewModel.lastName,
        viewModel.username,
        viewModel.dateOfBirth
    ) {
        isSignupEnabled = listOf(
            viewModel.email,
            viewModel.password,
            viewModel.firstName,
            viewModel.lastName,
            viewModel.username,
            viewModel.dateOfBirth
        ).all { it.isNotEmpty() }
    }

    LaunchedEffect(Unit) {
        delay(250)
        showContent = true
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieSection(showContent, 250, R.raw.login_signup)
            SignUpInputSection(viewModel, showContent)
            MainActionButton({ viewModel.signUp() }, "Sign Up", showContent, isSignupEnabled)
            SubActionButton(
                { onNavigateToLogin() },
                "Already a user? Log in",
                showContent
            )
        }
    }
}

@Composable
fun SignUpInputSection(viewModel: AuthViewModel, showContent: Boolean) {
    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn() + expandVertically(
            expandFrom = Alignment.Bottom, animationSpec = tween(durationMillis = 1000)
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NameInput(viewModel)
            EmailInput(viewModel)
            UsernameInput(viewModel)
            DateOfBirthInput(viewModel.dateOfBirth) { viewModel.toggleDatePicker() }
            if (viewModel.showDatePicker) ModalDatePicker({
                viewModel.updateField(
                    "dateOfBirth",
                    it
                )
            },
                { viewModel.toggleDatePicker() })
            PasswordInput(viewModel)
        }
    }
}

@Composable
fun NameInput(viewModel: AuthViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(0.85f),
    ) {
        OutlinedTextField(
            value = viewModel.firstName,
            onValueChange = { viewModel.updateField("firstName", it) },
            label = { Text("First Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .weight(1f)
                .padding(end = Padding.ExtraSmall, bottom = Padding.ExtraSmall)
        )
        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.updateField("lastName", it) },
            label = { Text("Last Name") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .weight(1f)
                .padding(start = Padding.ExtraSmall, bottom = Padding.ExtraSmall)
        )
    }
}

@Composable
fun UsernameInput(viewModel: AuthViewModel) {
    OutlinedTextField(
        value = viewModel.username,
        onValueChange = { viewModel.updateField("username", it) },
        label = { Text("Username") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(bottom = Padding.ExtraSmall)
    )
}

@Composable
fun DateOfBirthInput(value: String, onClick: () -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = { Text("Date of Birth") },
        readOnly = true,
        trailingIcon = {
            IconButton(onClick) {
                Icon(
                    imageVector = Icons.Default.DateRange, contentDescription = "Select date"
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        visualTransformation = VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(bottom = Padding.ExtraSmall)
    )
}
