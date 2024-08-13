package dev.yuanzix.beatify.ui.screens.auth.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ErrorDialog(showDialog: Boolean, content: StateFlow<String?>, onDismiss: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Error") },
            text = { Text(content.collectAsState().value ?: "") },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun LoadingIndicatorDialog(showDialog: Boolean, onDismiss: () -> Unit) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Surface(
                shape = CircleShape
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}