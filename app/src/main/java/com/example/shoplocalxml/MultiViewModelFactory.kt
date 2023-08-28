package com.example.shoplocalxml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.dagger.ActivityMainScope
import javax.inject.Inject
import javax.inject.Provider

@ActivityMainScope
class MultiViewModelFactory @Inject constructor(
    private val viewModelFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<RepositoryViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelFactories.getValue(modelClass as Class<ViewModel>).get() as T
    }

    val viewModelsClasses get() = viewModelFactories.keys
}