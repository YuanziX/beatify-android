package dev.yuanzix.beatify.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.yuanzix.beatify.data.NetworkResult
import dev.yuanzix.beatify.data.loginSignupRepository.AuthRepository
import dev.yuanzix.beatify.data.loginSignupRepository.utils.CreateUserError
import dev.yuanzix.beatify.data.loginSignupRepository.utils.LoginError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    var email: String by mutableStateOf("")
        private set

    var password: String by mutableStateOf("")
        private set

    var username: String by mutableStateOf("")
        private set

    var firstName: String by mutableStateOf("")
        private set

    var lastName: String by mutableStateOf("")
        private set

    var dateOfBirth: String by mutableStateOf("")
        private set

    var showDatePicker by mutableStateOf(false)
        private set

    private val _loginResult = MutableStateFlow<NetworkResult<LoginError, String>?>(null)
    val loginResult: StateFlow<NetworkResult<LoginError, String>?> = _loginResult

    private val _signupResult = MutableStateFlow<NetworkResult<CreateUserError, Nothing>?>(null)
    val signupResult: StateFlow<NetworkResult<CreateUserError, Nothing>?> = _signupResult

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updateUsername(username: String) {
        this.username = username
    }

    fun updateFirstName(firstName: String) {
        this.firstName = firstName
    }

    fun updateLastName(lastName: String) {
        this.lastName = lastName
    }

    fun updateDateOfBirth(dateOfBirth: String) {
        this.dateOfBirth = dateOfBirth
    }

    fun toggleDatePicker() {
        showDatePicker = !showDatePicker
    }

    fun signUp() {
        viewModelScope.launch {
            val result = repository.createUser(
                email,
                username,
                firstName,
                lastName,
                password,
                dateOfBirth
            )
            _signupResult.value = result
        }
    }

    fun login() {
        viewModelScope.launch {
            val result = repository.loginUser(email, password)
            _loginResult.value = result
        }
    }
}
