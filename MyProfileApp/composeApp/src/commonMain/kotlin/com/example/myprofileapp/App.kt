package com.example.myprofileapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.myprofileapp.data.ProfileRepository
import com.example.myprofileapp.db.NoteDatabase
import com.example.myprofileapp.navigation.AppNavigation
import com.example.myprofileapp.viewmodel.ProfileViewModel
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    KoinContext {
        val viewModel = koinViewModel<ProfileViewModel>()
        val isDarkMode by viewModel.isDarkMode.collectAsState()
        val colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()

        MaterialTheme(colorScheme = colorScheme) {
            Surface(modifier = Modifier.fillMaxSize()) {
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}