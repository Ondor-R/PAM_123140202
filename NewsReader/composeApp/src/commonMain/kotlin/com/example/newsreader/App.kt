package com.example.newsreader

import NewsViewModel
import NewsScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun App() {
    MaterialTheme {
        val viewModel = remember { NewsViewModel() }

        NewsScreen(viewModel = viewModel)
    }
}