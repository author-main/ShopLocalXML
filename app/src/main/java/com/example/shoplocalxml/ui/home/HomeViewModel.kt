package com.example.shoplocalxml.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplocalxml.log
import java.lang.Exception
import java.util.Stack

class HomeViewModel : ViewModel() {
    private val stackMode = Stack<HomeMode>().apply {
        push(HomeMode.MAIN)
    }
    private val _modeSearchProduct = MutableLiveData<HomeMode>(HomeMode.MAIN)
    val modeSearchProduct: LiveData<HomeMode> = _modeSearchProduct
    fun popStackMode(): HomeMode{
        try {
            stackMode.pop()
            _modeSearchProduct.value = stackMode.lastElement()
        } catch(_: Exception){
            _modeSearchProduct.value = HomeMode.NULL
        }
        return _modeSearchProduct.value!!
    }
    fun pushStackMode(value: HomeMode) {
        if (value == _modeSearchProduct.value)
            return
        _modeSearchProduct.value = value
        if (value == HomeMode.SEARCH_RESULT) {
            stackMode.remove(HomeMode.SEARCH_QUERY)
            stackMode.remove(HomeMode.SEARCH_RESULT)
            stackMode.push(HomeMode.SEARCH_RESULT)
        } else {
            stackMode.push(value)
        }
        //log(stackMode)
    }

    companion object {
        enum class HomeMode {
            MAIN,
            SEARCH_QUERY,
            SEARCH_RESULT,
            NULL
        }
    }
}