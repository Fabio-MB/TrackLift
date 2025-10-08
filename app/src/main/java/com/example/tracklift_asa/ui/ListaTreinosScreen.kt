package com.example.tracklift_asa.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tracklift_asa.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTreinosScreen(
    navController: NavController,
    treinoViewModel: TreinoViewModel
) {
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val treinos = treinoViewModel.treinos.collectAsState()

    LaunchedEffect(Unit) {
        try {
            Log.d("ListaTreinosScreen", "Iniciando carregamento dos treinos")
            isLoading = true
            error = null
            
            // Aguarda o primeiro valor dos treinos
            val treinosList = treinos.value
            Log.d("ListaTreinosScreen", "Treinos carregados: ${treinosList.size}")
            
            isLoading = false
        } catch (e: Exception) {
            Log.e("ListaTreinosScreen", "Erro ao carregar treinos: ${e.message}", e)
            error = "Erro ao carregar treinos: ${e.message}"
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Treinos") },
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
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
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
                            text = error ?: "Erro desconhecido",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                isLoading = true
                                error = null
                            }
                        ) {
                            Text("Tentar Novamente")
                        }
                    }
                }
                treinos.value.isEmpty() -> {
                    Text(
                        text = "Nenhum treino encontrado",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1A1A1A))
                            .padding(paddingValues)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(treinos.value.sortedByDescending { it.id }) { treino ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { 
                                        navController.navigate(Screen.DetalhesTreino.createRoute(treino.id))
                                    },
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
                                        text = treino.nome,
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Categoria: ${treino.categoria.name}",
                                        color = Color.Gray,
                                        fontSize = 14.sp
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