package com.example.tracklift_asa.data

import io.ktor.client.*
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object SupabaseClient {
    const val SUPABASE_URL = "https://ivhaapbvtqgcefgoidxt.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Iml2aGFhcGJ2dHFnY2VmZ29pZHh0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTQ2NjM3NDAsImV4cCI6MjAzMDIzOTc0MH0.2Yv9W24-n_zF4O9zQhY_3c4-r3r_3u_z3gC_3Yv9W24"

    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            header("apikey", SUPABASE_KEY)
            header("Authorization", "Bearer $SUPABASE_KEY")
            contentType(ContentType.Application.Json)
        }
    }
}
