package com.example.tracklift_asa.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tracklift_asa.data.FotoProgresso
import com.example.tracklift_asa.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FotosProgressoScreen(
    onBack: () -> Unit,
    fotoProgressoViewModel: FotoProgressoViewModel,
    userId: Int
) {
    val fotosState = fotoProgressoViewModel.fotos.collectAsState()
    val fotos = fotosState.value
    
    var showDeleteDialog by remember { mutableStateOf<FotoProgresso?>(null) }
    var observacao by remember { mutableStateOf("") }
    var showObservacaoDialog by remember { mutableStateOf<Uri?>(null) }
    
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { 
            showObservacaoDialog = it
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            text = "Fotos de Progresso",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${fotos.size} foto(s) registrada(s)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TrackLiftOnSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = TrackLiftOnPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TrackLiftPrimary,
                    titleContentColor = TrackLiftOnPrimary,
                    navigationIconContentColor = TrackLiftOnPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    imagePickerLauncher.launch("image/*")
                },
                containerColor = TrackLiftPrimary,
                contentColor = TrackLiftOnPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar foto"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(TrackLiftBackground)
        ) {
            if (fotos.isEmpty()) {
                // Estado vazio
                EmptyFotosState(
                    onAddPhoto = {
                        imagePickerLauncher.launch("image/*")
                    }
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(fotos) { foto ->
                        FotoProgressoCard(
                            foto = foto,
                            onDeleteClick = { showDeleteDialog = foto }
                        )
                    }
                }
            }
        }
    }
    
    // Dialog para observação
    showObservacaoDialog?.let { uri ->
        AlertDialog(
            onDismissRequest = { 
                showObservacaoDialog = null
                observacao = ""
            },
            title = { Text("Adicionar Observação (Opcional)") },
            text = {
                OutlinedTextField(
                    value = observacao,
                    onValueChange = { observacao = it },
                    label = { Text("Observação") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ex: Após 3 meses de treino") }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val fotoProgresso = FotoProgresso(
                            idUsuario = userId,
                            uriImagem = uri.toString(),
                            data = System.currentTimeMillis(),
                            observacao = observacao
                        )
                        fotoProgressoViewModel.inserirFoto(fotoProgresso)
                        showObservacaoDialog = null
                        observacao = ""
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showObservacaoDialog = null
                        observacao = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Dialog de confirmação de exclusão
    showDeleteDialog?.let { foto ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Excluir Foto") },
            text = { Text("Tem certeza que deseja excluir esta foto?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        fotoProgressoViewModel.excluirFoto(foto)
                        showDeleteDialog = null
                    }
                ) {
                    Text("Excluir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun FotoProgressoCard(
    foto: FotoProgresso,
    onDeleteClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateText = dateFormat.format(Date(foto.data))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(foto.uriImagem)
                    .crossfade(true)
                    .build(),
                contentDescription = "Foto de progresso",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Overlay com informações
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(12.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = dateText,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                if (foto.observacao.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = foto.observacao,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                }
            }
            
            // Botão de deletar
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyFotosState(onAddPhoto: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Nenhuma foto de progresso",
            style = MaterialTheme.typography.headlineSmall,
            color = TrackLiftOnSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Adicione fotos para acompanhar sua evolução ao longo do tempo",
            style = MaterialTheme.typography.bodyMedium,
            color = TrackLiftOnSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = onAddPhoto,
            colors = ButtonDefaults.buttonColors(containerColor = TrackLiftPrimary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adicionar Primeira Foto")
        }
    }
}
