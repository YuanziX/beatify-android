package dev.yuanzix.beatify.ui.screens.auth.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun MainActionButton(onClick: () -> Unit, text: String, showContent: Boolean, isEnabled: Boolean) {
    val buttonScale by animateFloatAsState(
        targetValue = if (isEnabled) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )

    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn() + scaleIn(),
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(50.dp)
                .scale(buttonScale),
        ) {
            Text(text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun SubActionButton(onClick: () -> Unit, text: String, showContent: Boolean) {
    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn() + scaleIn(),
    ) {
        TextButton(
            onClick = onClick,
        ) {
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}