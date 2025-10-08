package com.example.tracklift_asa.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "academias")
data class Academia(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val localizacao: String,
    val tipo: String,
    val preco: Double,
    val imagem: String = ""
) 