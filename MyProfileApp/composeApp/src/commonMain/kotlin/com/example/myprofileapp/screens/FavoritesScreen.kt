package com.example.myprofileapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myprofileapp.viewmodel.ProfileViewModel

@Composable
fun FavoritesScreen(viewModel: ProfileViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val favoriteNotes = uiState.notes.filter { it.isFavorite }

    if (favoriteNotes.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada favorit.")
        }
    } else {
        LazyColumn(Modifier.padding(16.dp)) {
            items(favoriteNotes) { note ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text(note.title, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}