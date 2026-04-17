import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class NewsRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private val apiKey = "API-Disni" // api keyny disni
    private val baseUrl = "https://newsapi.org/v2/top-headlines?country=us&apiKey=$apiKey"

    suspend fun getTopHeadlines(): Result<List<Article>> {
        return try {
            val response: NewsResponse = client.get(baseUrl).body()
            Result.success(response.articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}