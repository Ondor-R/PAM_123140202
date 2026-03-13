package com.example.myprofileapp

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

import myprofileapp.composeapp.generated.resources.Res
import myprofileapp.composeapp.generated.resources.compose_multiplatform

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import myprofileapp.composeapp.generated.resources.profilepict
import myprofileapp.composeapp.generated.resources.boxbg


@Composable
fun ProfileHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Image (
            painter = painterResource(Res.drawable.profilepict),
            contentDescription = "Gambar Profile",
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Reyhan Oktavian Putra",
            modifier = Modifier.weight(1f)
        )
        Button(onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00b7d4)
            )) {
            Text("Follow")
        }
    }
}

@Composable
fun HelloItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.boxbg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        )
        Text(
            text = "Hello There!",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InfoItem() {
   Column(
       modifier = Modifier
           .fillMaxWidth()
           .padding(20.dp),
       verticalArrangement =
           Arrangement.spacedBy(8.dp),
       horizontalAlignment =
           Alignment.Start
   ) {
       Text(text = "Mahasiswa Institut Teknologi Sumatera dengan NIM 123140202 dari prodi IF.")
       Spacer(modifier = Modifier.height(16.dp))
       Text(text = "Email: reyhan123140202@student.itera.ac.id")
       Text(text = "Phone: 082312313131")
       Text(text = "Location: Bandar Lampung")
   }
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileHeader()
            HelloItem()
            InfoItem()
        }
    }
}