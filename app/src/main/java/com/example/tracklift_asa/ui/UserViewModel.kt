package com.example.tracklift_asa.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracklift_asa.data.SupabaseAuth
import com.example.tracklift_asa.data.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val auth = SupabaseAuth(application.applicationContext)
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser

    init {
        viewModelScope.launch {
            auth.getCurrentUser()
                .onSuccess { user -> _currentUser.value = user }
        }
    }

    fun register(name: String, email: String, password: String, height: Int, age: Int, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            auth.signUp(name, email, password, height, age)
                .onSuccess { 
                    onResult(true, null)
                }
                .onFailure { exception ->
                    onResult(false, exception.message ?: "Erro desconhecido")
                }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            auth.signIn(email, password)
                .onSuccess { 
                    auth.getCurrentUser()
                        .onSuccess { user ->
                            _currentUser.value = user
                            onResult(true, null)
                        }
                        .onFailure { exception ->
                            onResult(false, exception.message ?: "Erro ao obter usuário")
                        }
                }
                .onFailure { exception ->
                    onResult(false, exception.message ?: "Email ou senha inválidos")
                }
        }
    }

    fun logout(onResult: () -> Unit) {
        viewModelScope.launch {
            auth.signOut()
            _currentUser.value = null
            onResult()
        }
    }
} 