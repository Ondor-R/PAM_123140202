package com.example.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myprofileapp.data.ProfileRepository
import com.example.myprofileapp.db.NoteEntity
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

sealed class NotesUiState {
    object Loading : NotesUiState()
    object Empty : NotesUiState()
    data class Success(val notes: List<NoteEntity>) : NotesUiState()
}

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    val isDarkMode = repository.isDarkMode.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    val name = repository.profileName.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    val bio = repository.profileBio.stateIn(viewModelScope, SharingStarted.Eagerly, "")
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val notesUiState: StateFlow<NotesUiState> = _searchQuery
        .flatMapLatest { query -> repository.getNotes(query) }
        .map { if (it.isEmpty()) NotesUiState.Empty else NotesUiState.Success(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesUiState.Loading)

    val favoriteNotes = repository.getFavoriteNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) { _searchQuery.value = query }
    fun toggleDarkMode(isDark: Boolean) = viewModelScope.launch { repository.updateTheme(isDark) }
    fun updateProfile(newName: String, newBio: String) = viewModelScope.launch { repository.updateProfile(newName, newBio) }

    fun addNote(title: String, content: String) = viewModelScope.launch {
        val id = Clock.System.now().toEpochMilliseconds().toString()
        repository.saveNote(id, title, content)
    }

    fun deleteNote(id: String) = viewModelScope.launch { repository.deleteNote(id) }
    fun toggleFavorite(id: String) = viewModelScope.launch { repository.toggleFavorite(id) }

    fun getNoteById(id: String): Flow<NoteEntity?> = repository.getNoteById(id)

    fun updateNote(id: String, title: String, content: String) = viewModelScope.launch {
        repository.updateNote(id, title, content)
    }
}