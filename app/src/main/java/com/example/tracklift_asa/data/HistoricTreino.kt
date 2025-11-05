package com.example.tracklift_asa.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "historico_treinos")
data class HistoricTreino(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_treino")
    val idTreino: Int,
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int,
    @ColumnInfo(name = "data_realizacao")
    val dataRealizacao: Long,
    @ColumnInfo(name = "duracao_minutos")
    val duracaoMinutos: Int = 0
)

data class HistoricTreinoComTreino(
    val id: Int,
    val idTreino: Int,
    val idUsuario: Int,
    val dataRealizacao: Long,
    val duracaoMinutos: Int,
    val treinoNome: String?,
    val treinoCategoria: CategoriaTreino?
)
