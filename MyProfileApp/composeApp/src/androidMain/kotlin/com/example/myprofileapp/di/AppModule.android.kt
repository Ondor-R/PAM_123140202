package com.example.myprofileapp.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.myprofileapp.db.NoteDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.example.myprofileapp.platform.DeviceInfo
import com.example.myprofileapp.platform.NetworkMonitor
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_settings")
actual val platformModule = module {
    single<app.cash.sqldelight.db.SqlDriver> {
        AndroidSqliteDriver(
            schema = NoteDatabase.Schema,
            context = androidContext(),
            name = "profile_notes.db"
        )
    }
    single { NoteDatabase(get()) }
    single { androidContext().dataStore }
    single { DeviceInfo() }
    single { NetworkMonitor(androidContext()) }
}