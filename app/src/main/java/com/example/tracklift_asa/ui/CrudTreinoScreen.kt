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
import com.example.tracklift_asa.data.CategoriaTreino
import com.example.tracklift_asa.data.Treino
import kotlinx.coroutines.launch
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrudTreinoScreen(
    navController: NavController,
    treinoViewModel: TreinoViewModel,
    onTreinoClick: (Treino) -> Unit,
    userId: Int
) {
    var nomeTreino by remember { mutableStateOf("") }
    var categoriaSelecionada by remember { mutableStateOf<CategoriaTreino?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var treinoParaExcluir by remember { mutableStateOf<Treino?>(null) }
    var showCategoriaDropdown by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val treinos = treinoViewModel.treinos.collectAsState()

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gerenciar Treinos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Formulário para adicionar treino
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
                            text = "Adicionar Treino",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedTextField(
                            value = nomeTreino,
                            onValueChange = { nomeTreino = it },
                            label = { Text("Nome do Treino") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Campo para selecionar categoria
                        Box {
                            OutlinedTextField(
                                value = categoriaSelecionada?.name ?: "",
                                onValueChange = { },
                                label = { Text("Categoria") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showCategoriaDropdown = true }) {
                                        Icon(Icons.Default.ArrowDropDown, "Selecionar categoria")
                                    }
                                }
                            )
                            
                            DropdownMenu(
                                expanded = showCategoriaDropdown,
                                onDismissRequest = { showCategoriaDropdown = false },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                CategoriaTreino.values().forEach { categoria ->
                                    DropdownMenuItem(
                                        text = { Text(categoria.name) },
                                        onClick = {
                                            categoriaSelecionada = categoria
                                            showCategoriaDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                if (nomeTreino.isNotBlank() && categoriaSelecionada != null) {
                                    isLoading = true
                                    scope.launch {
                                        try {
                                            val treino = Treino(
                                                nome = nomeTreino,
                                                categoria = categoriaSelecionada!!,
                                                userId = userId
                                            )
                                            treinoViewModel.inserirTreino(treino)
                                            nomeTreino = ""
                                            categoriaSelecionada = null
                                            snackbarMessage = "Treino criado com sucesso!"
                                            showSnackbar = true
                                        } catch (e: Exception) {
                                            snackbarMessage = "Erro ao criar treino: ${e.message}"
                                            showSnackbar = true
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                } else {
                                    snackbarMessage = "Preencha todos os campos"
                                    showSnackbar = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text("Adicionar Treino")
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Lista de treinos
                Text(
                    text = "Treinos Cadastrados",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(400.dp)
                ) {
                    items(treinos.value) { treino ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            onClick = { onTreinoClick(treino) }
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
                                        text = treino.nome,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Categoria: ${treino.categoria.name}",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                
                                IconButton(
                                    onClick = {
                                        treinoParaExcluir = treino
                                        showDialog = true
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Excluir treino",
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
    
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Deseja realmente excluir o treino ${treinoParaExcluir?.nome}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        treinoParaExcluir?.let { treinoViewModel.excluirTreino(it) }
                        showDialog = false
                        treinoParaExcluir = null
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { showSnackbar = false }) {
                    Text("OK")
                }
            }
        ) {
            Text(snackbarMessage)
        }
    }
} 