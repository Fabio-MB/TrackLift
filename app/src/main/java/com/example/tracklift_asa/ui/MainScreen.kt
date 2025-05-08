package com.example.tracklift_asa.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tracklift_asa.R

@Composable
fun MainScreen(
    onVerTodosTreinos: () -> Unit = {},
    onVerTodasAcademias: () -> Unit = {},
    onVerFerramentas: () -> Unit = {},
    onClickAcademia: (String) -> Unit = {},
    onClickFerramenta: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232325))
            .padding(16.dp)
    ) {
        // Logo e botão de sair
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo do app",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            IconButton(onClick = { /* ação de sair */ }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Sair",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Saudação
        Text(
            buildAnnotatedString {
                append("Olá ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("John,")
                }
            },
            color = Color.White,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Seção Treinos
        SectionTitle("Seus Treinos", onVerTodosTreinos)
        CardTreino()

        Spacer(modifier = Modifier.height(16.dp))

        // Seção Academias
        SectionTitle("Academias", onVerTodasAcademias)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardItem("Elite academia", onClick = { onClickAcademia("Elite academia") }, modifier = Modifier.weight(1f))
            CardItem("Health Center", onClick = { onClickAcademia("Health Center") }, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seção Ferramentas
        SectionTitle("Ferramentas", onVerFerramentas)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardItem(
                title = "Calculadora IMC",
                onClick = { onClickFerramenta("Calculadora IMC") },
                modifier = Modifier.weight(1f),
                imageRes = R.drawable.  imc
            )
            CardItem(
                title = "Cronometro",
                onClick = { onClickFerramenta("Cronometro") },
                modifier = Modifier.weight(1f),
                imageRes = R.drawable.cronometro
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, onVerTodos: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = "ver todos",
            color = Color(0xFFFFA500),
            fontSize = 14.sp,
            modifier = Modifier.clickable { onVerTodos() }
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun CardTreino() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
    ) {
        // Placeholder para imagem do treino
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF444444))
        )
        Text(
            text = "Treino 1 - Superiores",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        )
    }
}

@Composable
fun CardItem(title: String, onClick: () -> Unit, modifier: Modifier = Modifier, imageRes: Int? = null) {
    Box(
        modifier = modifier
            .height(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF323234))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
} 