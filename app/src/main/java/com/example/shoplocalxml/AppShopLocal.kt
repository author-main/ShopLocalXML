package com.example.shoplocalxml

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.shoplocalxml.dagger.AppComponent
import com.example.shoplocalxml.dagger.DaggerAppComponent
import com.example.shoplocalxml.repository.Repository

class AppShopLocal: Application() {
    private lateinit var appComponent: AppComponent
    private lateinit var repository: Repository
    init{
        instance = this
    }
    override fun onCreate() {
        super.onCreate()
        repository = Repository()
        appComponent = DaggerAppComponent.create()
            /*.factory().create(
                applicationInfo.dataDir + "/cache/",
                8
            )*/
        appComponent.injectApplication(this)

    }
    companion object {
        private lateinit var instance: AppShopLocal
        val appComponent: AppComponent
            get () = instance.appComponent
        val repository: Repository
            get() = instance.repository
        val applicationContext: Context
            get() = instance.applicationContext
    }
}

/*
app:boxBackgroundColor="@android:color/transparent"
android:background="@android:color/transparent">*/
