package com.example.tracklift_asa.ui

import androidx.compose.foundation.background
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
fun CronometroScreen(
    onBack: () -> Unit
) {
    var tempo by remember { mutableStateOf(0L) }
    var isRunning by remember { mutableStateOf(false) }
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
                text = "Cronômetro",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Display do cronômetro
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = formatTime(tempo),
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
                            while (isRunning) {
                                delay(10)
                                tempo += 10
                            }
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
                    tempo = 0
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF323234)
                )
            ) {
                Text("Resetar")
            }
        }
    }
}

private fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 60000) % 60
    val seconds = (timeInMillis / 1000) % 60
    val milliseconds = (timeInMillis / 10) % 100
    return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
} 