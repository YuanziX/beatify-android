package dev.yuanzix.beatify.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yuanzix.beatify.data.musicRepository.MusicRepository
import dev.yuanzix.beatify.data.musicRepository.utils.GetMusicListResponse
import dev.yuanzix.beatify.data.musicRepository.utils.GetMusicURL
import dev.yuanzix.beatify.models.Music
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer,
    private val repository: MusicRepository,
) : ViewModel() {

    private val _currentMusic = MutableStateFlow<Music?>(null)
    val currentMusic: StateFlow<Music?> = _currentMusic

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _showErrorDialog = MutableStateFlow(false)
    val showErrorDialog: StateFlow<Boolean> = _showErrorDialog

    private val _showLoading = MutableStateFlow(false)
    val showLoading: StateFlow<Boolean> = _showLoading

    private val _musicList = MutableStateFlow<List<Music>>(emptyList())
    val musicList: StateFlow<List<Music>> = _musicList

    private val _hasReachedEnd = MutableStateFlow(false)
    val hasReachedEnd: StateFlow<Boolean> = _hasReachedEnd

    private var currentPage = 1

    init {
        loadNextPage()
        setupExoPlayerListener()
    }

    private fun setupExoPlayerListener() {
        exoPlayer.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
            }
        })
    }

    fun loadNextPage() {
        if (_showLoading.value || _hasReachedEnd.value) return

        _showLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.fetchMusic(currentPage)
                when (result.response) {
                    GetMusicListResponse.SUCCESS -> {
                        if (result.data.isNullOrEmpty()) {
                            _hasReachedEnd.value = true
                        } else {
                            _musicList.value += result.data
                            currentPage++
                        }
                    }
                    GetMusicListResponse.END_OF_CONTENT -> {
                        _hasReachedEnd.value = true
                    }
                    else -> {
                        showError(result.message ?: "An error occurred while fetching music")
                    }
                }
            } catch (e: Exception) {
                showError("An error occurred while fetching music")
            } finally {
                _showLoading.value = false
            }
        }
    }

    fun refresh() {
        _musicList.value = emptyList()
        currentPage = 1
        _hasReachedEnd.value = false
        loadNextPage()
    }

    private fun showError(message: String) {
        _errorMessage.value = message
        _showErrorDialog.value = true
    }

    fun dismissErrorDialog() {
        _errorMessage.value = null
        _showErrorDialog.value = false
    }

    fun playMusic(music: Music) {
        _currentMusic.value = music
        exoPlayer.setMediaItem(MediaItem.fromUri(GetMusicURL(music.id)))
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            exoPlayer.pause()
        } else {
            exoPlayer.play()
        }
    }
}

