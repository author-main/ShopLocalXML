package com.example.shoplocalxml

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.shoplocalxml.repository.Repository
import com.example.shoplocalxml.ui.login.LoginViewModel

class FactoryViewModel(
    owner: SavedStateRegistryOwner,
    private val repository: Repository,
    private val defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return LoginViewModel(repository) as T
    }
}