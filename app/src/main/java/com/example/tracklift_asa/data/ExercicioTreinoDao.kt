package com.example.tracklift_asa.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercicioTreinoDao {
    @Query("SELECT * FROM exercicio_treino WHERE id_treino = :treinoId")
    fun getExerciciosByTreinoId(treinoId: Int): Flow<List<ExercicioTreino>>

    @Query("SELECT * FROM exercicio_treino WHERE id_exercicio = :exercicioId")
    fun getTreinosByExercicioId(exercicioId: Int): Flow<List<ExercicioTreino>>

    @Insert
    suspend fun insert(exercicioTreino: ExercicioTreino): Long

    @Update
    suspend fun update(exercicioTreino: ExercicioTreino)

    @Delete
    suspend fun delete(exercicioTreino: ExercicioTreino)

    @Query("DELETE FROM exercicio_treino WHERE id_treino = :treinoId")
    suspend fun deleteAllByTreinoId(treinoId: Int)
} 