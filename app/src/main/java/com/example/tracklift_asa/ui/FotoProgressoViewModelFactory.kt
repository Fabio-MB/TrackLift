package com.example.tracklift_asa.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FotoProgressoViewModelFactory(
    private val application: Application,
    private val userId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FotoProgressoViewModel(application, userId) as T
    }
}
