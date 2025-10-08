package com.example.tracklift_asa.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tracklift_asa.data.ExercicioTreino
import com.example.tracklift_asa.data.Treino
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrudExercicioTreinoScreen(
    navController: NavController,
    treinoId: Int,
    treinoViewModel: TreinoViewModel
) {
    val scope = rememberCoroutineScope()
    var exerciciosTreino by remember { mutableStateOf<List<ExercicioTreino>>(emptyList()) }
    var exercicioSelecionado by remember { mutableStateOf<Int?>(null) }
    var series by remember { mutableStateOf("") }
    var repeticoes by remember { mutableStateOf("") }
    var descanso by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val exerciciosState = treinoViewModel.exercicios.collectAsState()

    LaunchedEffect(treinoId) {
        treinoViewModel.getExerciciosDoTreino(treinoId).collect { exercicios ->
            exerciciosTreino = exercicios
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerenciar Exercícios do Treino") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Formulário para adicionar exercício
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Adicionar Exercício",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Campo para selecionar exercício
                    var showExercicioDropdown by remember { mutableStateOf(false) }
                    val exercicios = exerciciosState.value
                    
                    Box {
                        OutlinedTextField(
                            value = exercicioSelecionado?.let { id ->
                                exercicios.find { it.id == id }?.nome ?: ""
                            } ?: "",
                            onValueChange = { },
                            label = { Text("Exercício") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showExercicioDropdown = true }) {
                                    Icon(Icons.Default.ArrowDropDown, "Selecionar exercício")
                                }
                            }
                        )
                        
                        DropdownMenu(
                            expanded = showExercicioDropdown,
                            onDismissRequest = { showExercicioDropdown = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            exercicios.forEach { exercicio ->
                                DropdownMenuItem(
                                    text = { Text(exercicio.nome) },
                                    onClick = {
                                        exercicioSelecionado = exercicio.id
                                        showExercicioDropdown = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Campos para séries, repetições e descanso
                    OutlinedTextField(
                        value = series,
                        onValueChange = { series = it },
                        label = { Text("Séries") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = repeticoes,
                        onValueChange = { repeticoes = it },
                        label = { Text("Repetições") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = descanso,
                        onValueChange = { descanso = it },
                        label = { Text("Descanso (minutos)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            val exercicioId = exercicioSelecionado
                            if (exercicioId != null && series.isNotBlank() && repeticoes.isNotBlank() && descanso.isNotBlank()) {
                                treinoViewModel.adicionarExercicioAoTreino(
                                    treinoId = treinoId,
                                    exercicioId = exercicioId,
                                    series = series.toIntOrNull() ?: 0,
                                    repeticoes = repeticoes.toIntOrNull() ?: 0,
                                    descanso = descanso.toDoubleOrNull() ?: 0.0
                                )
                                // Limpar campos
                                exercicioSelecionado = null
                                series = ""
                                repeticoes = ""
                                descanso = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Adicionar Exercício")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Lista de exercícios do treino
            Text(
                text = "Exercícios do Treino",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(exerciciosTreino) { exercicioTreino ->
                    val exercicio = exerciciosState.value.find { it.id == exercicioTreino.id_exercicio }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = exercicio?.nome ?: "Exercício não encontrado",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${exercicioTreino.series} séries x ${exercicioTreino.repeticoes} repetições",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Descanso: ${exercicioTreino.descanso} minutos",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            
                            IconButton(
                                onClick = {
                                    treinoViewModel.removerExercicioDoTreino(exercicioTreino)
                                }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remover exercício",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
} 