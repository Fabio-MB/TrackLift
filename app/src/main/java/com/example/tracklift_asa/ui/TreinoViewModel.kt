package com.example.tracklift_asa.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracklift_asa.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TreinoViewModel(application: Application, private val userId: Int) : AndroidViewModel(application) {
    private val treinoDao = AppDatabase.getDatabase(application).treinoDao()
    private val exercicioTreinoDao = AppDatabase.getDatabase(application).exercicioTreinoDao()
    private val exercicioDao = AppDatabase.getDatabase(application).exercicioDao()
    private val historicTreinoDao = AppDatabase.getDatabase(application).historicTreinoDao()
    private val serieRealizadaDao = AppDatabase.getDatabase(application).serieRealizadaDao()

    init {
        Log.d("TreinoViewModel", "Inicializando TreinoViewModel para o userId: $userId")
    }

    val treinos: StateFlow<List<Treino>> = treinoDao.getTreinosByUserId(userId)
        .onEach { treinos ->
            Log.d("TreinoViewModel", "Treinos carregados: ${treinos.size} para o userId: $userId")
            treinos.forEach { treino ->
                Log.d("TreinoViewModel", "Treino: id=${treino.id}, nome=${treino.nome}, categoria=${treino.categoria}")
            }
        }
        .catch { e ->
            Log.e("TreinoViewModel", "Erro ao carregar treinos: ${e.message}", e)
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val exercicios: StateFlow<List<Exercicio>> = exercicioDao.getByUsuario(userId)
        .onEach { exercicios ->
            Log.d("TreinoViewModel", "Exercícios carregados: ${exercicios.size}")
        }
        .catch { e ->
            Log.e("TreinoViewModel", "Erro ao carregar exercícios: ${e.message}", e)
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun inserirTreino(treino: Treino) {
        viewModelScope.launch {
            try {
                Log.d("TreinoViewModel", "Inserindo treino: ${treino.nome}")
                val id = treinoDao.insert(treino)
                Log.d("TreinoViewModel", "Treino inserido com sucesso, ID: $id")
            } catch (e: Exception) {
                Log.e("TreinoViewModel", "Erro ao inserir treino: ${e.message}", e)
                throw e
            }
        }
    }

    fun atualizarTreino(treino: Treino) {
        viewModelScope.launch {
            try {
                treinoDao.update(treino)
            } catch (e: Exception) {
                Log.e("TreinoViewModel", "Erro ao atualizar treino: ${e.message}", e)
                throw e
            }
        }
    }

    fun excluirTreino(treino: Treino) {
        viewModelScope.launch {
            try {
                exercicioTreinoDao.deleteAllByTreinoId(treino.id)
                treinoDao.delete(treino)
            } catch (e: Exception) {
                Log.e("TreinoViewModel", "Erro ao excluir treino: ${e.message}", e)
                throw e
            }
        }
    }

    fun adicionarExercicioAoTreino(
        treinoId: Int,
        exercicioId: Int,
        series: Int,
        repeticoes: Int,
        descanso: Double
    ) {
        viewModelScope.launch {
            try {
                val exercicioTreino = ExercicioTreino(
                    id_exercicio = exercicioId,
                    id_treino = treinoId,
                    series = series,
                    repeticoes = repeticoes,
                    descanso = descanso
                )
                exercicioTreinoDao.insert(exercicioTreino)
            } catch (e: Exception) {
                Log.e("TreinoViewModel", "Erro ao adicionar exercício ao treino: ${e.message}", e)
                throw e
            }
        }
    }

    fun removerExercicioDoTreino(exercicioTreino: ExercicioTreino) {
        viewModelScope.launch {
            try {
                exercicioTreinoDao.delete(exercicioTreino)
            } catch (e: Exception) {
                Log.e("TreinoViewModel", "Erro ao remover exercício do treino: ${e.message}", e)
                throw e
            }
        }
    }

    fun getExerciciosDoTreino(treinoId: Int): Flow<List<ExercicioTreino>> {
        return try {
            Log.d("TreinoViewModel", "Buscando exercícios do treino $treinoId")
            exercicioTreinoDao.getExerciciosByTreinoId(treinoId)
                .onEach { exercicios ->
                    Log.d("TreinoViewModel", "Exercícios encontrados: ${exercicios.size}")
                }
                .catch { e ->
                    Log.e("TreinoViewModel", "Erro ao buscar exercícios do treino: ${e.message}", e)
                    emit(emptyList())
                }
        } catch (e: Exception) {
            Log.e("TreinoViewModel", "Erro ao iniciar busca de exercícios: ${e.message}", e)
            flow { emit(emptyList()) }
        }
    }

    fun getTreinosDoExercicio(exercicioId: Int): Flow<List<ExercicioTreino>> {
        return try {
            exercicioTreinoDao.getTreinosByExercicioId(exercicioId)
                .catch { e ->
                    Log.e("TreinoViewModel", "Erro ao buscar treinos do exercício: ${e.message}", e)
                    emit(emptyList())
                }
        } catch (e: Exception) {
            Log.e("TreinoViewModel", "Erro ao iniciar busca de treinos: ${e.message}", e)
            flow { emit(emptyList()) }
        }
    }

    val historicoTreinos: StateFlow<List<HistoricTreino>> = historicTreinoDao.getByUsuario(userId)
        .onEach { historicos ->
            Log.d("TreinoViewModel", "Histórico carregado: ${historicos.size} treinos")
        }
        .catch { e ->
            Log.e("TreinoViewModel", "Erro ao carregar histórico: ${e.message}", e)
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val historicoTreinosComTreino: StateFlow<List<HistoricTreinoComTreino>> = combine(
        historicTreinoDao.getByUsuario(userId),
        treinos
    ) { historicos, treinosList ->
        historicos.map { historico ->
            val treino = treinosList.find { it.id == historico.idTreino }
            HistoricTreinoComTreino(
                id = historico.id,
                idTreino = historico.idTreino,
                idUsuario = historico.idUsuario,
                dataRealizacao = historico.dataRealizacao,
                duracaoMinutos = historico.duracaoMinutos,
                treinoNome = treino?.nome,
                treinoCategoria = treino?.categoria
            )
        }
    }
        .catch { e ->
            Log.e("TreinoViewModel", "Erro ao carregar histórico com treinos: ${e.message}", e)
            emit(emptyList())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun salvarTreinoRealizado(
        idTreino: Int,
        seriesRealizadas: List<SerieRealizada>,
        duracaoMinutos: Int = 0
    ) {
        try {
            val historicTreino = HistoricTreino(
                idTreino = idTreino,
                idUsuario = userId,
                dataRealizacao = System.currentTimeMillis(),
                duracaoMinutos = duracaoMinutos
            )
            val idHistorico = historicTreinoDao.insert(historicTreino)
            
            // Salvar séries realizadas
            val seriesComHistorico = seriesRealizadas.map { serie ->
                serie.copy(idHistoricoTreino = idHistorico.toInt())
            }
            serieRealizadaDao.insertAll(seriesComHistorico)
            
            Log.d("TreinoViewModel", "Treino realizado salvo: $idHistorico com ${seriesComHistorico.size} séries")
        } catch (e: Exception) {
            Log.e("TreinoViewModel", "Erro ao salvar treino realizado: ${e.message}", e)
            throw e
        }
    }

    fun getSeriesDoHistoricoTreino(idHistoricoTreino: Int): Flow<List<SerieRealizada>> {
        return serieRealizadaDao.getByHistoricoTreino(idHistoricoTreino)
            .catch { e ->
                Log.e("TreinoViewModel", "Erro ao buscar séries: ${e.message}", e)
                emit(emptyList())
            }
    }

    fun getHistoricoByTreino(idTreino: Int): Flow<List<HistoricTreino>> {
        return historicTreinoDao.getByTreino(idTreino)
            .catch { e ->
                Log.e("TreinoViewModel", "Erro ao buscar histórico do treino: ${e.message}", e)
                emit(emptyList())
            }
    }

    suspend fun getHistoricoComTreinoById(historicoId: Int): HistoricTreinoComTreino? {
        val historico = historicTreinoDao.getById(historicoId) ?: return null
        val treino = treinos.value.find { it.id == historico.idTreino }
        return HistoricTreinoComTreino(
            id = historico.id,
            idTreino = historico.idTreino,
            idUsuario = historico.idUsuario,
            dataRealizacao = historico.dataRealizacao,
            duracaoMinutos = historico.duracaoMinutos,
            treinoNome = treino?.nome,
            treinoCategoria = treino?.categoria
        )
    }
} 