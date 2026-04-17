import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Article>>> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getTopHeadlines()
                .onSuccess { articles ->
                    val validArticles = articles.filter { it.title != "[Removed]" && it.title != null }
                    _uiState.value = UiState.Success(validArticles)
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Terjadi kesalahan yang tidak diketahui")
                }
        }
    }

    fun refresh() = loadNews()
}