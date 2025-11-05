package com.example.tracklift_asa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface FotoProgressoDao {
    @Query("SELECT * FROM fotos_progresso WHERE id_usuario = :idUsuario ORDER BY data DESC")
    fun getByUsuario(idUsuario: Int): Flow<List<FotoProgresso>>

    @Query("SELECT * FROM fotos_progresso WHERE id = :id")
    suspend fun getById(id: Int): FotoProgresso?

    @Insert
    suspend fun insert(fotoProgresso: FotoProgresso): Long

    @Delete
    suspend fun delete(fotoProgresso: FotoProgresso)

    @Query("DELETE FROM fotos_progresso WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM fotos_progresso WHERE id_usuario = :idUsuario")
    suspend fun deleteAllByUsuario(idUsuario: Int)
}
