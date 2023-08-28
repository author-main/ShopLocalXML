package com.example.shoplocalxml

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.shoplocalxml.repository.Repository
import com.example.shoplocalxml.ui.home.HomeViewModel
import com.example.shoplocalxml.ui.login.LoginViewModel
import java.lang.IllegalArgumentException

class FactoryViewModel(
    private val owner: SavedStateRegistryOwner/*,
    private val repository: Repository? = null,
    private val defaultArgs: Bundle? = null*/
) : AbstractSavedStateViewModelFactory(owner, defaultArgs = null) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return when (modelClass) {
            LoginViewModel::class.java  ->
                //LoginViewModel(repository!!) as T
                LoginViewModel() as T
            SharedViewModel::class.java -> {
                SharedViewModel() as T
                //SharedViewModel(repository!!) as T
            }
            HomeViewModel::class.java -> {
                HomeViewModel() as T
            }
            else ->
                throw IllegalArgumentException()
            //super.create(modelClass)
        }
    }

}
