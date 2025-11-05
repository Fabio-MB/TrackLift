package com.example.tracklift_asa.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tracklift_asa.navigation.Screen
import com.example.tracklift_asa.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTreinosScreen(
    navController: NavController,
    treinoViewModel: TreinoViewModel
) {
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val treinosState = treinoViewModel.treinos.collectAsState()
    val treinos = treinosState.value

    LaunchedEffect(Unit) {
        try {
            Log.d("ListaTreinosScreen", "Iniciando carregamento dos treinos")
            isLoading = true
            error = null
            
            // Aguarda o primeiro valor dos treinos
            val treinosList = treinosState.value
            Log.d("ListaTreinosScreen", "Treinos carregados: ${treinosList.size}")
            
            isLoading = false
        } catch (e: Exception) {
            Log.e("ListaTreinosScreen", "Erro ao carregar treinos: ${e.message}", e)
            error = "Erro ao carregar treinos: ${e.message}"
            isLoading = false
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
                        onClick = { navController.popBackStack() },
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
                            text = "Meus Treinos",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TrackLiftOnPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${treinos.size} treinos criados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TrackLiftOnPrimary.copy(alpha = 0.8f)
                        )
                    }
                    
                    // Botão de histórico
                    IconButton(
                        onClick = { navController.navigate(Screen.HistoricoTreinos.route) },
                        modifier = Modifier
                            .background(
                                color = TrackLiftOnPrimary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Histórico",
                            tint = TrackLiftOnPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    // Botão de adicionar
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.CrudTreino.route) },
                        modifier = Modifier.size(48.dp),
                        containerColor = TrackLiftOnPrimary,
                        contentColor = TrackLiftPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar treino",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            
            // Conteúdo principal
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = TrackLiftSurface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = TrackLiftPrimary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Carregando treinos...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TrackLiftOnSurface
                                )
                            }
                        }
                    }
                }
                error != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        colors = CardDefaults.cardColors(containerColor = TrackLiftError.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Erro ao carregar treinos",
                                style = MaterialTheme.typography.headlineSmall,
                                color = TrackLiftError,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error ?: "Erro desconhecido",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TrackLiftOnSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    isLoading = true
                                    error = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = TrackLiftPrimary)
                            ) {
                                Text("Tentar Novamente")
                            }
                        }
                    }
                }
                treinos.isEmpty() -> {
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
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = null,
                                tint = TrackLiftOnSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Nenhum treino criado",
                                style = MaterialTheme.typography.headlineSmall,
                                color = TrackLiftOnSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Comece criando seu primeiro treino personalizado",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TrackLiftOnSurfaceVariant.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { navController.navigate(Screen.CrudTreino.route) },
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
                                    text = "Criar Treino",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(treinos.sortedByDescending { it.id }) { treino ->
                            ModernTreinoCard(
                                treino = treino,
                                onClick = { 
                                    navController.navigate(Screen.DetalhesTreino.createRoute(treino.id))
                                }
                            )
                        }
                        
                        // Espaçamento inferior para o FAB
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
        
        // Floating Action Button fixo
        if (treinos.isNotEmpty()) {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.CrudTreino.route) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                containerColor = TrackLiftPrimary,
                contentColor = TrackLiftOnPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar treino"
                )
            }
        }
    }
}

@Composable
fun ModernTreinoCard(
    treino: com.example.tracklift_asa.data.Treino,
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
            // Ícone do treino
            Card(
                modifier = Modifier.size(56.dp),
                colors = CardDefaults.cardColors(containerColor = TrackLiftPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = TrackLiftOnPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Informações do treino
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = treino.nome,
                    style = MaterialTheme.typography.titleLarge,
                    color = TrackLiftOnSurface,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Categoria: ${treino.categoria.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TrackLiftOnSurfaceVariant
                )
            }
        }
    }
} 