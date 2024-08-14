package dev.yuanzix.beatify.ui.screens.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.yuanzix.beatify.models.Music
import dev.yuanzix.beatify.ui.theme.Padding
import dev.yuanzix.beatify.ui.viewmodels.DashViewModel

@ExperimentalMaterial3Api
@Composable
fun CollapsibleExoPlayer(viewModel: DashViewModel) {
    var isExpanded by remember { mutableStateOf(false) }
    val currentMusic by viewModel.currentMusic.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    val transition = updateTransition(targetState = isExpanded, label = "expansion")

    if (currentMusic == null) return

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { isExpanded = !isExpanded }) {
        transition.AnimatedContent(transitionSpec = {
            if (targetState) {
                fadeIn(animationSpec = tween(300)) + expandVertically(
                    expandFrom = Alignment.Bottom, animationSpec = tween(300)
                ) togetherWith fadeOut(animationSpec = tween(300))
            } else {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(
                    animationSpec = tween(
                        300
                    )
                ) + shrinkVertically(
                    shrinkTowards = Alignment.Bottom, animationSpec = tween(300)
                )
            }.using(SizeTransform(clip = false))
        }) { targetExpanded ->
            if (!targetExpanded) {
                CollapsedContent(
                    isPlaying = isPlaying, viewModel = viewModel, currentMusic = currentMusic!!
                )
            } else {
                ExpandedContent(
                    isPlaying = isPlaying, viewModel = viewModel, currentMusic = currentMusic!!
                )
            }
        }
    }

}

@Composable
fun CollapsedContent(isPlaying: Boolean, viewModel: DashViewModel, currentMusic: Music) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(Padding.Medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = currentMusic.title, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = currentMusic.artist, style = MaterialTheme.typography.bodyMedium
            )
        }
        Icon(
            modifier = Modifier
                .size(Padding.Large)
                .clickable {
                    viewModel.togglePlayPause()
                },
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ExpandedContent(isPlaying: Boolean, viewModel: DashViewModel, currentMusic: Music) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(Padding.Medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = currentMusic.title, style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = currentMusic.artist, style = MaterialTheme.typography.bodyLarge
        )
        Icon(
            modifier = Modifier
                .size(Padding.Large)
                .clickable {
                    viewModel.togglePlayPause()
                },
            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}