package com.example.tracklift_asa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface SerieRealizadaDao {
    @Query("SELECT * FROM series_realizadas WHERE id_historico_treino = :idHistoricoTreino ORDER BY id_exercicio_treino, numero_serie")
    fun getByHistoricoTreino(idHistoricoTreino: Int): Flow<List<SerieRealizada>>

    @Query("SELECT * FROM series_realizadas WHERE id_exercicio_treino = :idExercicioTreino AND id_historico_treino = :idHistoricoTreino ORDER BY numero_serie")
    suspend fun getByExercicioTreino(idExercicioTreino: Int, idHistoricoTreino: Int): List<SerieRealizada>

    @Insert
    suspend fun insert(serieRealizada: SerieRealizada): Long

    @Insert
    suspend fun insertAll(series: List<SerieRealizada>)

    @Delete
    suspend fun delete(serieRealizada: SerieRealizada)

    @Query("DELETE FROM series_realizadas WHERE id_historico_treino = :idHistoricoTreino")
    suspend fun deleteByHistoricoTreino(idHistoricoTreino: Int)
}
