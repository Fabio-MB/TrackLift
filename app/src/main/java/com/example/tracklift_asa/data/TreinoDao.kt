package com.example.tracklift_asa.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TreinoDao {
    @Query("SELECT * FROM treino WHERE userId = :userId")
    fun getTreinosByUserId(userId: Int): Flow<List<Treino>>

    @Query("SELECT * FROM treino WHERE id = :id")
    suspend fun getById(id: Int): Treino?

    @Insert
    suspend fun insert(treino: Treino): Long

    @Update
    suspend fun update(treino: Treino)

    @Delete
    suspend fun delete(treino: Treino)
} 