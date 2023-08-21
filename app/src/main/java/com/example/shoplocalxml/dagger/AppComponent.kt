package com.example.shoplocalxml.dagger

import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.SERVER_URL
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.classes.image_downloader.ImageCacheDrive
import com.example.shoplocalxml.classes.image_downloader.ImageCacheDriveImpl
import com.example.shoplocalxml.classes.image_downloader.ImageCacheMemory
import com.example.shoplocalxml.classes.image_downloader.ImageCacheMemoryImpl
import com.example.shoplocalxml.repository.database_api.DatabaseApiImpl
import com.example.shoplocalxml.repository.database_handler.DatabaseHandler
import com.example.shoplocalxml.repository.database_handler.DatabaseHandlerImpl
import com.example.shoplocalxml.ui.history_search.SearchQueryStorage
import com.example.shoplocalxml.ui.history_search.SearchQueryStorageInterface
import com.example.shoplocalxml.ui.login.access_handler.AccessHandler
import com.example.shoplocalxml.ui.login.access_handler.AccessHandlerImpl
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Scope

@[AppScope Component(modules = [DatabaseModule::class, BindsModule::class])]
interface AppComponent {
    fun injectApplication(appShopLocal: AppShopLocal)
    @Component.Factory
    interface ImageCache{
        //fun create(@[BindsInstance PathCache] pathDriveCache: String,
        fun create( @BindsInstance pathDriveCache: String,
                   @[BindsInstance DriveCacheSize] driveCacheSize: Int,
                   @[BindsInstance MemoryCacheSize] memoryCacheSize: Int
        ): AppComponent
    }
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


    @[Provides AppScope]
    fun provideUserShop(): User =
        User().apply { getUserData() }

    @[Provides]
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


  /*  @[Provides]
    fun provideAccessHandlerImpl(databaseApiImpl: DatabaseApiImpl): AccessHandlerImpl {
        return AccessHandlerImpl(databaseApiImpl)
    }

    @[Singleton Provides]
    fun provideDatabaseHandlerImpl(databaseApiImpl: DatabaseApiImpl): DatabaseHandlerImpl {
        return DatabaseHandlerImpl(databaseApiImpl)
    }*/
}

@Module
interface BindsModule {
    @Binds
    fun bindAccessHandlerImpltoAccesHandler(accessHandlerImpl: AccessHandlerImpl): AccessHandler

    @Binds
    fun bindDatabaseHandlerImpltoDatabaseHandler(databaseHandlerImpl: DatabaseHandlerImpl): DatabaseHandler

    @Binds
    fun bindSearchQueryStorageImpltoSearchQueryStorage(searchQueryStorageImpl: SearchQueryStorage): SearchQueryStorageInterface

    @Binds
    fun bindImageCacheMemoryImpltoImageCacheMemory(imageCacheMemoryImpl: ImageCacheMemoryImpl): ImageCacheMemory

    @Binds
    fun bindImageCacheDriveImpltoImageCacheDrive(imageCacheDriveImpl: ImageCacheDriveImpl): ImageCacheDrive
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

/*@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PathCache*/

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DriveCacheSize

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MemoryCacheSize