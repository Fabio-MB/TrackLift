package com.example.tracklift_asa.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface AcademiaDao {
    @Query("SELECT * FROM academias")
    fun getAll(): Flow<List<Academia>>

    @Insert
    suspend fun insert(academia: Academia)

    @Delete
    suspend fun delete(academia: Academia)

    @Query("DELETE FROM academias")
    suspend fun deleteAll()
} 