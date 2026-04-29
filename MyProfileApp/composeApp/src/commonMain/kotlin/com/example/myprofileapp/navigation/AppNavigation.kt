package com.example.myprofileapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myprofileapp.components.BottomNavigationBar
import com.example.myprofileapp.screens.*
import com.example.myprofileapp.viewmodel.ProfileViewModel
import com.example.myprofileapp.components.NetworkStatusIndicator

@Composable
fun AppNavigation(viewModel: ProfileViewModel = viewModel()) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {NetworkStatusIndicator()},
        bottomBar = {BottomNavigationBar(navController = navController)}
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Notes.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Notes.route) { NotesScreen(navController, viewModel) }
            composable(Screen.Favorites.route) { FavoritesScreen(viewModel) }
            composable(Screen.Profile.route) { ProfileScreen(viewModel) }
            composable(Screen.Settings.route) { SettingsScreen() }

            composable(
                route = Screen.NoteDetail.route,
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId")
                NoteDetailScreen(navController, noteId, viewModel)
            }

            composable(Screen.AddNote.route) { AddNoteScreen(navController, viewModel) }

            composable(
                route = Screen.EditNote.route,
                arguments = listOf(navArgument("noteId") { type = NavType.StringType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getString("noteId")
                EditNoteScreen(navController, noteId, viewModel)
            }
        }
    }
}