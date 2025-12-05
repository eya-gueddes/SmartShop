package com.eya.smartshop.auth

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val user: FirebaseUser? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)

class AuthViewModel (private val repo: AuthRepository) : ViewModel() {
    var uiState by mutableStateOf(AuthUiState())
        private set

    fun onEmailChange(newEmail: String) {
        uiState = uiState.copy(email = newEmail)
    }

    fun onPasswordChange(newPassword: String) {
        uiState = uiState.copy(password = newPassword)
    }

    fun signIn() = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true)
        try {
            repo.signIn(uiState.email, uiState.password)
            uiState = uiState.copy(user = repo.currentUser(), error = null, isLoading = false)
        } catch (e: Exception) {
            uiState = uiState.copy(error = e.message, isLoading = false)
        }
    }

    fun signUp() = viewModelScope.launch {
        uiState = uiState.copy(isLoading = true)
        try {
            repo.signUp(uiState.email, uiState.password)
            uiState = uiState.copy(user = repo.currentUser(), error = null, isLoading = false)
        } catch (e: Exception) {
            uiState = uiState.copy(error = e.message, isLoading = false)
        }
    }

    fun signOut() {
        repo.signOut()
        uiState = uiState.copy(user = null)
    }
}