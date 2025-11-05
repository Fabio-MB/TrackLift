package com.example.tracklift_asa.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

@Entity(tableName = "fotos_progresso")
data class FotoProgresso(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int,
    @ColumnInfo(name = "uri_imagem")
    val uriImagem: String,
    @ColumnInfo(name = "data")
    val data: Long,
    @ColumnInfo(name = "observacao")
    val observacao: String = ""
)
