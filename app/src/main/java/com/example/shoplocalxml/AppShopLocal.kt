package com.example.shoplocalxml

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.shoplocalxml.repository.Repository

class AppShopLocal: Application() {
    private val repository = Repository()
    init{
        instance = this
    }
    override fun onCreate() {
        super.onCreate()
    }
    companion object {
        private lateinit var instance: AppShopLocal
        val repository: Repository
            get() = instance.repository
        val applicationContext: Context
            get() = instance.applicationContext
    }
}


/*
app:boxBackgroundColor="@android:color/transparent"
android:background="@android:color/transparent">*/
