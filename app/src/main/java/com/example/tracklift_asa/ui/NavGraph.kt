package com.example.tracklift_asa.ui

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tracklift_asa.navigation.Screen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(application)
    )
    val academiaViewModel: AcademiaViewModel = viewModel(
        factory = AcademiaViewModelFactory(application)
    )

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            com.example.tracklift_asa.ui.screens.LoginScreen(navController = navController, userViewModel = userViewModel)
        }

        composable(Screen.MainScreen.route) {
            val currentUser = userViewModel.currentUser.collectAsState().value
            currentUser?.let { user ->
                val treinoViewModel: TreinoViewModel = viewModel(
                    factory = TreinoViewModelFactory(application, user.id)
                )
                val exercicioViewModel: ExercicioViewModel = viewModel(
                    factory = ExercicioViewModelFactory(application, user.id)
                )
                MainScreen(
                    academiaViewModel = academiaViewModel,
                    exercicioViewModel = exercicioViewModel,
                    treinoViewModel = treinoViewModel,
                    navController = navController
                )
            }
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController, userViewModel = userViewModel)
        }

        composable(Screen.Cronometro.route) {
            CronometroScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.Timer.route) {
            TimerScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.CrudAcademia.route) {
            CrudAcademiaScreen(
                onBack = { navController.popBackStack() },
                academiaViewModel = academiaViewModel,
                onAcademiaClick = { academia ->
                    navController.navigate(Screen.DetalhesAcademia.createRoute(academia.id))
                }
            )
        }

        composable(Screen.ListaAcademias.route) {
            ListaAcademiasScreen(
                onBack = { navController.popBackStack() },
                academiaViewModel = academiaViewModel,
                onAcademiaClick = { academia ->
                    navController.navigate(Screen.DetalhesAcademia.createRoute(academia.id))
                }
            )
        }

        composable(Screen.DetalhesAcademia.route) { backStackEntry ->
            val academiaId = backStackEntry.arguments?.getString("academiaId")?.toIntOrNull()
            val academias = academiaViewModel.academias.collectAsState().value
            val academia = academias.find { it.id == academiaId }

            academia?.let {
                DetalhesAcademiaScreen(
                    onBack = { navController.popBackStack() },
                    academia = it
                )
            }
        }

        composable(Screen.CrudExercicio.route) {
            val currentUser = userViewModel.currentUser.collectAsState().value
            currentUser?.let { user ->
                val exercicioViewModel: ExercicioViewModel = viewModel(
                    factory = ExercicioViewModelFactory(application, user.id)
                )
                CrudExercicioScreen(
                    onBack = { navController.popBackStack() },
                    exercicioViewModel = exercicioViewModel,
                    onExercicioClick = { exercicio ->
                        navController.navigate(Screen.DetalhesExercicio.createRoute(exercicio.id))
                    },
                    userId = user.id
                )
            }
        }

        composable(Screen.ListaExercicios.route) {
            val currentUser = userViewModel.currentUser.collectAsState().value
            currentUser?.let { user ->
                val exercicioViewModel: ExercicioViewModel = viewModel(
                    factory = ExercicioViewModelFactory(application, user.id)
                )
                ListaExerciciosScreen(
                    onBack = { navController.popBackStack() },
                    exercicioViewModel = exercicioViewModel,
                    onExercicioClick = { exercicio ->
                        navController.navigate(Screen.DetalhesExercicio.createRoute(exercicio.id))
                    },
                    onCreateExercicio = { navController.navigate(Screen.CrudExercicio.route) }
                )
            }
        }

        composable(Screen.DetalhesExercicio.route) { backStackEntry ->
            val exercicioId = backStackEntry.arguments?.getString("exercicioId")?.toIntOrNull()
            val currentUser = userViewModel.currentUser.collectAsState().value
            currentUser?.let { user ->
                val exercicioViewModel: ExercicioViewModel = viewModel(
                    factory = ExercicioViewModelFactory(application, user.id)
                )
                val exercicios = exercicioViewModel.exercicios.collectAsState().value
                val exercicio = exercicios.find { it.id == exercicioId }

                exercicio?.let {
                    DetalhesExercicioScreen(
                        onBack = { navController.popBackStack() },
                        exercicio = it
                    )
                }
            }
        }

        composable(Screen.IMC.route) {
            ImcScreen(onBack = { navController.popBackStack() })
        }

        composable(Screen.CrudTreino.route) {
            val currentUser = userViewModel.currentUser.collectAsState().value
            currentUser?.let { user ->
                val treinoViewModel: TreinoViewModel = viewModel(
                    factory = TreinoViewModelFactory(application, user.id)
                )
                CrudTreinoScreen(
                    navController = navController,
                    treinoViewModel = treinoViewModel,
                    onTreinoClick = { treino ->
                        navController.navigate(Screen.CrudExercicioTreino.createRoute(treino.id))
                    },
                    userId = user.id
                )
            }
        }

        composable(
            route = Screen.CrudExercicioTreino.route,
            arguments = listOf(
                navArgument("treinoId") { type = androidx.navigation.NavType.IntType }
            )
        ) { backStackEntry ->
            val treinoId = backStackEntry.arguments?.getInt("treinoId")
            val currentUser = userViewModel.currentUser.collectAsState().value
            if (treinoId != null && currentUser != null) {
                val treinoViewModel: TreinoViewModel = viewModel(
                    factory = TreinoViewModelFactory(application, currentUser.id)
                )
                CrudExercicioTreinoScreen(
                    navController = navController,
                    treinoId = treinoId,
                    treinoViewModel = treinoViewModel
                )
            }
        }

        composable(
            route = Screen.DetalhesTreino.route,
            arguments = listOf(
                navArgument("treinoId") { type = androidx.navigation.NavType.IntType }
            )
        ) { backStackEntry ->
            val treinoId = backStackEntry.arguments?.getInt("treinoId")
            val currentUser = userViewModel.currentUser.collectAsState().value
            if (treinoId != null && currentUser != null) {
                val treinoViewModel: TreinoViewModel = viewModel(
                    factory = TreinoViewModelFactory(application, currentUser.id)
                )
                DetalhesTreinoScreen(
                    navController = navController,
                    treinoId = treinoId,
                    treinoViewModel = treinoViewModel
                )
            }
        }

        composable(Screen.ListaTreinos.route) {
            val currentUser = userViewModel.currentUser.collectAsState().value
            currentUser?.let { user ->
                val treinoViewModel: TreinoViewModel = viewModel(
                    factory = TreinoViewModelFactory(application, user.id)
                )
                ListaTreinosScreen(
                    navController = navController,
                    treinoViewModel = treinoViewModel
                )
            }
        }
    }
} 