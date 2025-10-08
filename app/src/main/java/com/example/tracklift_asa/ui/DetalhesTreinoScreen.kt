package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.tracklift_asa.data.ExercicioTreino
import com.example.tracklift_asa.data.Treino
import kotlinx.coroutines.launch

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
    var exerciciosConcluidos by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(treino?.nome ?: "Carregando...") },
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
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    scope.launch {
                                        try {
                                            isLoading = true
                                            error = null
                                            treinoViewModel.getExerciciosDoTreino(treino.id).collect { exercicios ->
                                                exerciciosTreino = exercicios
                                                isLoading = false
                                            }
                                        } catch (e: Exception) {
                                            error = "Erro ao carregar exercícios: ${e.message}"
                                            isLoading = false
                                        }
                                    }
                                }
                            ) {
                                Text("Tentar Novamente")
                            }
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF2D2D2D)
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = "Categoria: ${treino.categoria.name}",
                                            color = Color.Gray,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Exercícios",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
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
                                items(exerciciosTreino) { exercicioTreino ->
                                    val exercicio = exerciciosState.value.find { it.id == exercicioTreino.id_exercicio }
                                    if (exercicio != null) {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFF2D2D2D)
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = exercicio.nome,
                                                        color = Color.White,
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Spacer(modifier = Modifier.height(4.dp))
                                                    Text(
                                                        text = "${exercicioTreino.series} séries x ${exercicioTreino.repeticoes} repetições",
                                                        color = Color.Gray,
                                                        fontSize = 14.sp
                                                    )
                                                    Text(
                                                        text = "Descanso: ${exercicioTreino.descanso} minutos",
                                                        color = Color.Gray,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                                Checkbox(
                                                    checked = exerciciosConcluidos.contains(exercicioTreino.id),
                                                    onCheckedChange = { checked ->
                                                        exerciciosConcluidos = if (checked) {
                                                            exerciciosConcluidos + exercicioTreino.id
                                                        } else {
                                                            exerciciosConcluidos - exercicioTreino.id
                                                        }
                                                    },
                                                    colors = CheckboxDefaults.colors(
                                                        checkedColor = Color(0xFFFFA500),
                                                        uncheckedColor = Color.Gray
                                                    )
                                                )
                                            }
                                        }
                                    } else {
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFF2D2D2D)
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Exercício não encontrado (ID: ${exercicioTreino.id_exercicio})",
                                                    color = Color.Red,
                                                    fontSize = 16.sp
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