package dev.yuanzix.beatify.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yuanzix.beatify.data.DataStoreRepository
import dev.yuanzix.beatify.data.NetworkResult
import dev.yuanzix.beatify.data.auth_repository.AuthRepository
import dev.yuanzix.beatify.data.auth_repository.utils.CreateUserResponse
import dev.yuanzix.beatify.data.auth_repository.utils.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    var showErrorDialog by mutableStateOf(false)
        private set
    var showLoadingDialog by mutableStateOf(false)
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var username by mutableStateOf("")
        private set

    var firstName by mutableStateOf("")
        private set

    var lastName by mutableStateOf("")
        private set

    var dateOfBirth by mutableStateOf("")
        private set

    var showDatePicker by mutableStateOf(false)
        private set

    private val _loginResult = MutableStateFlow<NetworkResult<LoginResponse, String>?>(null)
    val loginResult: StateFlow<NetworkResult<LoginResponse, String>?> = _loginResult

    private val _signupResult = MutableStateFlow<NetworkResult<CreateUserResponse, Nothing>?>(null)
    val signupResult: StateFlow<NetworkResult<CreateUserResponse, Nothing>?> = _signupResult

    fun updateField(field: String, value: String) {
        when (field) {
            "email" -> email = value
            "password" -> password = value
            "username" -> username = value
            "firstName" -> firstName = value
            "lastName" -> lastName = value
            "dateOfBirth" -> dateOfBirth = value
        }
    }

    fun toggleDatePicker() {
        showDatePicker = !showDatePicker
    }

    fun signUp() {
        showLoadingDialog = true
        viewModelScope.launch {
            try {
                val result = repository.createUser(
                    email,
                    username,
                    firstName,
                    lastName,
                    password,
                    dateOfBirth
                )
                _signupResult.value = result
                if (result.error != CreateUserResponse.SUCCESS) {
                    showError(result.message ?: "An error occurred during sign up")
                }
            } catch (e: Exception) {
                showError("An error occurred during login")
            } finally {
                showLoadingDialog = false
            }
        }
    }

    fun login() {
        showLoadingDialog = true
        viewModelScope.launch {
            try {
                val result = repository.loginUser(email, password)
                _loginResult.value = result
                if (result.error == LoginResponse.SUCCESS) {
                    dataStoreRepository.updateUserSettings(
                        email = email,
                        jwtTokenString = result.data
                    )
                }
                if (result.error != LoginResponse.SUCCESS && result.error != LoginResponse.NOT_VERIFIED) {
                    showError(result.message ?: "An error occurred during login")
                }
            } catch (e: Exception) {
                showError("An error occurred during login")
            } finally {
                showLoadingDialog = false
            }
        }
    }

    private fun showError(message: String) {
        _errorMessage.value = message
    }

    fun dismissErrorDialog() {
        _errorMessage.value = null
    }

    fun toggleLoadingDialog() {
        showLoadingDialog = !showLoadingDialog
    }
}
