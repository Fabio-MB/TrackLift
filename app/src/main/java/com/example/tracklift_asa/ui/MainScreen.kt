package com.example.tracklift_asa.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tracklift_asa.R
import com.example.tracklift_asa.data.Academia
import com.example.tracklift_asa.data.Exercicio
import com.example.tracklift_asa.data.Treino
import com.example.tracklift_asa.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    academiaViewModel: AcademiaViewModel,
    exercicioViewModel: ExercicioViewModel,
    treinoViewModel: TreinoViewModel,
    navController: NavController
) {
    val academiasState = academiaViewModel.academias.collectAsState()
    val exerciciosState = exercicioViewModel.exercicios.collectAsState()
    val treinosState = treinoViewModel.treinos.collectAsState()
    val academias: List<Academia> = academiasState.value.take(3)
    val exercicios: List<Exercicio> = exerciciosState.value.take(3)
    val treinos: List<Treino> = treinosState.value.take(3)

    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as android.app.Application)
    )
    val currentUser by userViewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .padding(16.dp)
            .verticalScroll(scrollState)
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
            IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Saudação
        Text(
            buildAnnotatedString {
                append("Bem vindo, ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(currentUser?.name ?: "")
                }
            },
            color = Color.White,
            fontSize = 28.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Seção Treinos
        SectionTitle("Seus Treinos", { navController.navigate(Screen.ListaTreinos.route) })
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            treinos.sortedByDescending { it.id }.take(3).forEach { treino ->
                CardItem(
                    title = treino.nome,
                    onClick = {
                        navController.navigate(Screen.DetalhesTreino.createRoute(treino.id))
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seção Exercícios
        SectionTitle("Exercícios", { navController.navigate(Screen.ListaExercicios.route) })
        Button(
            onClick = {
                navController.navigate(Screen.CrudExercicio.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar Exercício")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            exercicios.forEach { exercicio ->
                CardItem(
                    title = exercicio.nome,
                    onClick = {
                        navController.navigate(Screen.DetalhesExercicio.createRoute(exercicio.id))
                    },
                    modifier = Modifier.weight(1f),
                    imageUrl = exercicio.imagem
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seção Academias
        SectionTitle("Academias", { navController.navigate(Screen.ListaAcademias.route) })
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            academias.forEach { academia ->
                CardItem(
                    title = academia.nome,
                    onClick = {
                        navController.navigate(Screen.DetalhesAcademia.createRoute(academia.id))
                    },
                    modifier = Modifier.weight(1f),
                    imageUrl = academia.imagem
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seção Ferramentas
        SectionTitle("Ferramentas", {})
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardItem(
                title = "Calculadora IMC",
                onClick = { navController.navigate(Screen.IMC.route) },
                modifier = Modifier.weight(1f),
                imageUrl = "https://is4-ssl.mzstatic.com/image/thumb/Purple71/v4/02/86/13/02861392-1070-e994-a428-aebf467ebe8f/source/512x512bb.jpg"
            )
            CardItem(
                title = "Cronometro",
                onClick = { navController.navigate(Screen.Cronometro.route) },
                modifier = Modifier.weight(1f),
                imageUrl = "https://images.icon-icons.com/474/PNG/512/stopwatch_46861.png"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botão para adicionar treino
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { navController.navigate(Screen.CrudTreino.route) },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2D2D2D)
            )
        ) {
            Text(
                text = "Adicionar Treino",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, onSeeAllClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Ver todos",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.clickable(onClick = onSeeAllClick)
        )
    }
}

@Composable
fun CardItem(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String? = null
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D2D2D)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = title,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
} 