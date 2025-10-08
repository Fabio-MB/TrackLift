package com.example.tracklift_asa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface ExercicioDao {
    @Query("SELECT * FROM exercicios WHERE id_usuario = :idUsuario")
    fun getByUsuario(idUsuario: Int): Flow<List<Exercicio>>

    @Query("SELECT * FROM exercicios WHERE categoria = :categoria")
    fun getByCategoria(categoria: CategoriaExercicio): Flow<List<Exercicio>>

    @Insert
    suspend fun insert(exercicio: Exercicio)

    @Delete
    suspend fun delete(exercicio: Exercicio)

    @Query("DELETE FROM exercicios")
    suspend fun deleteAll()
} 