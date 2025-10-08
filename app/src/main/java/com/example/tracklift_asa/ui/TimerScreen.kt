package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TimerScreen(
    onBack: () -> Unit
) {
    var minutos by remember { mutableStateOf(0) }
    var segundos by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Timer",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Display do timer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = String.format("%02d:%02d", minutos, segundos),
                color = Color.White,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Botões de controle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (isRunning) {
                        isRunning = false
                    } else {
                        isRunning = true
                        scope.launch {
                            while (isRunning && (minutos > 0 || segundos > 0)) {
                                delay(1000)
                                if (segundos > 0) {
                                    segundos--
                                } else {
                                    minutos--
                                    segundos = 59
                                }
                            }
                            isRunning = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color.Red else Color(0xFF4CAF50)
                )
            ) {
                Text(if (isRunning) "Pausar" else "Iniciar")
            }

            Button(
                onClick = {
                    isRunning = false
                    minutos = 0
                    segundos = 0
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF323234)
                )
            ) {
                Text("Resetar")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Definir Timer") },
            text = {
                Column {
                    OutlinedTextField(
                        value = minutos.toString(),
                        onValueChange = { minutos = it.toIntOrNull() ?: 0 },
                        label = { Text("Minutos") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = segundos.toString(),
                        onValueChange = { segundos = it.toIntOrNull() ?: 0 },
                        label = { Text("Segundos") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
} 