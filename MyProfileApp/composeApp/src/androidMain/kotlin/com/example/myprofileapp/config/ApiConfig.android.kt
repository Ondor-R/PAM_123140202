package com.example.myprofileapp.config

import com.example.myprofileapp.BuildConfig

actual object ApiConfig {
    actual val geminiApiKey: String = BuildConfig.GEMINI_API_KEY
}