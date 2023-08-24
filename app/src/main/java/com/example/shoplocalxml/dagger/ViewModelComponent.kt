package com.example.shoplocalxml.dagger

import androidx.lifecycle.ViewModel
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.ui.home.HomeViewModel
import com.example.shoplocalxml.ui.login.LoginViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass


@Subcomponent(modules = [ViewModelModule::class])
interface ViewModelComponent {
    @Subcomponent.Builder
    interface Builder {
       fun build(): ViewModelComponent
    }
}


@Module
interface ViewModelModule{
    @Binds
    @[IntoMap ViewModelKey(HomeViewModel::class)]
    fun provideHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(LoginViewModel::class)]
    fun provideLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(SharedViewModel::class)]
    fun provideSharedViewModel(sharedViewModel: SharedViewModel): ViewModel
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)