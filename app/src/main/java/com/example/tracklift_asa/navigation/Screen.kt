package com.example.tracklift_asa.navigation

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object ListaTreinos : Screen("lista_treinos")
    object DetalhesTreino : Screen("detalhes_treino/{treinoId}") {
        fun createRoute(treinoId: Int) = "detalhes_treino/$treinoId"
    }
    object CrudTreino : Screen("crud_treino")
    object ListaExercicios : Screen("lista_exercicios")
    object DetalhesExercicio : Screen("detalhes_exercicio/{exercicioId}") {
        fun createRoute(exercicioId: Int) = "detalhes_exercicio/$exercicioId"
    }
    object CrudExercicio : Screen("crud_exercicio")
    object CrudExercicioTreino : Screen("crud_exercicio_treino/{treinoId}") {
        fun createRoute(treinoId: Int) = "crud_exercicio_treino/$treinoId"
    }
    object ListaAcademias : Screen("lista_academias")
    object DetalhesAcademia : Screen("detalhes_academia/{academiaId}") {
        fun createRoute(academiaId: Int) = "detalhes_academia/$academiaId"
    }
    object CrudAcademia : Screen("crud_academia")
    object IMC : Screen("imc")
    object Cronometro : Screen("cronometro")
    object Timer : Screen("timer")
    object Login : Screen("login")
    object Profile : Screen("profile")
    object FotosProgresso : Screen("fotos_progresso")
    object HistoricoTreinos : Screen("historico_treinos")
    object DetalhesHistoricoTreino : Screen("detalhes_historico_treino/{historicoId}") {
        fun createRoute(historicoId: Int) = "detalhes_historico_treino/$historicoId"
    }
} 