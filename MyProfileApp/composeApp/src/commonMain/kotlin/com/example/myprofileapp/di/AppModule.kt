package com.example.myprofileapp.di

import com.example.myprofileapp.data.ProfileRepository
import com.example.myprofileapp.viewmodel.ProfileViewModel
import com.example.myprofileapp.ai.GeminiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

//Data Module (Khusus Repository & Database)
val dataModule = module {
    single { ProfileRepository(get(), get()) }
}

//Network/AI Module (Khusus API & Ktor)
val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }
    single { GeminiService(get()) }
}

//ViewModel Module (Khusus UI State Holders)
val viewModelModule = module {
    factory { ProfileViewModel(get(), get()) }
}

val appModules = listOf(dataModule, networkModule, viewModelModule, platformModule)