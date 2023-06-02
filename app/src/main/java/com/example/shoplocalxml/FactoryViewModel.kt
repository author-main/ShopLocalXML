package com.example.shoplocalxml

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.shoplocalxml.repository.Repository
import com.example.shoplocalxml.ui.login.LoginViewModel

class FactoryViewModel(
    private val owner: SavedStateRegistryOwner,
    private val repository: Repository,
    private val defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {

      /*  val model = when (modelClass) {
            LoginViewModel::class.java  ->
                LoginViewModel(repository) as T
            SharedViewModel::class.java ->
                SharedViewModel(repository) as T
            else -> throw IllegalArgumentException()
            }

        return if (hashMap.contains(key))
            hashMap[key] as T
        else {
            hashMap[key] = model
            hashMap[key] as T
        }*/

        return when (modelClass) {
            LoginViewModel::class.java  ->
                LoginViewModel(repository) as T
            SharedViewModel::class.java -> {
                SharedViewModel(repository) as T
            }
            else ->
                throw IllegalArgumentException()
                    //super.create(modelClass)
        }
    }

   /* companion object {
        val hashMap = hashMapOf<String, ViewModel>()
    }*/
}