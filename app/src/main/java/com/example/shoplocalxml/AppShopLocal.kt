package com.example.shoplocalxml

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class AppShopLocal: Application() {
    init{
        instance = this
    }
    override fun onCreate() {
        super.onCreate()
    }
    companion object {
        private lateinit var instance: AppShopLocal
        fun applicationContext(): Context =
            instance.applicationContext
    }
}


/*
app:boxBackgroundColor="@android:color/transparent"
android:background="@android:color/transparent">*/
