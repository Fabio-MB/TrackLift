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
                    nome = "Smart Fit - Unidade Abolição",
                    localizacao = "Av. da Saudade, 1221 - Pte. Preta, Campinas - SP",
                    tipo = "Academia",
                    preco = 89.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTq-vBwS_tZBMMfA49hpmhDrevwDq2lVwF1wA&s"
                ),
                Academia(
                    nome = "Bluefit - Unidade Taquaral",
                    localizacao = "Av. Dr. Heitor Penteado, 1845 - Parque Taquaral, Campinas - SP",
                    tipo = "Academia",
                    preco = 99.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRP5OjvZ4jbCRkskh4bm3d6zzDLUOKyHiyvGA&s"
                ),
                Academia(
                    nome = "Panobianco - Unidade Barão Geraldo",
                    localizacao = "Av. Albino J. B. de Oliveira, 1600 - Barão Geraldo, Campinas - SP",
                    tipo = "Academia",
                    preco = 79.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_xO5s4vG9e8d4p9Y2X7Z7Y7X2y3bJ5n4xA&s"
                ),
                Academia(
                    nome = "Red Fitness - Unidade Guanabara",
                    localizacao = "R. Mogi Guaçu, 1500 - Jardim Guanabara, Campinas - SP",
                    tipo = "Academia",
                    preco = 119.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQh8F4Y6c3X9jJ9z7kL9vP3sW9t8gN1m2pQw&s"
                ),
                Academia(
                    nome = "Bioritmo - Iguatemi Campinas",
                    localizacao = "Av. Iguatemi, 777 - Vila Brandina, Campinas - SP",
                    tipo = "Academia",
                    preco = 249.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZ-5j9f_9X6kG9l8aB4c3Y2xW7t5n6R9oVg&s"
                ),
                Academia(
                    nome = "Competition - Cambuí",
                    localizacao = "R. Dr. Guilherme da Silva, 432 - Cambuí, Campinas - SP",
                    tipo = "Academia",
                    preco = 320.00,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSy-2Y3z7t6WjF4L1p8s7kG0rD9vX_zW6aYw&s"
                ),
                Academia(
                    nome = "24h Training Gym",
                    localizacao = "R. São Pedro, 31 - Cambuí, Campinas - SP",
                    tipo = "Academia",
                    preco = 109.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6f7j8g0kH5d1sX3wP9a7sB6c4R2Y9xL8pQ&s"
                ),
                Academia(
                    nome = "Hammer Fitness Club",
                    localizacao = "R. Dr. Sampaio Ferraz, 335 - Cambuí, Campinas - SP",
                    tipo = "Academia",
                    preco = 159.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJ-wY7Pz5v8tG4n_3X9cT5sB_6dF1eX0o8w&s"
                ),
                Academia(
                    nome = "Academia Ghimper",
                    localizacao = "Av. José Bonifácio, 2715 - Jardim das Paineiras, Campinas - SP",
                    tipo = "Academia",
                    preco = 99.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRz-8X1v9kK7l5mN0oF_2tJ4aU9b7cO8sR9w&s"
                ),
                Academia(
                    nome = "XPrime - Parque Prado",
                    localizacao = "Av. Washington Luiz, 2480 - Parque Prado, Campinas - SP",
                    tipo = "Academia",
                    preco = 129.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSz5J7H2pG3kF6n4Y8o9T7wR5d_1lJ0xP2oA&s"
                ),
                Academia(
                    nome = "Selfit - Campinas Shopping",
                    localizacao = "R. Jacy Teixeira de Camargo, 940 - Jardim do Lago, Campinas - SP",
                    tipo = "Academia",
                    preco = 79.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-9y7Z8xK6l5pG3tF_1oR7eD4sX9uP2nK0w&s"
                ),
                Academia(
                    nome = "Germano Fitness",
                    localizacao = "R. dos Contabilistas, 77 - Jardim Flamboyant, Campinas - SP",
                    tipo = "Academia",
                    preco = 95.00,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTz6m5n9p4hX1rF8yE7uO6tG1yR2iP5sH7vA&s"
                ),
                Academia(
                    nome = "Academia Just Fit",
                    localizacao = "Av. John Boyd Dunlop, 350 - Jardim Aurélia, Campinas - SP",
                    tipo = "Academia",
                    preco = 69.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-5b_0Kj7pL9X8wZ_3fG2sJ1yO8tX_4nL6Q&s"
                ),
                Academia(
                    nome = "CrossFit Campinas",
                    localizacao = "R. São Salvador, 162 - Jardim do Trevo, Campinas - SP",
                    tipo = "CrossFit",
                    preco = 280.00,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_9w8F4kH6yP5jR7eX2sN0lA9bC1jU7xI9g&s"
                ),
                Academia(
                    nome = "Top Fitness - Jd. Aurélia",
                    localizacao = "Av. Império do Sol Nascente, 631 - Jardim Aurélia, Campinas - SP",
                    tipo = "Academia",
                    preco = 85.00,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTb9H3rJ-1lX7aW0nF_6xG9sC_5uR7iY9p3A&s"
                ),
                Academia(
                    nome = "Academia Pro Corpo",
                    localizacao = "Av. Brasil, 442 - Vila Itapura, Campinas - SP",
                    tipo = "Academia",
                    preco = 90.00,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQp-4jN7yS_6zH5fL_8aW1kR9tU3vX9oG7pQ&s"
                ),
                Academia(
                    nome = "Alpha Fitness",
                    localizacao = "R. Dr. Betim, 535 - Vila Marieta, Campinas - SP",
                    tipo = "Academia",
                    preco = 110.00,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTz-9K4lX6gH7oR_5fT_3jI8mO9pU4vS2aQw&s"
                ),
                Academia(
                    nome = "Bodytech - Galleria Shopping",
                    localizacao = "Rod. Dom Pedro I, s/n - Jardim Nilópolis, Campinas - SP",
                    tipo = "Academia",
                    preco = 350.00,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-7yZ_5xK_1rO8lA_3wS_9eT6vF_4jX1uLg&s"
                ),
                Academia(
                    nome = "Fórmula Academia - Cambuí",
                    localizacao = "R. Comendador Querubim Uriel, 46 - Cambuí, Campinas - SP",
                    tipo = "Academia",
                    preco = 189.90,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZ-5j9f_9X6kG9l8aB4c3Y2xW7t5n6R9oVg&s"
                ),
                Academia(
                    nome = "Academia Inova",
                    localizacao = "Av. Dr. Jesuíno Marcondes Machado, 2030 - Chácara da Barra, Campinas - SP",
                    tipo = "Academia",
                    preco = 130.00,
                    imagem = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRk_9yF_2sX7hL-5wN_8aP_6jU5bC-1lA7aA&s"
                )
            )
            academias.forEach { academia ->
                dao.insert(academia)
            }
        }
    }
}