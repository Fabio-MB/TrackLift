package com.example.tracklift_asa.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromCategoriaExercicio(categoria: CategoriaExercicio): String {
        return categoria.name
    }

    @TypeConverter
    fun toCategoriaExercicio(categoria: String): CategoriaExercicio {
        return CategoriaExercicio.valueOf(categoria)
    }

    @TypeConverter
    fun fromCategoriaTreino(categoria: CategoriaTreino): String {
        return categoria.name
    }

    @TypeConverter
    fun toCategoriaTreino(categoria: String): CategoriaTreino {
        return CategoriaTreino.valueOf(categoria)
    }
} 