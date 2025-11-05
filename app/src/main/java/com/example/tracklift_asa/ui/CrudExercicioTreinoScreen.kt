package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tracklift_asa.data.ExercicioTreino
import com.example.tracklift_asa.data.CategoriaExercicio
import com.example.tracklift_asa.data.Exercicio
import com.example.tracklift_asa.ui.theme.*
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrudExercicioTreinoScreen(
    navController: NavController,
    treinoId: Int,
    treinoViewModel: TreinoViewModel,
    exercicioViewModel: ExercicioViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var exercicioSelecionadoParaAdicionar by remember { mutableStateOf<Exercicio?>(null) }
    var series by remember { mutableStateOf("") }
    var repeticoes by remember { mutableStateOf("") }
    var descanso by remember { mutableStateOf("") }
    
    val exerciciosState = exercicioViewModel.exerciciosFiltrados.collectAsState()
    val exercicios = exerciciosState.value
    var queryBusca by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf<CategoriaExercicio?>(null) }
    
    // Obter exercícios já adicionados ao treino para excluí-los da lista
    val exerciciosTreinoFlow = remember(treinoId) {
        treinoViewModel.getExerciciosDoTreino(treinoId)
    }
    val exerciciosTreinoState = exerciciosTreinoFlow.collectAsState(initial = emptyList())
    val exerciciosTreino = exerciciosTreinoState.value
    
    // Atualizar ViewModel quando a query ou categoria mudar
    LaunchedEffect(queryBusca) {
        exercicioViewModel.setQueryBusca(queryBusca)
    }
    
    LaunchedEffect(categoriaSelecionada) {
        exercicioViewModel.setCategoriaFiltro(categoriaSelecionada)
    }
    
    // Filtrar exercícios que já estão no treino
    val exerciciosDisponiveis = remember(exercicios, exerciciosTreino) {
        exercicios.filter { exercicio ->
            !exerciciosTreino.any { it.id_exercicio == exercicio.id }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        TrackLiftBackground,
                        TrackLiftSurface,
                        TrackLiftBackground
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header moderno
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                colors = CardDefaults.cardColors(containerColor = TrackLiftPrimary),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .background(
                                color = TrackLiftOnPrimary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = TrackLiftOnPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Adicionar Exercícios",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TrackLiftOnPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${exerciciosTreino.size} exercício(s) no treino",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TrackLiftOnPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            
            // Campo de busca
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                colors = CardDefaults.cardColors(containerColor = TrackLiftSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                OutlinedTextField(
                    value = queryBusca,
                    onValueChange = { queryBusca = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar exercício por nome...") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = TrackLiftOnSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (queryBusca.isNotEmpty()) {
                            IconButton(onClick = { queryBusca = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpar busca",
                                    tint = TrackLiftOnSurfaceVariant
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TrackLiftPrimary,
                        unfocusedBorderColor = TrackLiftOnSurfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
            
            // Filtros de categoria
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 0.dp),
                colors = CardDefaults.cardColors(containerColor = TrackLiftSurface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Filtrar por categoria:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TrackLiftOnSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Botão "Todos"
                        FilterChip(
                            selected = categoriaSelecionada == null,
                            onClick = { categoriaSelecionada = null },
                            label = { Text("Todos") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TrackLiftPrimary,
                                selectedLabelColor = TrackLiftOnPrimary
                            )
                        )
                        
                        // Botões para cada categoria
                        CategoriaExercicio.values().forEach { categoria ->
                            FilterChip(
                                selected = categoriaSelecionada == categoria,
                                onClick = { 
                                    categoriaSelecionada = if (categoriaSelecionada == categoria) null else categoria
                                },
                                label = { Text(categoria.name) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TrackLiftPrimary,
                                    selectedLabelColor = TrackLiftOnPrimary
                                )
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Lista de exercícios disponíveis
            if (exerciciosDisponiveis.isEmpty()) {
                // Estado vazio
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = TrackLiftSurfaceVariant),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsGymnastics,
                            contentDescription = null,
                            tint = TrackLiftOnSurfaceVariant,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (queryBusca.isNotEmpty() || categoriaSelecionada != null) {
                                "Nenhum exercício encontrado"
                            } else if (exerciciosTreino.isNotEmpty()) {
                                "Todos os exercícios já foram adicionados"
                            } else {
                                "Nenhum exercício cadastrado"
                            },
                            style = MaterialTheme.typography.headlineSmall,
                            color = TrackLiftOnSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = if (queryBusca.isNotEmpty() || categoriaSelecionada != null) {
                                "Tente ajustar sua busca ou filtro"
                            } else if (exerciciosTreino.isEmpty()) {
                                "Adicione exercícios primeiro"
                            } else {
                                "Todos os exercícios já estão no treino"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = TrackLiftOnSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(exerciciosDisponiveis) { exercicio ->
                        ModernExercicioCard(
                            exercicio = exercicio,
                            onClick = {
                                exercicioSelecionadoParaAdicionar = exercicio
                                series = ""
                                repeticoes = ""
                                descanso = ""
                                showDialog = true
                            }
                        )
                    }
                    
                    // Espaçamento inferior
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
    
    // Diálogo para adicionar exercício
    if (showDialog && exercicioSelecionadoParaAdicionar != null) {
        AlertDialog(
            onDismissRequest = { 
                showDialog = false
                exercicioSelecionadoParaAdicionar = null
            },
            title = {
                Text(
                    text = "Adicionar ${exercicioSelecionadoParaAdicionar?.nome}",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = series,
                        onValueChange = { series = it },
                        label = { Text("Séries") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    
                    OutlinedTextField(
                        value = repeticoes,
                        onValueChange = { repeticoes = it },
                        label = { Text("Repetições") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    
                    OutlinedTextField(
                        value = descanso,
                        onValueChange = { descanso = it },
                        label = { Text("Descanso (minutos)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val exercicioId = exercicioSelecionadoParaAdicionar?.id
                        if (exercicioId != null && series.isNotBlank() && repeticoes.isNotBlank() && descanso.isNotBlank()) {
                            treinoViewModel.adicionarExercicioAoTreino(
                                treinoId = treinoId,
                                exercicioId = exercicioId,
                                series = series.toIntOrNull() ?: 0,
                                repeticoes = repeticoes.toIntOrNull() ?: 0,
                                descanso = descanso.toDoubleOrNull() ?: 0.0
                            )
                            showDialog = false
                            exercicioSelecionadoParaAdicionar = null
                            series = ""
                            repeticoes = ""
                            descanso = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TrackLiftPrimary)
                ) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDialog = false
                    exercicioSelecionadoParaAdicionar = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
} 