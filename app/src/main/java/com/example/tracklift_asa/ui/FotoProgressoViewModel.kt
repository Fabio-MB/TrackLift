package com.example.tracklift_asa.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracklift_asa.data.FotoProgresso
import com.example.tracklift_asa.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FotoProgressoViewModel(application: Application, private val userId: Int) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).fotoProgressoDao()
    
    val fotos: StateFlow<List<FotoProgresso>> = dao.getByUsuario(userId)
        .onEach { fotos ->
            Log.d("FotoProgressoViewModel", "Fotos carregadas: ${fotos.size} para userId: $userId")
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun inserirFoto(fotoProgresso: FotoProgresso) {
        viewModelScope.launch {
            try {
                dao.insert(fotoProgresso)
                Log.d("FotoProgressoViewModel", "Foto inserida com sucesso: ${fotoProgresso.uriImagem}")
            } catch (e: Exception) {
                Log.e("FotoProgressoViewModel", "Erro ao inserir foto: ${e.message}", e)
            }
        }
    }

    fun excluirFoto(fotoProgresso: FotoProgresso) {
        viewModelScope.launch {
            try {
                dao.delete(fotoProgresso)
                Log.d("FotoProgressoViewModel", "Foto excluída com sucesso: ${fotoProgresso.id}")
            } catch (e: Exception) {
                Log.e("FotoProgressoViewModel", "Erro ao excluir foto: ${e.message}", e)
            }
        }
    }

    fun excluirFotoPorId(id: Int) {
        viewModelScope.launch {
            try {
                dao.deleteById(id)
                Log.d("FotoProgressoViewModel", "Foto excluída por ID: $id")
            } catch (e: Exception) {
                Log.e("FotoProgressoViewModel", "Erro ao excluir foto por ID: ${e.message}", e)
            }
        }
    }
}
