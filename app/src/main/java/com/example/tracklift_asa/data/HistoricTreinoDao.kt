package com.example.tracklift_asa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoricTreinoDao {
    @Query("SELECT * FROM historico_treinos WHERE id_usuario = :idUsuario ORDER BY data_realizacao DESC")
    fun getByUsuario(idUsuario: Int): Flow<List<HistoricTreino>>

    @Query("SELECT * FROM historico_treinos WHERE id = :id")
    suspend fun getById(id: Int): HistoricTreino?

    @Query("SELECT * FROM historico_treinos WHERE id_treino = :idTreino ORDER BY data_realizacao DESC")
    fun getByTreino(idTreino: Int): Flow<List<HistoricTreino>>

    @Insert
    suspend fun insert(historicTreino: HistoricTreino): Long

    @Delete
    suspend fun delete(historicTreino: HistoricTreino)
}
