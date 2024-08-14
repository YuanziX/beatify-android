package dev.yuanzix.beatify.ui.screens.home.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.yuanzix.beatify.models.Music
import dev.yuanzix.beatify.ui.theme.Padding
import dev.yuanzix.beatify.ui.viewmodels.DashViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HomeScreen(
    viewModel: DashViewModel = hiltViewModel(),
) {
    val musicList by viewModel.musicList.collectAsState()
    val isLoading by viewModel.showLoading.collectAsState()
    val hasReachedEnd by viewModel.hasReachedEnd.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo to listState.layoutInfo.totalItemsCount
        }.distinctUntilChanged().collect { (visibleItems, totalItemsCount) ->
            val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: 0
            val threshold = (totalItemsCount * 0.9f).toInt()

            if (lastVisibleItemIndex >= threshold && !isLoading && !hasReachedEnd) {
                viewModel.loadNextPage()
            }
        }
    }

    LaunchedEffect(Unit) {
        if (musicList.isEmpty()) {
            viewModel.loadNextPage()
        }
    }

    LazyColumn(state = listState) {
        item {
            Text(
                text = "Songs",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        items(musicList) { music ->
            MusicItem(music, viewModel::playMusic)
        }
        item {
            when {
                isLoading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                hasReachedEnd -> HorizontalDivider(modifier = Modifier.padding(top = Padding.Small))
            }
        }
    }
}

@Composable
fun MusicItem(music: Music, playMusic: (Music) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                playMusic(music)
            }, colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(modifier = Modifier.padding(horizontal = Padding.Large, vertical = Padding.Medium)) {
            Column {
                Text(text = music.title, style = MaterialTheme.typography.titleMedium)
                Text(text = music.artist, style = MaterialTheme.typography.bodyMedium)
                Text(text = "Year: ${music.year}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}