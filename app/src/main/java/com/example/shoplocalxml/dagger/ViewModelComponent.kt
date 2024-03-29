package com.example.shoplocalxml.dagger

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.MultiViewModelFactory
import com.example.shoplocalxml.RepositoryViewModel
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.ui.home.HomeViewModel
import com.example.shoplocalxml.ui.login.LoginViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import javax.inject.Scope
import kotlin.reflect.KClass


@[ActivityMainScope Subcomponent(modules = [ViewModelModule::class, ProviderViewModelModule::class])]
interface ViewModelComponent {
    val factory: MultiViewModelFactory
    @Subcomponent.Builder
    interface Builder {
        fun build() : ViewModelComponent
    }
    fun inject(activity: AppCompatActivity)
}

@Module
class ProviderViewModelModule {
    @Provides
    fun provideRepositoryViewModel(activity: AppCompatActivity):
            ViewModel {
        return ViewModelProvider(
            activity.viewModelStore,
            FactoryViewModel(activity)
        )[(RepositoryViewModel::class.java)]
    }
}

@Module
interface ViewModelModule{
    @Binds
    @[IntoMap ViewModelKey(HomeViewModel::class)]
    fun bindHomeViewModel(homeViewModel: HomeViewModel): RepositoryViewModel

    @Binds
    @[IntoMap ViewModelKey(LoginViewModel::class)]
    fun bindLoginViewModel(loginViewModel: LoginViewModel): RepositoryViewModel

    @Binds
    @[IntoMap ViewModelKey(SharedViewModel::class)]
    fun bindSharedViewModel(sharedViewModel: SharedViewModel): RepositoryViewModel
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityMainScope