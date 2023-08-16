package com.example.shoplocalxml

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.classes.image_downloader.ImageDownloadManager
import com.example.shoplocalxml.classes.sort_filter.Filter
import com.example.shoplocalxml.classes.sort_filter.SortOrder
import com.example.shoplocalxml.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs

class SharedViewModel(private val repository: Repository): ViewModel() {
    private val UUID_QUERY = System.nanoTime().toString()
    var sortProduct        = SortOrder()
    var filterProduct      = Filter().apply { discount = 2 }
//    var uploadDataAgain: Boolean = false
    private var queryOrder = getQueryOrder()
    private var processQuery = false
    var portionData = 0
    private val _products = MutableStateFlow<MutableList<Product>>(mutableListOf())
    val products = _products.asStateFlow()
    /*private fun setProducts(value: MutableList<Product>) {
        _products.value = value
    }*/


    /*private val _messages = MutableStateFlow<MutableList<UserMessage>>(mutableListOf())
    val messages = _messages.asStateFlow()*/


    /*private val _reviews = MutableStateFlow<MutableList<Review>>(mutableListOf())
    val reviews = _reviews.asStateFlow()*/
    /*private fun setReviews(value: MutableList<Review>) {
        _reviews.value = value
    }*/



    fun getListBrend(){
        viewModelScope.launch(Dispatchers.IO) {
            listBrend =  repository.getBrends() ?: listOf()
        }
    }

  /*  fun getListCategory(){
        viewModelScope.launch(Dispatchers.IO) {
            listCategory =  repository.getCategories() ?: listOf()
        }
    }*/


    fun getFilterData(action: (value: Boolean) -> Unit){
        if (processQuery) return
        processQuery = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                listBrend = repository.getBrends() ?: listOf()
                listCategory = repository.getCategories() ?: listOf()
                val success = !(listBrend.isEmpty() || listCategory.isEmpty())
                action(success)
            } catch (e: Exception) {
                action(false)
            }
            finally {
                processQuery = false
            }
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

    fun restoreDataMode(portionData: Int, sort: SortOrder, filter: Filter, products: List<Product>){
        this.portionData = portionData
        this.setSortProduct(sort, false)
        this.setFilterProduct(filter, true)
        ImageDownloadManager.cancelAll()
        _products.value = products.toMutableList()
    }

    private fun updateDataAfterQuery(list: List<Product>, uploadAgain: Boolean) {
            //log("update products...")
            val updateList = updateHostLink(list)
            if (uploadAgain) {
                ImageDownloadManager.cancelAll()
                _products.value = updateList.toMutableList()
            }
            else {
                val newList =
                    _products.value.toMutableList().apply {
                        addAll(updateList)
                    }
                _products.value = newList
            }

        //_products.value = newList
        /*_products.update {
            _products.value.toMutableList().apply { this.addAll(updateHostLink(list)) }
        }*/
    }

    fun getSearchProducts(searchQuery: String, page: Int, uploadAgain: Boolean = false, onEmptyResult: ((Boolean)->Unit)? = null){
        if (uploadAgain) {
            portionData = 0
            processQuery = false
        }
        if (processQuery) return
        if (page <= portionData) return
        processQuery = true
        viewModelScope.launch {
            val resultQuery = repository.getSearchProducts(UUID_QUERY, searchQuery, page, queryOrder)
            if (resultQuery == null)
                processQuery = false
            else resultQuery.let{ products ->
                if (products.isNotEmpty()) {
                    portionData += 1
                    updateDataAfterQuery(products, uploadAgain)
                }
                processQuery = false
            }

            onEmptyResult?.invoke(page==1 && resultQuery.isNullOrEmpty())
        }
    }


    fun getReviewsProduct(id: Int, limit: Int, updateReviews: (list: List<Review>) -> Unit) {
        viewModelScope.launch {
            repository.getReviewsProduct(id, limit)?.let{
                //_reviews.value = it.toMutableList()
                updateReviews(it)
            }
        }
    }

    fun getProducts(page: Int, uploadAgain: Boolean  = false, onEmptyResult: ((Boolean)->Unit)? = null){//}, order: String) {
//        uploadDataAgain = uploadAgain
        if (uploadAgain) {
            portionData = 0
            processQuery = false
        }
        if (processQuery) return
        if (page <= portionData) return
        processQuery = true
        //log(queryOrder)
        //CoroutineScope(Dispatchers.Main).launch {

        viewModelScope.launch {
            val resultQuery = repository.getProducts(page, queryOrder)
            if (resultQuery == null)
                processQuery = false
            else resultQuery.let{ products ->
                if (products.isNotEmpty()) {
                    portionData += 1
                    updateDataAfterQuery(products, uploadAgain)
                }
                processQuery = false
            }

            onEmptyResult?.invoke(page==1 && resultQuery.isNullOrEmpty())

            /*if (resultQuery.isNullOrEmpty())
                onEmptyResult?.invoke(true)*/
        }
    }


    fun getUnreadDeliveryMessages(action: ((MutableList<UserMessage>) -> Unit)? = null){
        val requestUnreadDelivery = 2
        if (processQuery) return
        processQuery = true
        viewModelScope.launch {
            processQuery = false
            val listMessages = repository.getMessages(requestUnreadDelivery)?.toMutableList() ?: mutableListOf()
            action?.invoke(listMessages)
        }
    }

    fun getMessages(requestCount: Boolean = false, action: ((MutableList<UserMessage>) -> Unit)? = null){
        if (processQuery) return
        processQuery = true
        val valueRequestCount = if (requestCount) 1 else 0
        viewModelScope.launch {
            processQuery = false
            val listMessages = repository.getMessages(valueRequestCount)?.toMutableList() ?: mutableListOf()
            action?.invoke(listMessages)
        }
    }

    fun updateMessages(joinread: String?, joindeleted: String?){
        if (joinread != null || joindeleted != null) {
            viewModelScope.launch {
                repository.updateMessages(joinread ?: "x", joindeleted ?: "x")
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

    /*override fun onCleared() {
        super.onCleared()
    }*/


    fun getProductFromId(id: Int) =
        _products.value.find { it.id == id }

    private fun getQueryOrder(): String {
        val sortorder          = sortProduct.order.value
        val sorttype           = sortProduct.sort.value
        val enum               =  filterProduct.enum
        //val filtercategory     = filterProduct.category
        //val filterbrend        = filterProduct.brend
        val filterfavorite     = if (filterProduct.favorite) 1 else 0
        val filterprice        = "${filterProduct.fromPrice}-${filterProduct.toPrice}" /*run {
            val value: Pair<Int, Int>   = filterProduct.priceRange
            "${value.first}-${value.second}"
        }*/
        val filterdiscount = filterProduct.discount
        val filterscreen   = 0
        var section = EMPTY_STRING
        for (entry in enum) {
            val key = abs(entry.key)
            section += "$key[" + entry.value.joinToString(",", postfix = "]-")
        }
        section = if (section.isEmpty())
            "-1"
        else
            section.substringBeforeLast("-")
        //log(section)
        /** Порядок для извлечения в PHP:
         *  0 - sort_order:         0 - ASCENDING, 1 - DESCENDING
         *  1 - sort_type:          0 POPULAR, 1 - RATING, 2 - PRICE

        /*   2 - filter_category:    ID категории продукта
         *  3 - filter_brand:       ID бренда  */

         *  2 - filter_enum:        выборка по категории и бренду
         *  3 - filter_favorite:    0 - все продукты, 1 - избранное
         *  4 - filter_price:       интервал цен, н/р 1000,00-20000,00
         *  5 - filter_discount:    скидка
         *  6 - filrter_screen:     текущий экран
         */

        //log("$sortorder $sorttype $section $filterfavorite $filterprice $filterdiscount $filterscreen")

        //  val queryOrder = "$sortorder $sorttype -1 -1 $filterfavorite $filterprice $filterdiscount $filterscreen"
        val queryOrder = "$sortorder $sorttype $section $filterfavorite $filterprice $filterdiscount $filterscreen"
        //log("queryOrder = $queryOrder")
        //val queryOrder = "$sortorder $sorttype $filtercategory $filterbrend $filterfavorite $filterprice $filterdiscount $filterscreen"
        //log(encodeBase64(queryOrder))
        return encodeBase64(queryOrder)

        //return "MCAwIC0xIC0xIDAgMC4wLTAuMCAwIDE="
    }

    @JvmName("setFilterProducts_")
    fun setFilterProduct(filter: Filter, changedFilterData: Boolean) {
        //if (filter != filterProduct) {
            filterProduct = filter
            if (changedFilterData)
                queryOrder = getQueryOrder()
        //}
    }

    @JvmName("setSortProducts_")
    fun setSortProduct(sort: SortOrder, updateQueryOrder: Boolean = true){
        if (sort != sortProduct) {
            sortProduct = sort
            if (updateQueryOrder)
                queryOrder = getQueryOrder()
        }
    }



    companion object {
        private var listBrend    = listOf<Brend>()
        private var listCategory = listOf<Category>()
        fun getCategories() = listCategory
        fun getBrands()     = listBrend
        @JvmStatic fun getProductBrend(id: Int?) =
            if (id == null) EMPTY_STRING
                else listBrend.find { it.id == id }?.name ?: EMPTY_STRING

        @JvmStatic fun getProductSalePrice(value: Float, discount: Int) =
            getFormattedFloat(value - value * discount / 100f)

        @JvmStatic fun getProductPrice(value: Float) =
            getFormattedFloat(value)

        @JvmStatic fun getProductPromotion(discount: Int, sold: Int): String {
            var textPromotion = EMPTY_STRING
            if (sold > BOUND_BESTSELLER)       textPromotion = getStringResource(R.string.text_bestseller)
            else
            if (discount > BOUND_ACTION) textPromotion = getStringResource(R.string.text_action)
            else
            if (discount > 0)           textPromotion = getStringResource(R.string.text_benefit)
            return textPromotion
        }


    }
}
