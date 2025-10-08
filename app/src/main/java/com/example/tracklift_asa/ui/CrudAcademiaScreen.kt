package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tracklift_asa.data.Academia

@Composable
fun CrudAcademiaScreen(
    onBack: () -> Unit,
    academiaViewModel: AcademiaViewModel,
    onAcademiaClick: (Academia) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var localizacao by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var imagem by remember { mutableStateOf("") }
    val academias by academiaViewModel.academias.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232325))
            .padding(16.dp)
    ) {
        // TopBar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Gerenciar Academias",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Formulário de cadastro
        Text("Adicionar nova academia", color = Color.White, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = localizacao,
            onValueChange = { localizacao = it },
            label = { Text("Localização") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = tipo,
            onValueChange = { tipo = it },
            label = { Text("Tipo") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = preco,
            onValueChange = { preco = it.filter { c -> c.isDigit() || c == '.' } },
            label = { Text("Preço") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            singleLine = true
        )
        OutlinedTextField(
            value = imagem,
            onValueChange = { imagem = it },
            label = { Text("URL da Imagem") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            singleLine = true
        )
        Button(
            onClick = {
                if (nome.isNotBlank() && localizacao.isNotBlank() && tipo.isNotBlank() && preco.isNotBlank()) {
                    academiaViewModel.inserirAcademia(
                        Academia(
                            nome = nome,
                            localizacao = localizacao,
                            tipo = tipo,
                            preco = preco.toDoubleOrNull() ?: 0.0,
                            imagem = imagem
                        )
                    )
                    nome = ""
                    localizacao = ""
                    tipo = ""
                    preco = ""
                    imagem = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("Adicionar")
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Listagem de academias
        Text("Academias cadastradas:", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        academias.forEach { academia ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(Color(0xFF323234), RoundedCornerShape(8.dp))
                    .padding(12.dp)
                    .clickable { onAcademiaClick(academia) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(academia.nome, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("${academia.localizacao} | ${academia.tipo} | R$ ${"%.2f".format(academia.preco)}", color = Color.LightGray, fontSize = 13.sp)
                }
                IconButton(onClick = { academiaViewModel.excluirAcademia(academia) }) {
                    Icon(Icons.Default.Close, contentDescription = "Excluir", tint = Color.Red)
                }
            }
        }
    }
} 