package com.example.shoplocalxml

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.annotation.EmptySuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

class SharedViewModel(private val repository: Repository): ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(listOf())
    val products = _products.asStateFlow()
    private fun setProducts(value: List<Product>) {
        _products.value = value
    }

    fun getListBrend(){
        viewModelScope.launch(Dispatchers.IO) {
            listBrend =  repository.getBrends() ?: listOf()
        }
    }

    private var onCloseApp: (() -> Unit)? = null
    fun setOnCloseApp(value: () -> Unit) {
        onCloseApp = value
    }

    fun closeApp() {
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

    fun downloadImage(url: String, reduce: Boolean = true, oncomplete: (Bitmap?) -> Unit) {
        repository.downloadImage(url, reduce, oncomplete)
    }

    fun getProducts(page: Int, order: String) {
        //CoroutineScope(Dispatchers.Main).launch {
        viewModelScope.launch {
            repository.getProducts(page, order)?.let { products ->
                setProducts(products)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    companion object {
        private var listBrend = listOf<Brend>()
        @JvmStatic fun getProductBrend(id: Int) =
            listBrend.find { it.id == id }?.name ?: EMPTY_STRING

        @JvmStatic fun getProductSalePrice(value: Float, discount: Int) =
            getFormattedFloat(value - value * discount / 100f)

        @JvmStatic fun getProductPrice(value: Float) =
            getFormattedFloat(value)
    }

}
