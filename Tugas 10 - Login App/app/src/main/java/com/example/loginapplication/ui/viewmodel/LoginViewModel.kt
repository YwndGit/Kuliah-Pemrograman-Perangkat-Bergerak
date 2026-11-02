package com.example.loginapplication.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.loginapplication.data.repository.UserRepository
import kotlinx.coroutines.launch

enum class LoginResultState {
    IDLE,
    SUCCESS,
    FAILED
}

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isPasswordVisible by mutableStateOf(false)
    var loginResult by mutableStateOf(LoginResultState.IDLE)
    var loginStateMessage by mutableStateOf("")

    init {
        viewModelScope.launch {
            repository.ensureDummyUser()
        }
    }

    fun login() {
        viewModelScope.launch {
            val user = repository.login(username.trim(), password.trim())
            if (user != null) {
                loginResult = LoginResultState.SUCCESS
                loginStateMessage = "Login Berhasil"
            } else {
                loginResult = LoginResultState.FAILED
                loginStateMessage = "Username atau Password Salah"
            }
        }
    }

    fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }
}

class LoginViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
