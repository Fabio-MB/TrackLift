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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tracklift_asa.data.Exercicio
import com.example.tracklift_asa.data.CategoriaExercicio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrudExercicioScreen(
    onBack: () -> Unit,
    exercicioViewModel: ExercicioViewModel,
    onExercicioClick: (Exercicio) -> Unit,
    userId: Int
) {
    var nome by remember { mutableStateOf("") }
    var imagem by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf(CategoriaExercicio.PEITORAL) }
    var descricao by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val exercicios by exercicioViewModel.exercicios.collectAsState()

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
                            text = "Novo Exercício",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = { Text("Nome do Exercício") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFFA500),
                                unfocusedBorderColor = Color(0xFF9E9E9E),
                                focusedLabelColor = Color(0xFFFFA500),
                                unfocusedLabelColor = Color(0xFF9E9E9E),
                                cursorColor = Color(0xFFFFA500),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = imagem,
                            onValueChange = { imagem = it },
                            label = { Text("URL da Imagem") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFFA500),
                                unfocusedBorderColor = Color(0xFF9E9E9E),
                                focusedLabelColor = Color(0xFFFFA500),
                                unfocusedLabelColor = Color(0xFF9E9E9E),
                                cursorColor = Color(0xFFFFA500),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = categoria.name,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Categoria") },
                                trailingIcon = {
                                    IconButton(onClick = { expanded = !expanded }) {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "Selecionar categoria"
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFFFA500),
                                    unfocusedBorderColor = Color(0xFF9E9E9E),
                                    focusedLabelColor = Color(0xFFFFA500),
                                    unfocusedLabelColor = Color(0xFF9E9E9E),
                                    cursorColor = Color(0xFFFFA500),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                )
                            )
                            
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                CategoriaExercicio.values().forEach { cat ->
                                    DropdownMenuItem(
                                        text = { Text(cat.name) },
                                        onClick = {
                                            categoria = cat
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        OutlinedTextField(
                            value = descricao,
                            onValueChange = { descricao = it },
                            label = { Text("Descrição") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFFFA500),
                                unfocusedBorderColor = Color(0xFF9E9E9E),
                                focusedLabelColor = Color(0xFFFFA500),
                                unfocusedLabelColor = Color(0xFF9E9E9E),
                                cursorColor = Color(0xFFFFA500),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                if (nome.isNotBlank()) {
                                    val novoExercicio = Exercicio(
                                        nome = nome,
                                        imagem = imagem,
                                        categoria = categoria,
                                        descricao = descricao,
                                        idUsuario = userId
                                    )
                                    exercicioViewModel.inserirExercicio(novoExercicio)
                                    nome = ""
                                    imagem = ""
                                    descricao = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFA500)
                            )
                        ) {
                            Text("Adicionar Exercício")
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "Exercícios Cadastrados",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            items(exercicios) { exercicio ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExercicioClick(exercicio) },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2D2D2D)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = exercicio.nome,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = exercicio.categoria.name,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                        IconButton(onClick = { exercicioViewModel.excluirExercicio(exercicio) }) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Excluir",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
} 