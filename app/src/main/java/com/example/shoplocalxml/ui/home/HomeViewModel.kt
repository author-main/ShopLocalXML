package com.example.shoplocalxml.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplocalxml.EMPTY_STRING
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.sort_filter.Filter
import com.example.shoplocalxml.classes.sort_filter.SortOrder
import java.lang.Exception
import java.util.Stack

class HomeViewModel : ViewModel() {
data class DataMode(var sort: SortOrder, var filter: Filter, var portionData: Int, var products: List<Product>, var scrollPosition: Int)
    var searchQuery = EMPTY_STRING
    private val hashDataMode = hashMapOf<HomeMode, DataMode>()
    fun saveData(mode: HomeMode, sort: SortOrder, filter: Filter, portionData: Int, products: List<Product>, scrollPosition: Int){
        if (hashDataMode[mode] == null)
            hashDataMode[mode] = DataMode(sort, filter, portionData, products, scrollPosition)
    }

    fun getData(mode: HomeMode): DataMode? =
        hashDataMode[mode]

    fun removeData(mode: HomeMode) {
        hashDataMode.remove(mode)
    }

    private val stackMode = Stack<HomeMode>().apply {
        push(HomeMode.MAIN)
    }
    private val _modeFragment = MutableLiveData<HomeMode>(HomeMode.MAIN)
    val modeFragment: LiveData<HomeMode> = _modeFragment

  /*  fun getStackMode(): HomeMode{
        return try {
        //    stackMode.lastElement()
            stackMode.peek()
        } catch(_: Exception){
            HomeMode.NULL
        }
    }*/

    fun popStackMode(): HomeMode{
        try {
            stackMode.pop()
            _modeFragment.value = stackMode.lastElement()
        } catch(_: Exception){
            _modeFragment.value = HomeMode.NULL
        }
        return _modeFragment.value!!
    }
    fun pushStackMode(value: HomeMode) {
        if (value == _modeFragment.value)
            return
        _modeFragment.value = value
        if (value == HomeMode.SEARCH_RESULT) {
            stackMode.remove(HomeMode.SEARCH_QUERY)
            stackMode.remove(HomeMode.SEARCH_RESULT)
            stackMode.push(HomeMode.SEARCH_RESULT)
        } else {
            stackMode.push(value)
        }
    }
    fun currentMode() = stackMode.peek()

    /*private fun setOrderQuery(value: String) {
        orderQuery = value
    }*/

    override fun onCleared() {
        hashDataMode.clear()
        stackMode.clear()
        super.onCleared()
    }

    companion object {
        enum class HomeMode {
            MAIN,
            SEARCH_QUERY,
            SEARCH_RESULT,
            PRODUCT_DETAIL,
            NULL
        }
    }
}