package com.example.tracklift_asa.data

import android.content.Context
import com.example.tracklift_asa.data.SupabaseClient.SUPABASE_URL
import io.ktor.client.HttpClient
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

@Serializable
data class SignUpRequestData(val name: String)

@Serializable
data class SignUpRequestBody(
    val email: String,
    val password: String,
    val data: SignUpRequestData
)

@Serializable
data class SignInRequestBody(
    val email: String,
    val password: String
)

@Serializable
data class ProfileRequestBody(
    val id: String,
    val name: String,
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

    private val authUrl = "${SUPABASE_URL}/auth/v1"
    private val restUrl = "${SUPABASE_URL}/rest/v1"

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
                
                if (authResponse.user != null && authResponse.session != null) {
                    val supabaseUser = authResponse.user
                    val session = authResponse.session
                    println("✅ Usuário criado no Supabase Auth com ID: ${supabaseUser.id}")

                    // 2. Salvar perfil no banco de dados do Supabase
                    saveProfileToSupabase(supabaseUser.id, name, height, age, session.access_token)
                    
                    // 3. Salvar no banco local como backup e sincronização
                    val localUser = User(
                        id = supabaseUser.id.hashCode().let { if (it < 0) -it else it }.toString(),
                        name = name,
                        email = email,
                        password = password, // Considere não salvar a senha em texto claro
                        height = height,
                        age = age
                    )
                    userDao.insert(localUser)
                    
                    // 4. Definir usuário atual
                    currentUser = UserProfile(
                        id = localUser.id.toIntOrNull() ?: System.currentTimeMillis().toInt(),
                        name = name,
                        email = email,
                        height = height,
                        age = age
                    )
                    
                    // 5. Salvar sessão atual
                    currentSession = session
                    
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
            println("🔍 URL do Supabase: $authUrl/signup")
            
            val requestBody = SignUpRequestBody(
                email = email,
                password = password,
                data = SignUpRequestData(name = name)
            )
            println("🔍 Request Body: $requestBody")
            
            val response = SupabaseClient.client.post("$authUrl/signup") {
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
            println("🔍 URL do Supabase: $authUrl/token?grant_type=password")
            
            val requestBody = SignInRequestBody(email, password)
            println("🔍 Request Body: $requestBody")
            
            val response = SupabaseClient.client.post("$authUrl/token?grant_type=password") {
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
                println("🔍 URL do Supabase: $authUrl/logout")
                SupabaseClient.client.post("$authUrl/logout") {
                    headers {
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
                println("🔍 URL do Supabase: $authUrl/user")
                val response = SupabaseClient.client.get("$authUrl/user") {
                    headers {
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

    private suspend fun saveProfileToSupabase(id: String, name: String, height: Int, age: Int, accessToken: String) {
        try {
            println("💾 Salvando perfil no banco de dados Supabase...")
            val profileData = ProfileRequestBody(
                id = id,
                name = name,
                height = height,
                age = age
            )

            val response = SupabaseClient.client.post("$restUrl/profiles") {
                headers {
                    append("Authorization", "Bearer $accessToken")
                    append("Prefer", "return=minimal")
                }
                setBody(profileData)
            }

            if (response.status.isSuccess()) {
                println("✅ Perfil salvo com sucesso no Supabase DB.")
            } else {
                val errorBody = response.body<String>()
                println("❌ Erro ao salvar perfil no Supabase DB: ${response.status} - $errorBody")
            }

        } catch (e: Exception) {
            println("❌ Erro crítico ao salvar perfil no Supabase: ${e.message}")
            e.printStackTrace()
        }
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
