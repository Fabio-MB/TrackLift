package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SportsGymnastics
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tracklift_asa.data.Exercicio
import com.example.tracklift_asa.data.CategoriaExercicio
import com.example.tracklift_asa.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaExerciciosScreen(
    onBack: () -> Unit,
    exercicioViewModel: ExercicioViewModel,
    onExercicioClick: (Exercicio) -> Unit,
    onCreateExercicio: () -> Unit
) {
    val exerciciosState = exercicioViewModel.exerciciosFiltrados.collectAsState()
    val exercicios = exerciciosState.value
    var queryBusca by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf<CategoriaExercicio?>(null) }
    
    // Atualizar ViewModel quando a query ou categoria mudar
    LaunchedEffect(queryBusca) {
        exercicioViewModel.setQueryBusca(queryBusca)
    }
    
    LaunchedEffect(categoriaSelecionada) {
        exercicioViewModel.setCategoriaFiltro(categoriaSelecionada)
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
                        onClick = onBack,
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
                            text = "Exercícios",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TrackLiftOnPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (queryBusca.isNotEmpty() || categoriaSelecionada != null) {
                                "${exercicios.size} exercício(s) encontrado(s)"
                            } else {
                                "${exercicios.size} exercícios cadastrados"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = TrackLiftOnPrimary.copy(alpha = 0.8f)
                        )
                    }
                    
                    // Botão de adicionar
                    FloatingActionButton(
                        onClick = onCreateExercicio,
                        modifier = Modifier.size(48.dp),
                        containerColor = TrackLiftOnPrimary,
                        contentColor = TrackLiftPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar exercício",
                            modifier = Modifier.size(24.dp)
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
            
            // Lista de exercícios
            if (exercicios.isEmpty()) {
                // Estado vazio modernizado
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
                            } else {
                                "Comece adicionando seu primeiro exercício"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = TrackLiftOnSurfaceVariant.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        if (queryBusca.isEmpty() && categoriaSelecionada == null) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = onCreateExercicio,
                                colors = ButtonDefaults.buttonColors(containerColor = TrackLiftPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Adicionar Exercício",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            Spacer(modifier = Modifier.height(24.dp))
                            OutlinedButton(
                                onClick = { 
                                    queryBusca = ""
                                    categoriaSelecionada = null
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Limpar Filtros",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(exercicios) { exercicio ->
                        ModernExercicioCard(
                            exercicio = exercicio,
                            onClick = { onExercicioClick(exercicio) }
                        )
                    }
                    
                    // Espaçamento inferior para o FAB
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
        
        // Floating Action Button fixo
        if (exercicios.isNotEmpty()) {
            FloatingActionButton(
                onClick = onCreateExercicio,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                containerColor = TrackLiftPrimary,
                contentColor = TrackLiftOnPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar exercício"
                )
            }
        }
    }
}

@Composable
fun ModernExercicioCard(
    exercicio: Exercicio,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = TrackLiftSurface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone do exercício
            Card(
                modifier = Modifier.size(56.dp),
                colors = CardDefaults.cardColors(containerColor = TrackLiftPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (exercicio.imagem.isNotBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(exercicio.imagem)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Imagem do exercício ${exercicio.nome}",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.SportsGymnastics,
                            contentDescription = null,
                            tint = TrackLiftOnPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Informações do exercício
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercicio.nome,
                    style = MaterialTheme.typography.titleLarge,
                    color = TrackLiftOnSurface,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = exercicio.categoria.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TrackLiftOnSurfaceVariant
                )
                
                if (exercicio.descricao.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = exercicio.descricao,
                        style = MaterialTheme.typography.bodySmall,
                        color = TrackLiftOnSurfaceVariant.copy(alpha = 0.8f),
                        maxLines = 2
                    )
                }
            }
        }
    }
} 