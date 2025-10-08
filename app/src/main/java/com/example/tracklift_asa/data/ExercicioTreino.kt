package com.example.tracklift_asa.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "exercicio_treino",
    foreignKeys = [
        ForeignKey(
            entity = Exercicio::class,
            parentColumns = ["id"],
            childColumns = ["id_exercicio"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Treino::class,
            parentColumns = ["id"],
            childColumns = ["id_treino"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExercicioTreino(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val id_exercicio: Int,
    val id_treino: Int,
    val series: Int,
    val repeticoes: Int,
    val descanso: Double
) 