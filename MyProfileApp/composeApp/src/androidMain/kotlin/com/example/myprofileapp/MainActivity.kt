package com.example.myprofileapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.myprofileapp.db.NoteDatabase

// Membuat instance DataStore khusus untuk platform Android
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // 1. Inisialisasi Driver SQLDelight
        val driver = AndroidSqliteDriver(
            schema = NoteDatabase.Schema,
            context = applicationContext,
            name = "profile_notes.db"
        )
        val database = NoteDatabase(driver)

        setContent {
            // 2. Kirim database dan dataStore ke pintu utama App()
            App(
                database = database,
                dataStore = applicationContext.dataStore
            )
        }
    }
}