package com.example.myprofileapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myprofileapp.navigation.Screen
import com.example.myprofileapp.viewmodel.ProfileViewModel
import com.example.myprofileapp.viewmodel.NotesUiState
import androidx.compose.ui.text.font.FontWeight

@Composable
fun NotesScreen(navController: NavController, viewModel: ProfileViewModel) {
    val uiState by viewModel.notesUiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddNote.route) }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Cari Catatan...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            // UI States
            when (val state = uiState) {
                is NotesUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is NotesUiState.Empty -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(if (searchQuery.isEmpty()) "Belum ada catatan. Klik + untuk menambah." else "Catatan tidak ditemukan.")
                    }
                }
                is NotesUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                        items(state.notes) { note ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Row(
                                    Modifier.padding(16.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(Modifier.weight(1f).clickable {
                                        navController.navigate(Screen.NoteDetail.createRoute(note.id))
                                    }) {
                                        Text(note.title, style = MaterialTheme.typography.titleMedium)
                                        Text(note.content, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                                    }

                                    IconButton(onClick = { viewModel.toggleFavorite(note.id) }) {
                                        Icon(
                                            imageVector = if (note.isFavorite == 1L) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = null,
                                            tint = if (note.isFavorite == 1L) Color.Red else Color.Gray
                                        )
                                    }

                                    IconButton(onClick = { viewModel.deleteNote(note.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddNoteScreen(navController: NavController, viewModel: ProfileViewModel) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Buat Catatan Baru", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5
        )

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    viewModel.addNote(title, content)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Catatan")
        }

        TextButton(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Batal")
        }
    }
}

@Composable
fun NoteDetailScreen(navController: NavController, noteId: String?, viewModel: ProfileViewModel) {
    if (noteId == null) return

    // Mengambil data catatan secara reaktif dari database
    val note by viewModel.getNoteById(noteId).collectAsState(initial = null)

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        if (note == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            Text(note!!.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(note!!.content, style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.weight(1f)) // Mendorong tombol ke bawah

            Button(
                onClick = { navController.navigate(Screen.EditNote.createRoute(noteId)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Catatan Ini")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kembali")
            }
        }
    }
}

@Composable
fun EditNoteScreen(navController: NavController, noteId: String?, viewModel: ProfileViewModel) {
    if (noteId == null) return
    val note by viewModel.getNoteById(noteId).collectAsState(initial = null)

    // Mengisi form otomatis dengan data yang sudah ada
    var title by remember(note) { mutableStateOf(note?.title ?: "") }
    var content by remember(note) { mutableStateOf(note?.content ?: "") }

    Column(Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Edit Catatan", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Isi Catatan") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    viewModel.updateNote(noteId, title, content) // Update ke database
                    navController.popBackStack() // Kembali ke layar sebelumnya
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Perubahan")
        }

        TextButton(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Batal")
        }
    }
}