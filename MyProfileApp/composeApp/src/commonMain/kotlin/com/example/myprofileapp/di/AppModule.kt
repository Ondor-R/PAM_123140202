package com.example.myprofileapp.di

import com.example.myprofileapp.data.ProfileRepository
import com.example.myprofileapp.viewmodel.ProfileViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val commonModule = module {
    single { ProfileRepository(get(), get()) }
    factory { ProfileViewModel(get()) }
}

val appModules = listOf(commonModule, platformModule)