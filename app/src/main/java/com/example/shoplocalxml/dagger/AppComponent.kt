package com.example.shoplocalxml.dagger

import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.SERVER_URL
import com.example.shoplocalxml.repository.database_api.DatabaseApi
import com.example.shoplocalxml.repository.database_api.DatabaseApiImpl
import com.example.shoplocalxml.repository.database_handler.DatabaseHandler
import com.example.shoplocalxml.repository.database_handler.DatabaseHandlerImpl
import com.example.shoplocalxml.ui.login.access_handler.AccessHandler
import com.example.shoplocalxml.ui.login.access_handler.AccessHandlerImpl
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@[Singleton Component(modules = [DatabaseModule::class, BindsModule::class])]
interface AppComponent {
    fun injectApplication(appShopLocal: AppShopLocal)
}

@Module
class DatabaseModule {
 /*   @[Singleton Provides]
    fun provideDatabaseApi(): DatabaseApi {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create()
    }*/

    @[Singleton Provides]
    fun provideDatabaseApiImpl(): DatabaseApiImpl {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return DatabaseApiImpl(retrofit.create())
    }


    @[Singleton Provides]
    fun provideAccessHandlerImpl(databaseApiImpl: DatabaseApiImpl): AccessHandlerImpl {
        return AccessHandlerImpl(databaseApiImpl)
    }

    @[Singleton Provides]
    fun provideDatabaseHandlerImpl(databaseApiImpl: DatabaseApiImpl): DatabaseHandlerImpl {
        return DatabaseHandlerImpl(databaseApiImpl)
    }
}

@Module
interface BindsModule {
    @Binds
    fun bind_AccessHandlerImpl_to_AccesHandler(accessHandlerImpl: AccessHandlerImpl): AccessHandler

    @Binds
    fun bind_DatabaseHandlerImpl_to_DatabaseHandler(databaseHandlerImpl: DatabaseHandlerImpl): DatabaseHandler
}