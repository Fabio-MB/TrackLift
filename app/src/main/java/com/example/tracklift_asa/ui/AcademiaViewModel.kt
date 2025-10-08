package com.example.tracklift_asa.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracklift_asa.data.Academia
import com.example.tracklift_asa.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.content.Context

class AcademiaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).academiaDao()
    val academias: StateFlow<List<Academia>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        verificarPrimeiraExecucao(application)
    }

    private fun verificarPrimeiraExecucao(application: Application) {
        val sharedPreferences = application.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val jaExecutou = sharedPreferences.getBoolean("ja_executou_academias", false)

        if (!jaExecutou) {
            popularAcademias()
            sharedPreferences.edit().putBoolean("ja_executou_academias", true).apply()
        }
    }

    fun inserirAcademia(academia: Academia) {
        viewModelScope.launch {
            dao.insert(academia)
        }
    }

    fun excluirAcademia(academia: Academia) {
        viewModelScope.launch {
            dao.delete(academia)
        }
    }

    fun popularAcademias() {
        viewModelScope.launch {
            val academias = listOf(
                Academia(
                    nome = "Academia Blue Fit",
                    localizacao = "Rua das Flores, 123",
                    tipo = "Academia",
                    preco = 99.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRP5OjvZ4jbCRkskh4bm3d6zzDLUOKyHiyvGA&s"
                ),
                Academia(
                    nome = "Gym Center",
                    localizacao = "Av. Principal, 456",
                    tipo = "Academia",
                    preco = 89.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRK_Upj905l7R9L57PLtiiK9T3cG_FXhqpQdQ&s"
                ),
                Academia(
                    nome = "Fit Club",
                    localizacao = "Rua dos Esportes, 789",
                    tipo = "Academia",
                    preco = 79.90,
                    imagem = "https://www.academiafitclub.esp.br/img/principal/slide/fitclub-slide-logo.png"
                ),
                Academia(
                    nome = "Smart Fit",
                    localizacao = "Av. dos Atletas, 321",
                    tipo = "Academia",
                    preco = 69.90,
                    imagem = "https://braziljournal.com/wp-content/uploads/2023/03/smarfit-scaled.jpg"
                ),             
            )
            academias.forEach { academia ->
                dao.insert(academia)
            }
        }
    }
} 