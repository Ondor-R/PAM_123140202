package com.example.myprofileapp.ai

import io.ktor.client.plugins.ClientRequestException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

sealed class AIError(override val message: String) : Exception(message) {
    data class RateLimited(val retryAfter: Int) : AIError("Too many request. Try again later gang.")
    data class Unauthorized(override val message: String) : AIError(message)
    data class ServerError(override val message: String) : AIError(message)
    data class NetworkError(override val message: String) : AIError(message)
    data class ParseError(override val message: String) : AIError(message)
}

suspend fun <T> safeAICall(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: ClientRequestException) {
        when (e.response.status.value) {
            401 -> Result.failure(AIError.Unauthorized("Invalid API key"))
            429 -> Result.failure(AIError.RateLimited(60))
            in 500..599 -> Result.failure(AIError.ServerError("Busy Server"))
            else -> Result.failure(e)
        }
    } catch (e: IOException) {
        Result.failure(AIError.NetworkError("Check ur internet connection"))
    } catch (e: SerializationException) {
        Result.failure(AIError.ParseError("Failed to process data from AI"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}