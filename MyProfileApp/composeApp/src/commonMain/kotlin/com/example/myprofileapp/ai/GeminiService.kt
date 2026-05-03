package com.example.myprofileapp.ai

import com.example.myprofileapp.config.ApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class GeminiService(private val client: HttpClient) {
    private val baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"

    suspend fun generateTitle(noteContent: String): Result<String> = safeAICall {
        val request = GeminiRequest(
            systemInstruction = Content(
                parts = listOf(
                    Part(
                        "Kamu adalah penulis judul yang singkat dan padat. " +
                                "Tugasmu: Buatkan judul untuk catatan yang diberikan. " +
                                "Aturan: Judul TIDAK BOLEH lebih dari 10 kata. Judul meringkas dari isi catatan dengan bahasa yang tidak kaku. " +
                                "Return hasil jadi judul dan bukan list atau pilihan.")
                ),
                role = "system"
            ),
            contents = listOf(
                Content(parts = listOf(Part(noteContent)))
            )
        )

        val apiKey = ApiConfig.geminiApiKey

        val httpResponse = client.post("$baseUrl?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        if (!httpResponse.status.isSuccess()) {
            val errorRaw = httpResponse.bodyAsText()
            println("GEMINI_API_ERROR: $errorRaw")
            throw Exception("Ditolak Server: ${httpResponse.status.value}")
        }

        val response: GeminiResponse = httpResponse.body()

        if (response.candidates.isNullOrEmpty()) {
            throw Exception("Teks kamu diblokir oleh sistem keamanan AI.")
        }

        response.candidates.first().content.parts.firstOrNull()?.text?.trim()
            ?: throw Exception("AI memberikan balasan kosong")
    }
}