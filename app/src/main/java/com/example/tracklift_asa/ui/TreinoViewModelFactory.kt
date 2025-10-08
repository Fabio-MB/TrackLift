package com.example.tracklift_asa.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TreinoViewModelFactory(
    private val application: Application,
    private val userId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TreinoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TreinoViewModel(application, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 