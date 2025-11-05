package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tracklift_asa.data.*
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesHistoricoTreinoScreen(
    navController: NavController,
    historicoId: Int,
    treinoViewModel: TreinoViewModel
) {
    var historicoComTreino by remember { mutableStateOf<HistoricTreinoComTreino?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val treinosState = treinoViewModel.treinos.collectAsState()
    val exerciciosState = treinoViewModel.exercicios.collectAsState()

    // Buscar histórico com dados do treino
    LaunchedEffect(historicoId) {
        isLoading = true
        historicoComTreino = treinoViewModel.getHistoricoComTreinoById(historicoId)
        isLoading = false
    }

    // Observar séries realizadas quando o histórico for carregado
    val historicoTreino = historicoComTreino?.let {
        HistoricTreino(
            id = it.id,
            idTreino = it.idTreino,
            idUsuario = it.idUsuario,
            dataRealizacao = it.dataRealizacao,
            duracaoMinutos = it.duracaoMinutos
        )
    }

    val seriesRealizadasFlow = remember(historicoTreino?.id) {
        if (historicoTreino != null) {
            treinoViewModel.getSeriesDoHistoricoTreino(historicoTreino.id)
        } else {
            flowOf(emptyList())
        }
    }

    val seriesRealizadasState = seriesRealizadasFlow.collectAsState(initial = emptyList())

    val exerciciosTreinoFlow = remember(historicoTreino?.idTreino) {
        if (historicoTreino != null) {
            treinoViewModel.getExerciciosDoTreino(historicoTreino.idTreino)
        } else {
            flowOf(emptyList())
        }
    }

    val exerciciosTreinoState = exerciciosTreinoFlow.collectAsState(initial = emptyList())

    val treinoNome = historicoComTreino?.treinoNome
    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
    val dataTexto = historicoTreino?.let { dateFormat.format(java.util.Date(it.dataRealizacao)) } ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(treinoNome ?: "Detalhes do Treino") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFFFA500)
                    )
                }
                historicoTreino == null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Treino não encontrado",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF2D2D2D)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = dataTexto,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (historicoTreino!!.duracaoMinutos > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Duração: ${historicoTreino!!.duracaoMinutos} minutos",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Agrupar séries por exercício
                        exerciciosTreinoState.value.forEach { exercicioTreino ->
                            val exercicio = exerciciosState.value.find { it.id == exercicioTreino.id_exercicio }
                            val seriesDoExercicio = seriesRealizadasState.value.filter { 
                                it.idExercicioTreino == exercicioTreino.id 
                            }.sortedBy { it.numeroSerie }
                            
                            if (seriesDoExercicio.isNotEmpty()) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFF2D2D2D)
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                        ) {
                                            Text(
                                                text = exercicio?.nome ?: "Exercício não encontrado",
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(12.dp))
                                            
                                            seriesDoExercicio.forEach { serie ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Text(
                                                        text = "Série ${serie.numeroSerie}",
                                                        color = Color.Gray,
                                                        fontSize = 14.sp
                                                    )
                                                    Text(
                                                        text = "${serie.peso} kg x ${serie.repeticoes} reps",
                                                        color = Color.White,
                                                        fontSize = 14.sp,
                                                        fontWeight = FontWeight.SemiBold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
