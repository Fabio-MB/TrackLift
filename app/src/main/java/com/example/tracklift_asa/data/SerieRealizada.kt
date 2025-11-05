package com.example.tracklift_asa.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey

@Entity(
    tableName = "series_realizadas",
    foreignKeys = [
        ForeignKey(
            entity = HistoricTreino::class,
            parentColumns = ["id"],
            childColumns = ["id_historico_treino"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ExercicioTreino::class,
            parentColumns = ["id"],
            childColumns = ["id_exercicio_treino"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SerieRealizada(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_historico_treino")
    val idHistoricoTreino: Int,
    @ColumnInfo(name = "id_exercicio_treino")
    val idExercicioTreino: Int,
    @ColumnInfo(name = "numero_serie")
    val numeroSerie: Int,
    @ColumnInfo(name = "peso")
    val peso: Double,
    @ColumnInfo(name = "repeticoes")
    val repeticoes: Int
)
