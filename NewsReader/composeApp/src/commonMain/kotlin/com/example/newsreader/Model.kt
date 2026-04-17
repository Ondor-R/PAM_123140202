import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

@Serializable
data class Article(
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val url: String?,
    val content: String?
)