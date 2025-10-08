package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tracklift_asa.data.Exercicio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaExerciciosScreen(
    onBack: () -> Unit,
    exercicioViewModel: ExercicioViewModel,
    onExercicioClick: (Exercicio) -> Unit
) {
    val exerciciosState = exercicioViewModel.exercicios.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exercícios") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A1A),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(exerciciosState.value) { exercicio ->
                ExercicioCard(
                    exercicio = exercicio,
                    onClick = { onExercicioClick(exercicio) }
                )
            }
        }
    }
}

@Composable
fun ExercicioCard(
    exercicio: Exercicio,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D2D2D)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagem do exercício
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF3D3D3D)),
                contentAlignment = Alignment.Center
            ) {
                if (exercicio.imagem.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(exercicio.imagem)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagem do exercício ${exercicio.nome}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Informações do exercício
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exercicio.nome,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = exercicio.categoria.name,
                    color = Color(0xFF9E9E9E),
                    fontSize = 14.sp
                )
                
                if (exercicio.descricao.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exercicio.descricao,
                        color = Color(0xFF9E9E9E),
                        fontSize = 14.sp,
                        maxLines = 2
                    )
                }
            }
        }
    }
} 