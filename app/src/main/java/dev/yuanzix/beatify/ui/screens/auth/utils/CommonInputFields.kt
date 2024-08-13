package dev.yuanzix.beatify.ui.screens.auth.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import dev.yuanzix.beatify.ui.theme.Padding
import dev.yuanzix.beatify.ui.viewmodels.AuthViewModel

@Composable
fun EmailInput(viewModel: AuthViewModel) {
    OutlinedTextField(
        value = viewModel.email,
        onValueChange = { viewModel.updateField("email", it) },
        label = { Text("Email") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(bottom = Padding.ExtraSmall)
    )
}

@Composable
fun PasswordInput(viewModel: AuthViewModel) {
    OutlinedTextField(
        value = viewModel.password,
        onValueChange = { viewModel.updateField("password", it) },
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(bottom = Padding.Medium)
    )
}

