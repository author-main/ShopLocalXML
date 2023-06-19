package com.example.shoplocalxml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.sort_filter.Filter
import com.example.shoplocalxml.classes.sort_filter.Order
import com.example.shoplocalxml.classes.sort_filter.Sort
import com.example.shoplocalxml.classes.sort_filter.SortOrder
import com.example.shoplocalxml.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SharedViewModel(private val repository: Repository): ViewModel() {
    var sortProduct        = SortOrder()
    var filterProduct      = Filter().apply { discount = 0 }
    private var queryOrder = getQueryOrder()
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

    private fun updateDataAfterQuery(list: List<Product>, uploadAgain: Boolean) {
            if (uploadAgain)
                _products.value = list.toMutableList()
            else {
                val newList =
                    _products.value.toMutableList().apply {
                        addAll(updateHostLink(list))
                    }
                _products.value = newList
            }

        //_products.value = newList
        /*_products.update {
            _products.value.toMutableList().apply { this.addAll(updateHostLink(list)) }
        }*/
    }


    fun getProducts(page: Int, uploadAgain: Boolean  = false){//}, order: String) {
        if (processQuery) return
        if (uploadAgain) portionData = 0
        if (page <= portionData) return
        processQuery = true
        //CoroutineScope(Dispatchers.Main).launch {
        viewModelScope.launch {
            repository.getProducts(page, queryOrder)?.let { products ->
                if (products.isNotEmpty()) {
                    portionData += 1
                    updateDataAfterQuery(products, uploadAgain)
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

    private fun getQueryOrder(): String {
        val sortorder          = sortProduct.order.value
        val sorttype           = sortProduct.sort.value
        val filtercategory     = filterProduct.category
        val filterbrend        = filterProduct.brend
        val filterfavorite     = filterProduct.favorite
        val filterprice        = run {
            val value: Pair<Float, Float>   = filterProduct.priceRange
            "${value.first}-${value.second}"
        }
        val filterdiscount = filterProduct.discount
        val filterscreen   = 0

        /** Порядок для извлечения в PHP:
         *  0 - sort_order:         0 - ASCENDING, 1 - DESCENDING
         *  1 - sort_type:          0 POPULAR, 1 - RATING, 2 - PRICE
         *  2 - filter_category:    ID категории продукта
         *  3 - filter_brand:       ID бренда
         *  4 - filter_favorite:    0 - все продукты, 1 - избранное
         *  5 - filter_price:       интервал цен, н/р 1000,00-20000,00
         *  6 - filter_discount:    скидка
         *  7 - filrter_screen:     текущий экран
         */
         val queryOrder = "$sortorder $sorttype $filtercategory $filterbrend $filterfavorite $filterprice $filterdiscount $filterscreen"
         return encodeBase64(queryOrder)

        //return "MCAwIC0xIC0xIDAgMC4wLTAuMCAwIDE="
    }

    @JvmName("setFilterProducts_")
    fun setFilterProduct(filter: Filter) {
        if (filter != filterProduct) {
            filterProduct = filter
            queryOrder = getQueryOrder()
        }
    }

    @JvmName("setSortProducts_")
    fun setSortProduct(sort: SortOrder){
        if (sort != sortProduct) {
            sortProduct = sort
            queryOrder = getQueryOrder()
        }
    }

}
