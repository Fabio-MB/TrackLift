package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ImcScreen(onBack: () -> Unit) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var imc by remember { mutableStateOf<Double?>(null) }

    val imcResult = imc?.let { calculateImcResult(it) }

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
                text = "Calculadora IMC",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card de Detalhes
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF323234), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Detalhes",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Altura (cm)", color = Color.White) },
                    placeholder = { Text("Enter your height") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Peso (kg)", color = Color.White) },
                    placeholder = { Text("Enter your weight") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val h = height.toFloatOrNull()
                        val w = weight.toFloatOrNull()
                        if (h != null && w != null && h > 0) {
                            imc = (w / ((h / 100) * (h / 100))).toDouble()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA500))
                ) {
                    Text("Calcular IMC", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card de Resultado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF323234), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "IMC",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(Color(0xFFFFA500), RoundedCornerShape(50))
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = imc?.let { String.format("%.1f", it) } ?: "--",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = imcResult ?: "",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

fun calculateImcResult(imc: Double): String {
    return when {
        imc < 18.5 -> "Baixo peso"
        imc < 25 -> "Normal"
        imc < 30 -> "Sobrepeso"
        else -> "Obesidade"
    }
} 