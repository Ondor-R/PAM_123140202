package com.example.myprofileapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myprofileapp.data.Note
import com.example.myprofileapp.data.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun addNote(title: String, content: String) {
        val newNote = Note(
            id = Random.nextInt(1000, 9999).toString(),
            title = title,
            content = content
        )
        _uiState.update { it.copy(notes = it.notes + newNote) }
    }

    fun deleteNote(noteId: String) {
        _uiState.update { currentState ->
            currentState.copy(notes = currentState.notes.filter { it.id != noteId })
        }
    }

    fun toggleFavorite(noteId: String) {
        _uiState.update { currentState ->
            val updatedNotes = currentState.notes.map { note ->
                if (note.id == noteId) note.copy(isFavorite = !note.isFavorite) else note
            }
            currentState.copy(notes = updatedNotes)
        }
    }

    fun updateProfile(newName: String, newBio: String) {
        _uiState.update { it.copy(name = newName, bio = newBio) }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(isDarkMode = enabled) }
    }
}