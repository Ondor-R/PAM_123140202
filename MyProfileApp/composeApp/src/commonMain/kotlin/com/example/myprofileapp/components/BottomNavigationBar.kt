package com.example.myprofileapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myprofileapp.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Notes to Icons.Default.List,
        Screen.Favorites to Icons.Default.Favorite,
        Screen.Profile to Icons.Default.Person
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute in listOf(Screen.Notes.route, Screen.Favorites.route, Screen.Profile.route)) {
        NavigationBar {
            items.forEach { (screen, icon) ->
                NavigationBarItem(
                    icon = { Icon(icon, contentDescription = screen.route) },
                    label = { Text(screen.route.replaceFirstChar { it.uppercase() }) },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}