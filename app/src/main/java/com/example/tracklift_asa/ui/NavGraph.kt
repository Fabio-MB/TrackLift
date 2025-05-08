package com.example.tracklift_asa.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onClickFerramenta = { ferramenta ->
                    if (ferramenta == "Calculadora IMC") {
                        navController.navigate("imc")
                    }
                }
            )
        }
        composable("imc") {
            ImcScreen(onBack = { navController.popBackStack() })
        }
    }
} 