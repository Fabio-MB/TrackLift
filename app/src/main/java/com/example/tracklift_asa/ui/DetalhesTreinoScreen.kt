package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tracklift_asa.data.ExercicioTreino
import com.example.tracklift_asa.data.SerieRealizada
import com.example.tracklift_asa.data.Treino
import kotlinx.coroutines.launch

data class SerieInput(
    val idExercicioTreino: Int,
    val numeroSerie: Int,
    var peso: String = "",
    var repeticoes: String = "",
    var concluida: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesTreinoScreen(
    navController: NavController,
    treinoId: Int,
    treinoViewModel: TreinoViewModel
) {
    val scope = rememberCoroutineScope()
    val treinosState = treinoViewModel.treinos.collectAsState()
    val treino = remember(treinosState.value, treinoId) {
        treinosState.value.find { it.id == treinoId }
    }

    var exerciciosTreino by remember { mutableStateOf<List<ExercicioTreino>>(emptyList()) }
    val exerciciosState = treinoViewModel.exercicios.collectAsState()
    var seriesInput by remember { mutableStateOf<List<SerieInput>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showFinalizarDialog by remember { mutableStateOf(false) }
    val inicioTreino = remember { System.currentTimeMillis() }

    // Inicializar séries quando os exercícios forem carregados
    LaunchedEffect(exerciciosTreino) {
        if (exerciciosTreino.isNotEmpty() && seriesInput.isEmpty()) {
            val novasSeries = mutableListOf<SerieInput>()
            exerciciosTreino.forEach { exercicioTreino ->
                for (i in 1..exercicioTreino.series) {
                    novasSeries.add(
                        SerieInput(
                            idExercicioTreino = exercicioTreino.id,
                            numeroSerie = i,
                            repeticoes = exercicioTreino.repeticoes.toString()
                        )
                    )
                }
            }
            seriesInput = novasSeries
        }
    }

    LaunchedEffect(treino?.id) {
        treino?.id?.let {
            try {
                isLoading = true
                error = null
                treinoViewModel.getExerciciosDoTreino(it).collect { exercicios ->
                    exerciciosTreino = exercicios
                    isLoading = false
                }
            } catch (e: Exception) {
                error = "Erro ao carregar exercícios: ${e.message}"
                isLoading = false
            }
        }
    }

    fun finalizarTreino() {
        scope.launch {
            val duracaoMinutos = ((System.currentTimeMillis() - inicioTreino) / 60000).toInt()
            val seriesRealizadas = seriesInput
                .filter { it.concluida && it.peso.isNotBlank() && it.repeticoes.isNotBlank() }
                .map { serie ->
                    SerieRealizada(
                        idHistoricoTreino = 0, // Será preenchido no ViewModel
                        idExercicioTreino = serie.idExercicioTreino,
                        numeroSerie = serie.numeroSerie,
                        peso = serie.peso.toDoubleOrNull() ?: 0.0,
                        repeticoes = serie.repeticoes.toIntOrNull() ?: 0
                    )
                }
            
            if (seriesRealizadas.isNotEmpty() && treino != null) {
                try {
                    treinoViewModel.salvarTreinoRealizado(treino.id, seriesRealizadas, duracaoMinutos)
                    navController.popBackStack()
                } catch (e: Exception) {
                    error = "Erro ao salvar treino: ${e.message}"
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(treino?.nome ?: "Carregando...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (seriesInput.any { it.concluida }) {
                        TextButton(
                            onClick = { showFinalizarDialog = true }
                        ) {
                            Text("Finalizar", color = Color(0xFFFFA500))
                        }
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
            if (treino == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Treino não encontrado.", color = Color.White, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Voltar")
                        }
                    }
                }
            } else {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color(0xFFFFA500)
                        )
                    }
                    error != null -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = error ?: "",
                                color = Color.Red,
                                fontSize = 16.sp
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
                                            text = "Categoria: ${treino.categoria.name}",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }

                            if (exerciciosTreino.isEmpty()) {
                                item {
                                    Text(
                                        text = "Nenhum exercício adicionado a este treino",
                                        color = Color.Gray,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            } else {
                                // Agrupar séries por exercício
                                exerciciosTreino.forEach { exercicioTreino ->
                                    val exercicio = exerciciosState.value.find { it.id == exercicioTreino.id_exercicio }
                                    val seriesDoExercicio = seriesInput.filter { it.idExercicioTreino == exercicioTreino.id }
                                    
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
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "Meta: ${exercicioTreino.series} séries x ${exercicioTreino.repeticoes} repetições",
                                                    color = Color.Gray,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    text = "Descanso: ${exercicioTreino.descanso} min",
                                                    color = Color.Gray,
                                                    fontSize = 14.sp
                                                )
                                                Spacer(modifier = Modifier.height(12.dp))
                                                
                                                // Séries individuais
                                                seriesDoExercicio.forEach { serie ->
                                                    SerieRow(
                                                        serie = serie,
                                                        numeroSerie = serie.numeroSerie,
                                                        repeticoesMeta = exercicioTreino.repeticoes,
                                                        onUpdate = { updated ->
                                                            seriesInput = seriesInput.map { 
                                                                if (it.idExercicioTreino == serie.idExercicioTreino && 
                                                                    it.numeroSerie == serie.numeroSerie) {
                                                                    updated
                                                                } else {
                                                                    it
                                                                }
                                                            }
                                                        }
                                                    )
                                                    if (serie.numeroSerie < exercicioTreino.series) {
                                                        Spacer(modifier = Modifier.height(8.dp))
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

    // Dialog de confirmação para finalizar treino
    if (showFinalizarDialog) {
        AlertDialog(
            onDismissRequest = { showFinalizarDialog = false },
            title = { Text("Finalizar Treino") },
            text = {
                val seriesConcluidas = seriesInput.count { it.concluida }
                val totalSeries = seriesInput.size
                Text("Você completou $seriesConcluidas de $totalSeries séries.\n\nDeseja salvar este treino no histórico?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        finalizarTreino()
                        showFinalizarDialog = false
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showFinalizarDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun SerieRow(
    serie: SerieInput,
    numeroSerie: Int,
    repeticoesMeta: Int,
    onUpdate: (SerieInput) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (serie.concluida) Color(0xFF1A4D1A) else Color(0xFF3D3D3D)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Série $numeroSerie",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(0.3f)
            )
            
            OutlinedTextField(
                value = serie.peso,
                onValueChange = { 
                    onUpdate(serie.copy(peso = it))
                },
                label = { Text("Peso (kg)", fontSize = 12.sp) },
                modifier = Modifier
                    .weight(0.35f)
                    .height(56.dp),
                singleLine = true,
                enabled = !serie.concluida,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            OutlinedTextField(
                value = serie.repeticoes,
                onValueChange = { 
                    onUpdate(serie.copy(repeticoes = it))
                },
                label = { Text("Repetições", fontSize = 12.sp) },
                modifier = Modifier
                    .weight(0.35f)
                    .height(56.dp),
                singleLine = true,
                enabled = !serie.concluida,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            
            IconButton(
                onClick = {
                    if (!serie.concluida && serie.peso.isNotBlank() && serie.repeticoes.isNotBlank()) {
                        onUpdate(serie.copy(concluida = true))
                    }
                },
                enabled = !serie.concluida && serie.peso.isNotBlank() && serie.repeticoes.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Concluir série",
                    tint = if (serie.concluida) Color(0xFF4CAF50) 
                           else if (serie.peso.isNotBlank() && serie.repeticoes.isNotBlank()) Color(0xFFFFA500)
                           else Color.Gray
                )
            }
        }
    }
} 