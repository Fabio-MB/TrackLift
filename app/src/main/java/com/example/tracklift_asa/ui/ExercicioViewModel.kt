package com.example.tracklift_asa.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracklift_asa.data.Exercicio
import com.example.tracklift_asa.data.CategoriaExercicio
import com.example.tracklift_asa.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.content.Context

class ExercicioViewModel(application: Application, userId: Int) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).exercicioDao()
    val exercicios: StateFlow<List<Exercicio>> = dao.getByUsuario(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        verificarPrimeiraExecucao(application)
    }

    private fun verificarPrimeiraExecucao(application: Application) {
        val sharedPreferences = application.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val jaExecutou = sharedPreferences.getBoolean("ja_executou_exercicios", false)

        if (!jaExecutou) {
            popularExercicios()
            sharedPreferences.edit().putBoolean("ja_executou_exercicios", true).apply()
        }
    }

    fun getExerciciosByCategoria(categoria: CategoriaExercicio): StateFlow<List<Exercicio>> {
        return dao.getByCategoria(categoria)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun inserirExercicio(exercicio: Exercicio) {
        viewModelScope.launch {
            dao.insert(exercicio)
        }
    }

    fun excluirExercicio(exercicio: Exercicio) {
        viewModelScope.launch {
            dao.delete(exercicio)
        }
    }

    fun popularExercicios() {
        viewModelScope.launch {
            // Exercícios de Peitoral
            val exerciciosPeitoral = listOf(
                Exercicio(
                    idUsuario = 1,
                    nome = "Supino Reto",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2017/08/supino-fechado-cp.jpg",
                    categoria = CategoriaExercicio.PEITORAL,
                    descricao = "Exercício clássico para peitoral, realizado na posição horizontal"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Supino Inclinado",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2016/10/supino-inclinado-como-fazer-musculos-780x470.jpg",
                    categoria = CategoriaExercicio.PEITORAL,
                    descricao = "Variante do supino com foco na parte superior do peitoral"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Supino Declinado",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2017/05/supino-declinado-capa.jpg",
                    categoria = CategoriaExercicio.PEITORAL,
                    descricao = "Variante do supino com foco na parte inferior do peitoral"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Crucifixo",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2017/05/crucifixo-exercicio.jpg",
                    categoria = CategoriaExercicio.PEITORAL,
                    descricao = "Exercício de isolamento para peitoral"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Fly com Halteres",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2016/09/peck-deck-exercicio-execucao-correta.jpg",
                    categoria = CategoriaExercicio.PEITORAL,
                    descricao = "Exercício de isolamento para peitoral usando halteres"
                )
            )

            // Exercícios de Costas
            val exerciciosCostas = listOf(
                Exercicio(
                    idUsuario = 1,
                    nome = "Puxada Frontal",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2019/09/puxada-frontal-costas-aberta-pulley.jpg",
                    categoria = CategoriaExercicio.COSTAS,
                    descricao = "Exercício fundamental para costas"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Remada Curvada",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2015/03/remada-curvada-capa.jpg",
                    categoria = CategoriaExercicio.COSTAS,
                    descricao = "Exercício composto para costas"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Remada Baixa",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2019/09/remada-baixa.jpg",
                    categoria = CategoriaExercicio.COSTAS,
                    descricao = "Exercício de remada na máquina"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Puxada Triangulo",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2017/12/Puxada-alta-com-tri%C3%A2ngulo.jpg",
                    categoria = CategoriaExercicio.COSTAS,
                    descricao = "Variante da puxada com pegada por trás"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Remada Unilateral",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2017/03/remada-unilateral.jpg",
                    categoria = CategoriaExercicio.COSTAS,
                    descricao = "Exercício de remada com um braço"
                )
            )

            // Exercícios de Pernas
            val exerciciosPernas = listOf(
                Exercicio(
                    idUsuario = 1,
                    nome = "Agachamento",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2015/11/agachamento-sumo-musculos-.jpg",
                    categoria = CategoriaExercicio.PERNAS,
                    descricao = "Exercício fundamental para pernas"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Leg Press",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2021/12/leg-press-horizontal-para-que-serve-como-fazer.jpg",
                    categoria = CategoriaExercicio.PERNAS,
                    descricao = "Exercício de pressão para pernas"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Extensão de Pernas",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2016/08/cadeira-extensora-execucao-correta.jpg",
                    categoria = CategoriaExercicio.PERNAS,
                    descricao = "Exercício de isolamento para quadríceps"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Flexão de Pernas",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2017/11/cadeira-flexora-e-a-mesa-flexora-diferencas.jpg",
                    categoria = CategoriaExercicio.PERNAS,
                    descricao = "Exercício de isolamento para posterior"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Afundo",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2018/06/exercicio-afundo-.png",
                    categoria = CategoriaExercicio.PERNAS,
                    descricao = "Exercício unilateral para pernas"
                )
            )

            // Exercícios de Braço
            val exerciciosBraco = listOf(
                Exercicio(
                    idUsuario = 1,
                    nome = "Rosca Direta",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2022/07/rosca-direta-barra-w.png",
                    categoria = CategoriaExercicio.BRACO,
                    descricao = "Exercício fundamental para bíceps"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Tríceps Pulley",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2017/09/tricepes-pulley-corda-polia--scaled.jpg",
                    categoria = CategoriaExercicio.BRACO,
                    descricao = "Exercício fundamental para tríceps"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Rosca Martelo",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2017/12/Rosca-Martelo-com-halteres.jpg",
                    categoria = CategoriaExercicio.BRACO,
                    descricao = "Variante da rosca para bíceps e antebraço"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Tríceps Francês",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2022/07/triceps-frances.png",
                    categoria = CategoriaExercicio.BRACO,
                    descricao = "Exercício de isolamento para tríceps"
                ),
                Exercicio(
                    idUsuario = 1,
                    nome = "Rosca Alternada",
                    imagem = "https://treinomestre.com.br/wp-content/uploads/2014/04/rosca-alternada.jpg",
                    categoria = CategoriaExercicio.BRACO,
                    descricao = "Variante da rosca alternando os braços"
                )
            )

            // Inserir todos os exercícios no banco de dados
            val todosExercicios = exerciciosPeitoral + exerciciosCostas + exerciciosPernas + exerciciosBraco
            todosExercicios.forEach { exercicio ->
                dao.insert(exercicio)
            }
        }
    }
} 