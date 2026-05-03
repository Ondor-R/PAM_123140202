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

val commonModule = module {
    single { ProfileRepository(get(), get()) }

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

    factory { ProfileViewModel(get(), get()) }
}

val appModules = listOf(commonModule, platformModule)