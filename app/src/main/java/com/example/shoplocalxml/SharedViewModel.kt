package com.example.shoplocalxml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SharedViewModel(private val repository: Repository): ViewModel() {
    private var processQuery = false
    var portionData = 0
    private val _products = MutableStateFlow<MutableList<Product>>(mutableListOf())
    val products = _products.asStateFlow()
    private fun setProducts(value: MutableList<Product>) {
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

    /*fun downloadImage(url: String, reduce: Boolean = true, oncomplete: (Bitmap?) -> Unit) {
        repository.downloadImage(url, reduce, oncomplete)
    }*/

    private fun updateDataAfterQuery(list: List<Product>) {
        _products.update {
            _products.value.toMutableList().apply { this.addAll(updateHostLink(list)) }
        }
    }


    fun getProducts(page: Int, order: String) {
        if (processQuery) return
        if (page <= portionData) return
        log("portion loaded...")
        processQuery = true
        //CoroutineScope(Dispatchers.Main).launch {
        viewModelScope.launch {
            repository.getProducts(page, order)?.let { products ->

                if (products.isNotEmpty()) {
                    portionData += 1
                    updateDataAfterQuery(products)
                    processQuery = false
                    //setProducts(updateHostLink(products))
                }
            }
        }
    }

    fun updateProductFavorite(idProduct: Int, value: Boolean){
        viewModelScope.launch {
            _products.value.find { it.id == idProduct }?.apply {
                favorite = if (value) 1 else 0
            }
            repository.updateProductFavorite(idProduct, value)
        }
    }

    private fun updateHostLink(list: List<Product>): List<Product>{
        val listProduct = mutableListOf<Product>()
        for (product in list) {
            val links = mutableListOf<String>()
            product.linkimages?.let{linkimages_ ->
                for (link in linkimages_) {
                    links.add(
                        "$SERVER_URL/$DIR_IMAGES/$link"
                    )
                }
            }
            listProduct.add(product.copy(linkimages = links))
        }
        return listProduct
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

        @JvmStatic fun getProductPromotion(discount: Int, sold: Int): String {
            val boundSold   = 10
            val boundAction =  7
            var textPromotion = EMPTY_STRING
            if (sold > boundSold)       textPromotion = getStringResource(R.string.text_bestseller)
            else
            if (discount > boundAction) textPromotion = getStringResource(R.string.text_action)
            else
            if (discount > 0)           textPromotion = getStringResource(R.string.text_benefit)
            return textPromotion
        }
    }

    fun getProductFromId(id: Int) =
        _products.value.find { it.id == id }

}
