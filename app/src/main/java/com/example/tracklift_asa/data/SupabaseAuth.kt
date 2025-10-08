package com.example.tracklift_asa.data

import android.content.Context
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    val height: Int,
    val age: Int
)

// Dados para o Supabase
@Serializable
data class SupabaseUser(
    val id: String,
    val email: String,
    val user_metadata: Map<String, String> = emptyMap()
)

@Serializable
data class SupabaseAuthResponse(
    val user: SupabaseUser? = null,
    val session: SupabaseSession? = null,
    val access_token: String? = null,
    val refresh_token: String? = null
)

@Serializable
data class SupabaseSession(
    val access_token: String,
    val refresh_token: String,
    val user: SupabaseUser
)

class SupabaseAuth(private val context: Context) {
    private val userDao = AppDatabase.getDatabase(context).userDao()
    private var currentUser: UserProfile? = null
    private var currentSession: SupabaseSession? = null
    
    // Flag para controlar se o Supabase está disponível
    private var supabaseEnabled = true

    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        height: Int,
        age: Int
    ): Result<Unit> = runCatching {
        if (supabaseEnabled) {
            try {
                println("🚀 Tentando registro no Supabase...")
                
                // 1. Registrar no Supabase Auth
                val authResponse = registerInSupabase(email, password, name)
                
                if (authResponse.user != null) {
                    val supabaseUserId = authResponse.user!!.id
                    println("✅ Usuário criado no Supabase Auth com ID: $supabaseUserId")
                    
                    // 2. Salvar no banco local como backup e sincronização
                    val localUser = User(
                        id = supabaseUserId.hashCode().let { if (it < 0) -it else it }.toString(),
                        name = name,
                        email = email,
                        password = password,
                        height = height,
                        age = age
                    )
                    userDao.insert(localUser)
                    
                    // 3. Definir usuário atual
                    currentUser = UserProfile(
                        id = localUser.id.toIntOrNull() ?: System.currentTimeMillis().toInt(),
                        name = name,
                        email = email,
                        height = height,
                        age = age
                    )
                    
                    // 4. Salvar sessão se disponível
                    if (authResponse.session != null) {
                        currentSession = authResponse.session
                    }
                    
                    println("🎉 Usuário registrado com sucesso no Supabase e banco local!")
                    return@runCatching
                }
            } catch (e: Exception) {
                println("❌ Erro no Supabase: ${e.message}")
                e.printStackTrace()
                // Continuar para fallback local
            }
        }
        
        // Fallback para banco local
        println("🔄 Fallback: Salvando apenas no banco local...")
        registerInLocalDatabase(name, email, password, height, age)
    }

    suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        if (supabaseEnabled) {
            try {
                println("🔐 Tentando login no Supabase...")
                
                // 1. Tentar login no Supabase
                val authResponse = loginInSupabase(email, password)
                
                if (authResponse.session != null) {
                    val session = authResponse.session!!
                    val user = session.user
                    
                    println("✅ Login bem-sucedido no Supabase para: ${user.email}")
                    
                    // 2. Salvar sessão
                    currentSession = session
                    
                    // 3. Buscar ou criar usuário local
                    val localUser = userDao.getUserByEmail(email)
                    if (localUser != null) {
                        currentUser = UserProfile(
                            id = localUser.id.toIntOrNull() ?: System.currentTimeMillis().toInt(),
                            name = localUser.name,
                            email = localUser.email,
                            height = localUser.height,
                            age = localUser.age
                        )
                    } else {
                        // Criar usuário local com dados básicos
                        val newLocalUser = User(
                            id = user.id.hashCode().let { if (it < 0) -it else it }.toString(),
                            name = user.user_metadata["name"] ?: "Usuário",
                            email = user.email,
                            password = "", // Não salvamos senha localmente
                            height = 170,
                            age = 25
                        )
                        userDao.insert(newLocalUser)
                        
                        currentUser = UserProfile(
                            id = newLocalUser.id.toIntOrNull() ?: System.currentTimeMillis().toInt(),
                            name = newLocalUser.name,
                            email = newLocalUser.email,
                            height = newLocalUser.height,
                            age = newLocalUser.age
                        )
                    }
                    
                    println("🎉 Login realizado com sucesso no Supabase!")
                    return@runCatching
                }
            } catch (e: Exception) {
                println("❌ Erro no login do Supabase: ${e.message}")
                e.printStackTrace()
                // Continuar para fallback local
            }
        }
        
        // Fallback para banco local
        println("🔄 Fallback: Tentando login via banco local...")
        loginInLocalDatabase(email, password)
    }

    suspend fun signOut() = runCatching {
        if (supabaseEnabled && currentSession != null) {
            try {
                println("🚪 Tentando logout do Supabase...")
                logoutFromSupabase()
                println("✅ Logout realizado no Supabase")
            } catch (e: Exception) {
                println("⚠️ Erro no logout do Supabase: ${e.message}")
            }
        }
        
        // Limpar dados locais
        currentSession = null
        currentUser = null
        println("✅ Usuário desconectado localmente")
    }

    suspend fun getCurrentUser(): Result<UserProfile?> = runCatching {
        if (supabaseEnabled && currentSession != null) {
            try {
                println("🔍 Verificando sessão no Supabase...")
                val user = validateSupabaseSession()
                if (user != null) {
                    currentUser = user
                    println("✅ Sessão válida no Supabase")
                }
            } catch (e: Exception) {
                println("⚠️ Erro ao obter usuário do Supabase: ${e.message}")
                // Sessão inválida, limpar
                currentSession = null
            }
        }
        
        currentUser
    }

    // Métodos privados para implementação
    private suspend fun registerInSupabase(email: String, password: String, name: String): SupabaseAuthResponse {
        try {
            println("🔍 URL do Supabase: ${SupabaseClient.authUrl}/signup")
            println("🔍 Headers: ${SupabaseClient.defaultHeaders}")
            
            val requestBody = mapOf(
                "email" to email,
                "password" to password,
                "data" to mapOf("name" to name)
            )
            println("🔍 Request Body: $requestBody")
            
            val response = SupabaseClient.client.post("${SupabaseClient.authUrl}/signup") {
                headers {
                    SupabaseClient.defaultHeaders.forEach { (key, value) ->
                        append(key, value)
                        println("🔍 Header adicionado: $key = $value")
                    }
                }
                setBody(requestBody)
            }
            
            println("🔍 Response Status: ${response.status}")
            val responseBody = response.body<String>()
            println("🔍 Response Body: $responseBody")
            
            return Json.decodeFromString<SupabaseAuthResponse>(responseBody)
        } catch (e: Exception) {
            println("❌ Erro detalhado no registro Supabase: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private suspend fun loginInSupabase(email: String, password: String): SupabaseAuthResponse {
        try {
            println("🔍 URL do Supabase: ${SupabaseClient.authUrl}/token?grant_type=password")
            
            val requestBody = mapOf(
                "email" to email,
                "password" to password
            )
            println("🔍 Request Body: $requestBody")
            
            val response = SupabaseClient.client.post("${SupabaseClient.authUrl}/token?grant_type=password") {
                headers {
                    SupabaseClient.defaultHeaders.forEach { (key, value) ->
                        append(key, value)
                    }
                }
                setBody(requestBody)
            }
            
            println("🔍 Response Status: ${response.status}")
            val responseBody = response.body<String>()
            println("🔍 Response Body: $responseBody")
            
            return Json.decodeFromString<SupabaseAuthResponse>(responseBody)
        } catch (e: Exception) {
            println("❌ Erro detalhado no login Supabase: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private suspend fun logoutFromSupabase() {
        currentSession?.let { session ->
            try {
                println("🔍 URL do Supabase: ${SupabaseClient.authUrl}/logout")
                SupabaseClient.client.post("${SupabaseClient.authUrl}/logout") {
                    headers {
                        SupabaseClient.defaultHeaders.forEach { (key, value) ->
                            append(key, value)
                        }
                        append("Authorization", "Bearer ${session.access_token}")
                    }
                }
                println("✅ Logout request enviado para Supabase")
            } catch (e: Exception) {
                println("❌ Erro no logout Supabase: ${e.message}")
                throw e
            }
        }
    }

    private suspend fun validateSupabaseSession(): UserProfile? {
        currentSession?.let { session ->
            try {
                println("🔍 URL do Supabase: ${SupabaseClient.authUrl}/user")
                val response = SupabaseClient.client.get("${SupabaseClient.authUrl}/user") {
                    headers {
                        SupabaseClient.defaultHeaders.forEach { (key, value) ->
                            append(key, value)
                        }
                        append("Authorization", "Bearer ${session.access_token}")
                    }
                }
                
                println("🔍 Response Status: ${response.status}")
                val responseBody = response.body<String>()
                println("🔍 Response Body: $responseBody")
                
                val user = Json.decodeFromString<SupabaseUser>(responseBody)
                return UserProfile(
                    id = user.id.hashCode().let { if (it < 0) -it else it },
                    name = user.user_metadata["name"] ?: "Usuário",
                    email = user.email,
                    height = 170,
                    age = 25
                )
            } catch (e: Exception) {
                println("❌ Erro ao validar sessão: ${e.message}")
                e.printStackTrace()
            }
        }
        return null
    }

    private suspend fun registerInLocalDatabase(name: String, email: String, password: String, height: Int, age: Int) {
        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) {
            throw Exception("Email já cadastrado")
        }

        val userId = System.currentTimeMillis().toInt()
        val user = User(
            id = userId.toString(),
            name = name,
            email = email,
            password = password,
            height = height,
            age = age
        )
        
        userDao.insert(user)
        
        currentUser = UserProfile(
            id = userId,
            name = name,
            email = email,
            height = height,
            age = age
        )
        
        println("⚠️ Usuário salvo apenas no banco local (fallback)")
    }

    private suspend fun loginInLocalDatabase(email: String, password: String) {
        val user = userDao.login(email, password)
        if (user != null) {
            currentUser = UserProfile(
                id = user.id.toIntOrNull() ?: System.currentTimeMillis().toInt(),
                name = user.name,
                email = user.email,
                height = user.height,
                age = user.age
            )
            println("⚠️ Login realizado via banco local (fallback)")
        } else {
            throw Exception("Email ou senha inválidos")
        }
    }
}
