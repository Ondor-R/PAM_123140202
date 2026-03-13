package com.example.myfirstkmpapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import myfirstkmpapp.composeapp.generated.resources.Res
import myfirstkmpapp.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    MaterialTheme {                          // Material Design theme
        var showContent by remember {         // State management
            mutableStateOf(false)
        }
        Column(                               // Vertical layout
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hello, Reyhan Oktavian Putra!")
            Text("NIM: 123140202")// Text component

            Button(                           // Button component
                onClick = { showContent = !showContent }
            ) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) { // Animation
                Text("Platform: ${getPlatform()}")
            }
        }
    }
}