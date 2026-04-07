package com.example.myprofileapp.data

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val isFavorite: Boolean = false
)

data class ProfileUiState(
    val name: String = "Reyhan Oktavian Putra",
    val bio: String = "Mahasiswa Institut Teknologi Sumatera",
    val isDarkMode: Boolean = false,
    // Note list kosong secara default
    val notes: List<Note> = emptyList()
)