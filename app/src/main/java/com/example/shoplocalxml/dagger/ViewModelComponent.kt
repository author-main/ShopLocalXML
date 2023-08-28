package com.example.shoplocalxml.dagger

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.MainActivity
import com.example.shoplocalxml.MultiViewModelFactory
import com.example.shoplocalxml.RepositoryViewModel
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.repository.Repository
import com.example.shoplocalxml.ui.home.HomeViewModel
import com.example.shoplocalxml.ui.login.LoginViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.MapKey
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import dagger.multibindings.Multibinds
import kotlin.reflect.KClass


@Subcomponent(modules = [ViewModelModule::class])//, ProviderViewModel::class])
interface ViewModelComponent {
    //val factory: MultiViewModelFactory

    /*@Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity) : ViewModelComponent
    }*/

    @Subcomponent.Builder
    interface Builder {
        fun build() : ViewModelComponent
    }
    fun inject(activity: AppCompatActivity)
}


/*@Module
class ProviderViewModel {
    @[Provides]
    fun provideRepositoryViewModel(activity: AppCompatActivity):
            ViewModel {
        return ViewModelProvider(
            activity.viewModelStore,
            FactoryViewModel(activity)
        )[(ViewModel::class.java)]
    }
}*/

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
annotation class ViewModelKey(val value: KClass<out RepositoryViewModel>)

