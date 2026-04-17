package com.example.newsreader

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform