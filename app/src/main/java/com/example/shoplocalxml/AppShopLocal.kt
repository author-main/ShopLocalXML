package com.example.shoplocalxml

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.classes.image_downloader.ImageDownloadManager
import com.example.shoplocalxml.dagger.AppComponent
import com.example.shoplocalxml.dagger.DaggerAppComponent
import com.example.shoplocalxml.repository.Repository
import javax.inject.Inject
import javax.inject.Singleton

class AppShopLocal: Application() {
    private lateinit var appComponent: AppComponent

    @Inject
    lateinit var user: User

    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var imageDownloadManager: ImageDownloadManager

    init{
        instance = this
    }
    override fun onCreate() {
        super.onCreate()
           appComponent = DaggerAppComponent.factory().create(
            CACHE_DIR,
            128,
            32
        )
        appComponent.injectApplication(this)
    }

    companion object {
        private lateinit var instance: AppShopLocal
        val appComponent: AppComponent
            get () = instance.appComponent

        val repository: Repository
            get() = instance.repository

        val userShop: User
            get() = instance.user

        val imageDownloadManager: ImageDownloadManager
            get() = instance.imageDownloadManager

        val applicationContext: Context
            get() = instance.applicationContext
    }
}