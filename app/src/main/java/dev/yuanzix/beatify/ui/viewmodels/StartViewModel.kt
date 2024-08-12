package dev.yuanzix.beatify.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yuanzix.beatify.data.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {
    val userSettingsFlow = dataStoreRepository.userSettingsFlow
}