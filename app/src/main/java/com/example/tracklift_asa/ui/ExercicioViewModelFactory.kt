package com.example.tracklift_asa.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ExercicioViewModelFactory(
    private val application: Application,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercicioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExercicioViewModel(application, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 