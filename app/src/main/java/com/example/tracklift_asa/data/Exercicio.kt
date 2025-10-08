package com.example.tracklift_asa.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

enum class CategoriaExercicio {
    PEITORAL,
    COSTAS,
    PERNAS,
    BRACO
}

@Entity(tableName = "exercicios")
data class Exercicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "id_usuario") val idUsuario: Int,
    val nome: String,
    val imagem: String,
    val categoria: CategoriaExercicio,
    val descricao: String
) 