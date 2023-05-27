package com.example.shoplocalxml.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplocalxml.log
import java.util.Stack

class HomeViewModel : ViewModel() {
    private val stackMode = Stack<HomeMode>().apply {
        push(HomeMode.MAIN)
    }
    private val _modeSearchProduct = MutableLiveData<HomeMode>(HomeMode.MAIN)
    val modeSearchProduct: LiveData<HomeMode> = _modeSearchProduct
    fun setModeSearchProduct(value: HomeMode) {
        _modeSearchProduct.value = value
        //log("last = ${stackMode.lastElement()}")
        val stackEntity = stackMode.find { it == value }
        stackEntity?.let {
            if (it == HomeMode.SEARCH_QUERY) {
                stackMode.remove(HomeMode.SEARCH_QUERY)
                stackMode.push(HomeMode.SEARCH_QUERY)
            }
        } ?: run {
            stackMode.push(value)
        }
        log(stackMode)
    }

    companion object {
        enum class HomeMode {
            MAIN,
            SEARCH_QUERY,
            SEARCH_RESULT
        }
    }
}