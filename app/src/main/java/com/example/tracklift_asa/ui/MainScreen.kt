package com.example.tracklift_asa.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.tracklift_asa.ui.theme.*

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        TrackLiftBackground,
                        TrackLiftSurface,
                        TrackLiftBackground
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Header com logo e perfil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo do app",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "TrackLift",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TrackLiftOnBackground,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                IconButton(
                    onClick = { navController.navigate(Screen.Profile.route) },
                    modifier = Modifier
                        .background(
                            color = TrackLiftSurfaceVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = TrackLiftOnSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Saudação personalizada
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = TrackLiftPrimary),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        buildAnnotatedString {
                            append("Olá ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(currentUser?.name ?: "")
                            }
                            append("! 👋")
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        color = TrackLiftOnPrimary
                    )
                    Text(
                        text = "Pronto para mais um treino?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TrackLiftOnPrimary.copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ferramentas rápidas
            ModernSectionTitle("Ferramentas Rápidas")
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val quickTools = listOf(
                    QuickTool(
                        title = "IMC",
                        icon = Icons.Default.Add,
                        onClick = { navController.navigate(Screen.IMC.route) }
                    ),
                    QuickTool(
                        title = "Cronômetro",
                        icon = Icons.Default.Timer,
                        onClick = { navController.navigate(Screen.Cronometro.route) }
                    ),
                    QuickTool(
                        title = "Timer",
                        icon = Icons.Default.PlayArrow,
                        onClick = { navController.navigate(Screen.Timer.route) }
                    )
                )
                
                items(quickTools) { tool ->
                    QuickToolCard(
                        title = tool.title,
                        icon = tool.icon,
                        onClick = tool.onClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Seus Treinos
            ModernSectionTitle("Seus Treinos", { navController.navigate(Screen.ListaTreinos.route) })
            Spacer(modifier = Modifier.height(12.dp))
            
            if (treinos.isEmpty()) {
                EmptyStateCard(
                    title = "Nenhum treino criado",
                    subtitle = "Comece criando seu primeiro treino",
                    buttonText = "Criar Treino",
                    onClick = { navController.navigate(Screen.CrudTreino.route) }
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(treinos.sortedByDescending { it.id }) { treino ->
                        ModernCardItem(
                            title = treino.nome,
                            subtitle = "Treino personalizado",
                            icon = Icons.Default.FitnessCenter,
                            onClick = { navController.navigate(Screen.DetalhesTreino.createRoute(treino.id)) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Exercícios
            ModernSectionTitle("Exercícios", { navController.navigate(Screen.ListaExercicios.route) })
            Spacer(modifier = Modifier.height(12.dp))
            
            if (exercicios.isEmpty()) {
                EmptyStateCard(
                    title = "Nenhum exercício cadastrado",
                    subtitle = "Adicione exercícios para criar seus treinos",
                    buttonText = "Adicionar Exercício",
                    onClick = { navController.navigate(Screen.CrudExercicio.route) }
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(exercicios) { exercicio ->
                        ModernCardItem(
                            title = exercicio.nome,
                            subtitle = "Exercício",
                            icon = Icons.Default.SportsGymnastics,
                            onClick = { navController.navigate(Screen.DetalhesExercicio.createRoute(exercicio.id)) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Academias
            ModernSectionTitle("Academias", { navController.navigate(Screen.ListaAcademias.route) })
            Spacer(modifier = Modifier.height(12.dp))
            
            if (academias.isEmpty()) {
                EmptyStateCard(
                    title = "Nenhuma academia cadastrada",
                    subtitle = "Descubra academias próximas a você",
                    buttonText = "Ver Academias",
                    onClick = { navController.navigate(Screen.ListaAcademias.route) }
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(academias) { academia ->
                        ModernCardItem(
                            title = academia.nome,
                            subtitle = "Academia",
                            icon = Icons.Default.LocationOn,
                            onClick = { navController.navigate(Screen.DetalhesAcademia.createRoute(academia.id)) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Data classes para ferramentas rápidas
data class QuickTool(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)


@Composable
fun ModernSectionTitle(
    title: String,
    onSeeAllClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = TrackLiftOnBackground,
            fontWeight = FontWeight.Bold
        )
        
        onSeeAllClick?.let { onClick ->
            TextButton(onClick = onClick) {
                Text(
                    text = "Ver todos",
                    color = TrackLiftPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun QuickToolCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(80.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = TrackLiftSurface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = TrackLiftPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TrackLiftOnSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ModernCardItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = TrackLiftSurface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = TrackLiftPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = TrackLiftOnSurface,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TrackLiftOnSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    title: String,
    subtitle: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = TrackLiftSurfaceVariant),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = TrackLiftOnSurfaceVariant,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TrackLiftOnSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TrackLiftOnSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = TrackLiftPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = buttonText,
                    color = TrackLiftOnPrimary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
} 