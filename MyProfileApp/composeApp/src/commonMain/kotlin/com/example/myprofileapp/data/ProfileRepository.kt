package com.example.myprofileapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.myprofileapp.db.NoteDatabase
import com.example.myprofileapp.db.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import app.cash.sqldelight.coroutines.mapToOneOrNull

class ProfileRepository(
    db: NoteDatabase,
    private val dataStore: DataStore<Preferences>
) {
    private val queries = db.noteEntityQueries
    
    private val THEME_KEY = booleanPreferencesKey("is_dark_mode")
    private val NAME_KEY = stringPreferencesKey("profile_name")
    private val BIO_KEY = stringPreferencesKey("profile_bio")

    val isDarkMode: Flow<Boolean> = dataStore.data.map { it[THEME_KEY] ?: false }
    val profileName: Flow<String> = dataStore.data.map { it[NAME_KEY] ?: "Reyhan Oktavian Putra" }
    val profileBio: Flow<String> = dataStore.data.map { it[BIO_KEY] ?: "Mahasiswa Institut Teknologi Sumatera" }

    suspend fun updateTheme(isDark: Boolean) { dataStore.edit { it[THEME_KEY] = isDark } }
    suspend fun updateProfile(name: String, bio: String) {
        dataStore.edit {
            it[NAME_KEY] = name
            it[BIO_KEY] = bio
        }
    }

    fun getNotes(query: String = ""): Flow<List<NoteEntity>> {
        return if (query.isBlank()) {
            queries.getAllNotes().asFlow().mapToList(Dispatchers.IO)
        } else {
            queries.searchNotes(query).asFlow().mapToList(Dispatchers.IO)
        }
    }

    fun getFavoriteNotes(): Flow<List<NoteEntity>> = queries.getFavorites().asFlow().mapToList(Dispatchers.IO)

    suspend fun saveNote(id: String, title: String, content: String, isFav: Boolean = false) {
        queries.insertNote(id, title, content, if (isFav) 1 else 0, Clock.System.now().toEpochMilliseconds())
    }

    fun getNoteById(id: String): Flow<NoteEntity?> {
        return queries.getNoteById(id).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    suspend fun updateNote(id: String, title: String, content: String) {
        queries.updateNote(title, content, id)
    }

    suspend fun deleteNote(id: String) = queries.deleteNote(id)
    suspend fun toggleFavorite(id: String) = queries.toggleFavorite(id)
}