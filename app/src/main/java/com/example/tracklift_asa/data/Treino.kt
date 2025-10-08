package com.example.tracklift_asa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "treino")
data class Treino(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val categoria: CategoriaTreino,
    val userId: Int
) 