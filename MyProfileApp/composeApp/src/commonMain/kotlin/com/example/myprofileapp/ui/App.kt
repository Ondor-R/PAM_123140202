package com.example.myprofileapp

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myprofileapp.viewmodel.ProfileViewModel
import org.jetbrains.compose.resources.painterResource
import myprofileapp.composeapp.generated.resources.Res
import myprofileapp.composeapp.generated.resources.profilepict
import myprofileapp.composeapp.generated.resources.boxbg

@Composable
fun ProfileHeader(name: String, onEditClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(Res.drawable.profilepict),
            contentDescription = "Gambar Profile",
            modifier = Modifier.size(50.dp).clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = name,
            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = onEditClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00b7d4))
        ) {
            Text("Edit")
        }
    }
}

@Composable
fun HelloItem() {
    Box(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.boxbg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))
        Text(
            text = "Hello There!",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun InfoItem(bio: String) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = bio)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Email: reyhan123140202@student.itera.ac.id", fontSize = 14.sp)
        Text(text = "Phone: 082312313131", fontSize = 14.sp)
        Text(text = "Location: Bandar Lampung", fontSize = 14.sp)
    }
}

@Composable
fun EditProfileForm(
    currentName: String,
    currentBio: String,
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var nameInput by remember { mutableStateOf(currentName) }
    var bioInput by remember { mutableStateOf(currentBio) }

    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Edit Profile", style = MaterialTheme.typography.titleLarge)
        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = bioInput,
            onValueChange = { bioInput = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onCancel) { Text("Batal") }
            Button(onClick = { onSave(nameInput, bioInput) }) { Text("Simpan") }
        }
    }
}

@Composable
fun App(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var isEditing by remember { mutableStateOf(false) }

    val colorScheme = if (uiState.isDarkMode) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Dark Mode")
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = uiState.isDarkMode,
                        onCheckedChange = { viewModel.toggleDarkMode(it) }
                    )
                }

                if (isEditing) {
                    EditProfileForm(
                        currentName = uiState.name,
                        currentBio = uiState.bio,
                        onSave = { n, b ->
                            viewModel.updateProfile(n, b)
                            isEditing = false
                        },
                        onCancel = { isEditing = false }
                    )
                } else {
                    ProfileHeader(name = uiState.name, onEditClick = { isEditing = true })
                    HelloItem()
                    InfoItem(bio = uiState.bio)
                }
            }
        }
    }
}