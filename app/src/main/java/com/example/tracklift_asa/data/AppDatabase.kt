package com.example.tracklift_asa.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Academia::class,
        Exercicio::class,
        Treino::class,
        ExercicioTreino::class,
        User::class
    ],
    version = 7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun academiaDao(): AcademiaDao
    abstract fun exercicioDao(): ExercicioDao
    abstract fun treinoDao(): TreinoDao
    abstract fun exercicioTreinoDao(): ExercicioTreinoDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tracklift_database"
                )
                .addCallback(AppDatabaseCallback(context))
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val context: Context
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    val database = getDatabase(context)
                    populateDatabase(database.academiaDao(), database.exercicioDao())
                }
            }
        }

        suspend fun populateDatabase(academiaDao: AcademiaDao, exercicioDao: ExercicioDao) {
            // Limpar dados antigos
            academiaDao.deleteAll()
            exercicioDao.deleteAll()

            // Adicionar exercícios
            exercicioDao.insert(Exercicio(nome = "Supino Reto", categoria = CategoriaExercicio.PEITORAL, idUsuario = 0, descricao = "Exercício para peitoral", imagem = "https://i.imgur.com/gallery/a4hYk2J.png"))
            exercicioDao.insert(Exercicio(nome = "Agachamento Livre", categoria = CategoriaExercicio.PERNAS, idUsuario = 0, descricao = "Exercício para pernas", imagem = "https://i.imgur.com/gallery/a4hYk2J.png"))
            exercicioDao.insert(Exercicio(nome = "Puxada Frontal", categoria = CategoriaExercicio.COSTAS, idUsuario = 0, descricao = "Exercício para costas", imagem = "https://i.imgur.com/gallery/a4hYk2J.png"))
        }
    }
} 