package com.example.shoplocalxml.ui.home

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplocalxml.classes.image_downloader.ImageDownloadManager
import com.example.shoplocalxml.classes.image_downloader.ImageDownloaderImpl
import com.example.shoplocalxml.log
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Stack
import java.util.concurrent.Executors

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