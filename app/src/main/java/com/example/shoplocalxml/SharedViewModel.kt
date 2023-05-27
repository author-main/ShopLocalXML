package com.example.shoplocalxml

import androidx.annotation.EmptySuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplocalxml.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count

class SharedViewModel(private val repository: Repository): ViewModel() {

   /* private val _querySearch = MutableLiveData<String>(EMPTY_STRING)
    val querySearch: LiveData<String> = _querySearch
    fun setQuerySearch(value: String){
        _querySearch.value = value
    }*/


    private var onCloseApp: (() -> Unit)? = null
    fun setOnCloseApp(value:() -> Unit ) {
        onCloseApp = value
    }
    fun closeApp(){
        onCloseApp?.invoke()
    }


    fun getSearchHistoryItems(): List<String> =
        repository.getSearchHistoryItems()

    fun addSearchHistoryItem(value: String) {
        repository.addSearchHistoryItem(value)
    }

    fun deleteSearchHistoryItem(value: String) {
        repository.deleteSearchHistoryItem(value)
    }

    fun saveSearchHistory() {
        repository.saveSearchHistory()
    }

    fun clearSearchHistory() {
        repository.clearSearchHistory()
    }

}