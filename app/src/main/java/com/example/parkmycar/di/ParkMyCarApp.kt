package com.example.parkmycar.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ParkMyCarApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}