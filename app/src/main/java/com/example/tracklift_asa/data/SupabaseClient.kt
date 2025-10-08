package com.example.tracklift_asa.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object SupabaseClient {
    private const val SUPABASE_URL = "https://kcqearqyayyubzwiewcl.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtjcWVhcnF5YXl5dWJ6d2lld2NsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDg3MzIwNzUsImV4cCI6MjAyNDMwODA3NX0.ogd0JYJzbJoMHxu_jexMcOMBQJc1CwOLXN5H5rBvRq4"
    
    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }
    
    // Cliente HTTP para fazer requests diretos ao Supabase
    val client = httpClient
    
    // URLs para diferentes endpoints
    val authUrl = "$SUPABASE_URL/auth/v1"
    val restUrl = "$SUPABASE_URL/rest/v1"
    
    // Headers padrão para autenticação
    val defaultHeaders = mapOf(
        "apikey" to SUPABASE_KEY,
        "Authorization" to "Bearer $SUPABASE_KEY",
        "Content-Type" to "application/json"
    )
}
