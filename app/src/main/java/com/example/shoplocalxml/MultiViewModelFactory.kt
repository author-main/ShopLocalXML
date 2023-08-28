package com.example.shoplocalxml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class MultiViewModelFactory @Inject constructor(
    private val viewModelFactories: Map<Class<out RepositoryViewModel>, @JvmSuppressWildcards Provider<RepositoryViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelFactories.getValue(modelClass as Class<RepositoryViewModel>).get() as T
    }

    val viewModelsClasses get() = viewModelFactories.keys
}