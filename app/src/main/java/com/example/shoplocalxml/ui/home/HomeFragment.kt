package com.example.shoplocalxml.ui.home

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.AppShopLocal.Companion.repository
import com.example.shoplocalxml.DATA_PORTION
import com.example.shoplocalxml.FILTER_KEY
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.MainActivity
import com.example.shoplocalxml.OnBackPressed
import com.example.shoplocalxml.OnBottomNavigationListener
import com.example.shoplocalxml.OnFabListener
import com.example.shoplocalxml.OnSpeechRecognizer
import com.example.shoplocalxml.R
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.classes.sort_filter.Filter
import com.example.shoplocalxml.classes.sort_filter.Order
import com.example.shoplocalxml.classes.sort_filter.Sort
import com.example.shoplocalxml.classes.sort_filter.SortOrder
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.custom_view.SnackbarExt
import com.example.shoplocalxml.databinding.FragmentDetailProductBinding
import com.example.shoplocalxml.databinding.FragmentHomeBinding
import com.example.shoplocalxml.getStringArrayResource
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.detail_product.DetailProductFragment
import com.example.shoplocalxml.ui.filter.FilterActivity
import com.example.shoplocalxml.ui.history_search.OnSearchHistoryListener
import com.example.shoplocalxml.ui.history_search.SearchHistoryPanel
import com.example.shoplocalxml.ui.product_item.BottomSheetProductMenu
import com.example.shoplocalxml.ui.product_item.BottomSheetProductMenu.Companion.MenuItemProduct
import com.example.shoplocalxml.ui.product_item.DividerItemCardDecoration
import com.example.shoplocalxml.ui.product_item.DividerItemRowDecoration
import com.example.shoplocalxml.ui.product_item.ProductsAdapter
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener
import com.example.shoplocalxml.vibrate
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnBackPressed, OnSpeechRecognizer, OnFabListener{
   // private lateinit var sharedViewModel: SharedViewModel
    private val adapter:ProductsAdapter by lazy {
       ProductsAdapter(context = requireContext())
   }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
       if (result.resultCode == Activity.RESULT_OK) {
           updateFilter(result.data)
       }
   }




    private var orderQuery: String = ""
    private val sharedViewModel: SharedViewModel by activityViewModels(factoryProducer = {
        FactoryViewModel(
            this,
            repository
        )
    })


    /*private val sharedViewModel: SharedViewModel =
        ViewModelProvider(requireActivity(), FactoryViewModel(requireActivity(), repository))[SharedViewModel::class.java]*/


    private lateinit var homeViewModel: HomeViewModel
    private var searchHistoryPanel: SearchHistoryPanel? = null
    private lateinit var dataBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val displaySize = getDisplaySize()
        val widthProductItemCard = (displaySize.width - 16.toPx * 3) / 2*/
        homeViewModel =
           ViewModelProvider(this)[HomeViewModel::class.java]


     /*   sharedViewModel = run {
            val factory = FactoryViewModel(this, repository)
            ViewModelProvider(requireActivity(), factory)[SharedViewModel::class.java]
        }*/

        dataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel.modeSearchProduct.observe(viewLifecycleOwner) {
           if (it == HomeViewModel.Companion.HomeMode.NULL) {
               sharedViewModel.closeApp()
           } else {
               val visible = if (it != HomeViewModel.Companion.HomeMode.MAIN)
                   View.VISIBLE else View.GONE
               dataBinding.buttonBack.visibility = visible
           }
            /*if (it == HomeViewModel.Companion.HomeMode.SEARCH_RESULT)
                dataBinding.editTextSearchQuery.borderColor = applicationContext.getColor(R.color.colorBrend)
            else
                dataBinding.editTextSearchQuery.borderColor = Color.TRANSPARENT*/
        }

        dataBinding.editTextSearchQuery.doAfterTextChanged {
            searchHistoryPanel?.setSearchQuery(it.toString())
        }

        dataBinding.editTextSearchQuery.setOnEditorActionListener { v, _, _ ->
            var result = false
            val query = (v as EditTextExt).text.toString()
            if (query.isNotBlank()) {
                hideKeyboard()
                result = true
                //dataBinding.buttonBack.visibility = View.GONE
                sharedViewModel.addSearchHistoryItem(query)
                hideSearchHistoryPanel()
                searchProducts(query)
            }
            result
        }


        dataBinding.editTextSearchQuery.setOnClickListener {
            showSearchHistoryPanel(dataBinding.editTextSearchQuery.text.toString())
        }

        dataBinding.buttonBack.setOnClickListener {
            performBack()
        }
        dataBinding.editTextSearchQuery.setDrawableOnClick {
            if (activity is OnSpeechRecognizer)
                (activity as OnSpeechRecognizer).recognize()
        }



        /*val layoutManager = GridLayoutManager(requireContext(), 2)
        dataBinding.recyclerViewProductHome.layoutManager = layoutManager
        dataBinding.recyclerViewProductHome.addItemDecoration(DividerItemDecoration())
        dataBinding.recyclerViewProductHome.itemAnimator = null*/

        getLayoutManagerRecyclerViewProductHome(sharedViewModel.filterProduct.viewmode)
        dataBinding.recyclerViewProductHome.itemAnimator = null
        dataBinding.recyclerViewProductHome.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val showFab = recyclerView.canScrollVertically(-1) && dy < 0
                //(activity as MainActivity).setFabVisibility(canScrollUp)//recyclerView.canScrollVertically(-1))
                (activity as MainActivity).setFabVisibility(showFab)
                if (!recyclerView.canScrollVertically(1)) {
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastVisibilityPosition = layoutManager.findLastVisibleItemPosition()
                    val nextPortion = lastVisibilityPosition / DATA_PORTION + 1
                    val nextPortionPosition = nextPortion * DATA_PORTION
                    val nextRowPosition = lastVisibilityPosition + 1
                    if (nextRowPosition == nextPortionPosition) {
                        sharedViewModel.getProducts(nextPortion + 1)//, sharedViewModel.getQueryOrder())
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

//        val adapter = ProductsAdapter(context = requireContext())
        adapter.setOnProductItemListener(object: OnProductItemListener{
            override fun onChangedFavorite(id: Int, value: Boolean) {
                sharedViewModel.updateProductFavorite(id, value)
            }

            override fun onClick(id: Int, index: Int) {
               // log("product $id, index $index")
                openDetailProductFragment(id, index)
            }

            override fun onShowMenu(id: Int) {
                val bottomSheetProductMenu = BottomSheetProductMenu { itemMenu, idProduct, favorite ->
                    onClickProductItemMenu(
                        itemMenu, idProduct, favorite
                    )
                }
                val bundle = Bundle().apply {
                    putInt("idproduct", id)
                    sharedViewModel.getProductFromId(id)?.let {
                        putBoolean("favorite", it.favorite > 0)
                    }
                    bottomSheetProductMenu.arguments = this
                }
                bottomSheetProductMenu.show(parentFragmentManager, "BOTTOMSHEET_PRODUCT")
            }

            override fun onAddCart(id: Int) {

            }
        })
        dataBinding.recyclerViewProductHome.adapter = adapter



     /*   dataBinding.buttonUpdateProduct.setOnClickListener {
            sharedViewModel.getProducts(1, "MCAwIC0xIC0xIDAgMC4wLTAuMCAwIDE=")
        }*/




        //

        //dataBinding.cardProduct.

       /* sharedViewModel.run {
            val errorMessage = "download error..."
            downloadImage("file.txt", true, ) { bitmap ->
                if (bitmap == null)
                    log(errorMessage)
            }
        }*/

     /*   dataBinding.cardProduct.setOnProductListener(object: OnProductListener {
            override fun onChangedFavorite(value: Boolean) {
                sharedViewModel.getProducts(1, "MCAwIC0xIC0xIDAgMC4wLTAuMCAwIDE=")
            }

            override fun onClick(index: Int) {
                log ("product $id, image $index")
            }

            override fun onShowMenu() {
                log("show menu...")
            }
        })*/

        //sharedViewModel.idViewModel = 20

       /* dataBinding.imageView2.setImageBitmap(null)
        dataBinding.buttonGradient.setOnClickListener {
            dataBinding.imageView2.setDefaultDrawable()
        }*/

        dataBinding.includeButtonMessage.buttonMessage.setOnClickListener {
            //val orderQuery = sharedViewModel.getQueryOrder()
            sharedViewModel.getProducts(1)//, orderQuery)
        }

        val wrapper: Context = ContextThemeWrapper(requireContext(), com.example.shoplocalxml.R.style.PopupMenu)
        val popupMenu = androidx.appcompat.widget.PopupMenu(wrapper, dataBinding.includePanelOrderFilter.buttonSort)
        val sortItems = getStringArrayResource(com.example.shoplocalxml.R.array.sort_items)
        setTextButtonOrder(sharedViewModel.sortProduct)
        for (i in sortItems.indices){
            val item = popupMenu.menu.add(sortItems[i]).setOnMenuItemClickListener {
                menuOrderClick(i)
                true
            }
        }

       /* popupMenu.setOnMenuItemClickListener {
            menuOrderClick(it)
            true
        }*/

        dataBinding.includePanelOrderFilter.buttonSort.setOnClickListener {
            popupMenu.show()
        }

        dataBinding.includePanelOrderFilter.buttonFilter.setOnClickListener {

                sharedViewModel.getFilterData {
                    if (it) {
                        val gson = Gson()
                        val brandsJson = gson.toJson(SharedViewModel.getBrands())
                        val categoriesJson = gson.toJson(SharedViewModel.getCategories())
                        val filterJson     = gson.toJson(sharedViewModel.filterProduct)
                        val intent = Intent(requireContext(), FilterActivity::class.java)
                        intent.putExtra("brands", brandsJson)
                        intent.putExtra("categories", categoriesJson)
                        intent.putExtra(FILTER_KEY, filterJson)


                        /*val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                            if (result.resultCode == Activity.RESULT_OK) {
                                val data: Intent? = result.data
                            }
                        }*/
                        resultLauncher.launch(intent)
                        //activity?.startActivity(intent)
                    } else {
                        vibrate(400)
                        val snackbarExt = SnackbarExt(dataBinding.root, getStringResource(R.string.message_data_error))
                        snackbarExt.type = SnackbarExt.Companion.SnackbarType.ERROR
                        snackbarExt.show()
                    }
                }
        }




        lifecycleScope.launch {
            sharedViewModel.products.collect {
                val visibility = if (it.size > 0) View.VISIBLE else View.GONE
                dataBinding.includePanelOrderFilter.panelOrderFilter.visibility = visibility

              /*  Handler(Looper.getMainLooper()).post {
                    dataBinding.recyclerViewProductHome.invalidateItemDecorations()
                }*/

                   // log("collect...")
                   /* val products = mutableListOf<Product>()
                    it.forEach {product ->
                        val listUrl = mutableListOf<String>()
                        product.linkimages?.let {linkimages_ ->
                            linkimages_.forEach { url ->
                                listUrl.add("$SERVER_URL/$DIR_IMAGES/$url")
                            }
                            products.add(
                                product.copy(
                                    linkimages = listUrl
                                )
                            )
                        }
                    }*/


                    (dataBinding.recyclerViewProductHome.adapter as ProductsAdapter).setProducts(it)//, sharedViewModel.uploadDataAgain)//, sharedViewModel.portionData)



            /* products.forEach { product ->
                        product.linkimages?.forEach {url ->
                            sharedViewModel.downloadImage(url) {bitmap ->
                                (dataBinding.recyclerViewProductHome.adapter as ProductsAdapter).updateImage(url, bitmap)
                                //productItemCard.updateImage(url, bitmap)
                            }
                        }
                    }*/

    /*                product.linkimages?.forEach {url ->
                        sharedViewModel.downloadImage(url) {bitmap ->
                            dataBinding.productItemCard.updateImage(url, bitmap)
                        }
                    }*/

                 /*   val listUrl = mutableListOf<String>()
                    it[1].linkimages?.let{linkImages_ ->
                        linkImages_.forEach {url ->
                            listUrl.add("$SERVER_URL/$DIR_IMAGES/$url")
                        }
                    }

                    //listUrl[0] = listUrl[0] + "1"


                    val product = it[1].copy(
                        linkimages = listUrl
                    )

                    dataBinding.productItemCard.product = product

                    product.linkimages?.forEach {url ->
                        sharedViewModel.downloadImage(url) {bitmap ->
                            dataBinding.productItemCard.updateImage(url, bitmap)
                        }
                    }*/

            }
        }


        return dataBinding.root
    }


    private fun performRecognize(value: String){
        showSearchHistoryPanel(start = value)
        with(dataBinding.editTextSearchQuery) {
            setText(value)
            requestFocus()
            setSelection(value.length)
        }
    }

    private fun showSearchHistoryPanel(start: String) {
        (activity as OnBottomNavigationListener).setVisibilityBottomNavigation(false)
        homeViewModel.pushStackMode(HomeViewModel.Companion.HomeMode.SEARCH_QUERY)
        if (isNotShowSearchPanel()) {
            val items = sharedViewModel.getSearchHistoryItems()
       /*     if (items.isEmpty())
                return*/
            searchHistoryPanel =
                SearchHistoryPanel(dataBinding.layoutRoot, object : OnSearchHistoryListener {
                    override fun clearSearchHistory() {
                        sharedViewModel.clearSearchHistory()
                        //dataBinding.buttonBack.visibility = View.GONE
                        hideSearchHistoryPanel()
                    }

                    override fun clickSearchHistoryItem(value: String) {
                        //sharedViewModel.addSearchHistoryItem(value)
                        dataBinding.editTextSearchQuery.setText(value)
                    }

                    override fun deleteSearchHistoryItem(value: String) {
                        sharedViewModel.deleteSearchHistoryItem(value)
                    }
                })
            searchHistoryPanel?.show(items, start)
        } else
            searchHistoryPanel?.update(start)
    }

    private fun hideSearchHistoryPanel() {
        searchHistoryPanel?.let {
            sharedViewModel.saveSearchHistory()
            it.hide()
        }
        searchHistoryPanel = null
        (activity as OnBottomNavigationListener).setVisibilityBottomNavigation(true)
    }

    override fun onStart() {
        super.onStart()
        showUnreadMessage(27)
        //log("fragmenthome start...")
        //sharedViewModel.getProducts(1, "MCAwIC0xIC0xIDAgMC4wLTAuMCAwIDE=")
    }

    private fun showUnreadMessage(count: Int) {
        if (count > 0) {
            val layoutMessageCount = dataBinding.includeButtonMessage.layoutMessageCount
            layoutMessageCount.alpha = 0f
            layoutMessageCount.clearAnimation()

            val textMessageCount = dataBinding.includeButtonMessage.textMessageCount
            textMessageCount.text = count.toString()
            textMessageCount.alpha = 0f

            val imageMessage = dataBinding.includeButtonMessage.imageMessage
            imageMessage.bringToFront()

            val imageMessageCount = dataBinding.includeButtonMessage.imageMessageCount
            val center = imageMessageCount.width / 2f
            val animScale = ScaleAnimation(
                0F, 1F, 0F, 1F,
                center, center
            )
            animScale.duration = 400

            val animTranslater = TranslateAnimation(-5f, 5f, 0f,0f)
            animTranslater.duration = 30
            animTranslater.repeatCount = 7
            animTranslater.repeatMode = ValueAnimator.REVERSE

            val animScale1 = ScaleAnimation(
                1F, 0.56F, 1F, 0.56F,
                32.toPx.toFloat(), 0f
            )
            animScale1.duration = 300
            animScale1.fillAfter = true

            layoutMessageCount.alpha = 1f
            imageMessageCount.alpha  = 1f

            CoroutineScope(Dispatchers.Main).launch {
                imageMessageCount.startAnimation(animScale)
                delay(500)
                imageMessage.startAnimation(animTranslater)
                delay(350)
                textMessageCount.alpha = 1f
                layoutMessageCount.startAnimation(animScale1)
                delay(300)
                layoutMessageCount.bringToFront()
            }
        }

    }

    override fun backPressed() {
        performBack()
    }

    private fun performBack(){
        if (homeViewModel.currentMode() == HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL) {
            activity?.supportFragmentManager?.popBackStack()
        }
        hideSearchHistoryPanel()
        val mode = homeViewModel.modeSearchProduct.value//homeViewModel.getStackMode()
        if (homeViewModel.popStackMode() == HomeViewModel.Companion.HomeMode.MAIN) {
            dataBinding.editTextSearchQuery.text?.clear()
            if (mode == HomeViewModel.Companion.HomeMode.SEARCH_RESULT) {
                val dataMode = homeViewModel.getData(HomeViewModel.Companion.HomeMode.MAIN)
                dataMode?.let{data ->
                    val changedViewMode = sharedViewModel.filterProduct.changedViewMode(data.filter)
                    if (changedViewMode)
                        getLayoutManagerRecyclerViewProductHome(data.filter.viewmode)
                    sharedViewModel.restoreDataMode(data.portionData, data.sort, data.filter, data.products)
                    if (data.scrollPosition != -1) {
                        dataBinding.recyclerViewProductHome.scrollToPosition(
                            data.scrollPosition
                        )
                    }
                }
                homeViewModel.removeData(HomeViewModel.Companion.HomeMode.SEARCH_RESULT)
            }
        }

        hideKeyboard()
    }

    private fun hideKeyboard(){
        dataBinding.editTextSearchQuery.clearFocus()
        val imm = applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(dataBinding.editTextSearchQuery.windowToken, 0)
    }

    private fun searchProducts(query: String){
        //log(homeViewModel.modeSearchProduct.value)
        /*val firstVisibled = try {
            (dataBinding.recyclerViewProductHome.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        } catch(_:Exception) {-1}*/
        //homeViewModel.searchQuery = query
        homeViewModel.saveData(
            HomeViewModel.Companion.HomeMode.MAIN,
            sharedViewModel.sortProduct.copy(),
            sharedViewModel.filterProduct.copy(),
            sharedViewModel.portionData,
            sharedViewModel.products.value.toList(),
            try {
                (dataBinding.recyclerViewProductHome.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            } catch(_:Exception) {-1}
        )
        sharedViewModel.getSearchProducts(query, page = 1, uploadAgain = true) {isEmpty ->
            homeViewModel.searchQuery = query
            if (isEmpty) {
            /*    sharedViewModel.portionData = homeViewModel.getData(HomeViewModel.Companion.HomeMode.MAIN)?.portionData ?: run {
                    val countProducts = sharedViewModel.products.value.size
                    countProducts / DATA_PORTION + if (countProducts % DATA_PORTION > 0) 1 else 0
                }
                homeViewModel.removeData(HomeViewModel.Companion.HomeMode.MAIN)*/
                showNoProductInfo()
            }// else
                homeViewModel.pushStackMode(HomeViewModel.Companion.HomeMode.SEARCH_RESULT)
        }


    }

    private fun showNoProductInfo(){
        val snackbarExt = SnackbarExt(
            dataBinding.root,
            getStringResource(R.string.message_data_filterinfo)
        )
        snackbarExt.type = SnackbarExt.Companion.SnackbarType.INFO
        snackbarExt.show()
    }

    private fun isNotShowSearchPanel() = searchHistoryPanel == null

    override fun recognize(value: String?) {
        performRecognize(value!!)
    }

    private fun onClickProductItemMenu(
        itemMenu: MenuItemProduct, idProduct: Int, favorite: Boolean
    ){
        when (itemMenu) {
            MenuItemProduct.ITEM_CART -> {}
            MenuItemProduct.ITEM_BREND -> {}
            MenuItemProduct.ITEM_FAVORITE -> {
                sharedViewModel.updateProductFavorite(idProduct, favorite)
                (dataBinding.recyclerViewProductHome.adapter as ProductsAdapter).updateProductFavorite(
                    idProduct,
                    favorite
                )
            }
            MenuItemProduct.ITEM_PRODUCT -> {}
            else -> {}
        }
    }

    override fun onFabClick() {
        dataBinding.appbarHome.setExpanded(true, true)
        hideFab()
        dataBinding.recyclerViewProductHome.scrollToPosition(0)
    }

    override fun onPause() {
        hideFab()
        super.onPause()
    }

    private fun hideFab(){
        (activity as MainActivity).setFabVisibility(false)
    }

    private fun setTextButtonOrder(sortOrder: SortOrder) {
        /*val order = sharedViewModel.sortProduct.order
        val sort  = getStringArrayResource(com.example.shoplocalxml.R.array.sort_items)[sharedViewModel.sortProduct.sort.value]*/
        val text = getStringArrayResource(com.example.shoplocalxml.R.array.sort_items)[sortOrder.sort.value]

        dataBinding.includePanelOrderFilter.buttonSort.text = text
        val sizeIcon = 24.toPx
        if (sortOrder.order == Order.DESCENDING) {
            val drawable = AppCompatResources.getDrawable(
                requireContext(),
                com.example.shoplocalxml.R.drawable.ic_sort
            )?.toBitmap(sizeIcon, sizeIcon, Bitmap.Config.ARGB_8888)
            drawable?.let { srcBitmap ->
                val matrix = Matrix()
                matrix.preScale(1f, -1f)
                val bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, sizeIcon, sizeIcon, matrix, false)
                val icon: Drawable = BitmapDrawable(resources, bitmap)
                dataBinding.includePanelOrderFilter.buttonSort.setCompoundDrawablesWithIntrinsicBounds(
                    icon,
                    null,
                    null,
                    null
                )
            }
        } else {
            dataBinding.includePanelOrderFilter.buttonSort.setCompoundDrawablesWithIntrinsicBounds(
                AppCompatResources.getDrawable(
                    requireContext(),
                    com.example.shoplocalxml.R.drawable.ic_sort
                ),
                null,
                null,
                null
            )
        }
    }

    private fun menuOrderClick(index: Int) {
        val ITEM_PRICE   = 0
        val ITEM_POPULAR = 1
        val ITEM_RATING  = 2
        val prev = sharedViewModel.sortProduct
        val sortOrder = sharedViewModel.sortProduct.copy()
        when (index) {
            ITEM_PRICE   ->{sortOrder.sort = Sort.PRICE}
            ITEM_POPULAR ->{sortOrder.sort = Sort.POPULAR}
            ITEM_RATING  ->{sortOrder.sort = Sort.RATING}
        }
        if (prev.sort == sortOrder.sort)
            sortOrder.invertOrder()
        setTextButtonOrder(sortOrder)
//        (dataBinding.recyclerViewProductHome.adapter as ProductsAdapter).
        sharedViewModel.setSortProduct(sortOrder)
        sharedViewModel.getProducts(1, true)
        /*Handler(Looper.getMainLooper()).post {
            dataBinding.recyclerViewProductHome.invalidateItemDecorations()
        }*/
    }

    private fun updateFilter(intent: Intent?){
         intent?.let{data ->
            val gson = Gson()
            val extraFilter = data.getStringExtra(FILTER_KEY)
            val prevFilter = sharedViewModel.filterProduct
            val filter = gson.fromJson(extraFilter, Filter::class.java)

            val changedViewMode = prevFilter.changedViewMode(filter)
            val changedFilterData = prevFilter != filter
            if (changedViewMode || changedFilterData)
                sharedViewModel.setFilterProduct(filter, changedFilterData)
            else return

            getLayoutManagerRecyclerViewProductHome(filter.viewmode)
            val onlyChangedViewMode = changedViewMode && !changedFilterData

             //log("changedViewMode $changedViewMode, changedFilterData $changedFilterData")


            if (onlyChangedViewMode) { // если изменился только viewMode, не загружаем данные
                                       // и делаем скролл до позиции в предыдущем viewMode
                val firstVisibled = try {
                    val manager = dataBinding.recyclerViewProductHome.layoutManager
                    (manager as GridLayoutManager).findFirstVisibleItemPosition()
                } catch(_:Exception) {-1}
                if (firstVisibled != -1) {
                    dataBinding.recyclerViewProductHome.scrollToPosition(
                        firstVisibled
                    )
                }
            } else
                updateProductsWhenFilterChanges(homeViewModel.modeSearchProduct.value!!)
        }
    }

    private fun updateProductsWhenFilterChanges(mode: HomeViewModel.Companion.HomeMode) {
        fun showInfoMessage(isEmpty: Boolean) {
            if (isEmpty)
                showNoProductInfo()
        }
        when (mode) {
            HomeViewModel.Companion.HomeMode.MAIN -> {
                sharedViewModel.getProducts(1, true) { isEmpty ->
                    showInfoMessage(isEmpty)
                }
            }
            HomeViewModel.Companion.HomeMode.SEARCH_RESULT -> {
                val searchQuery = homeViewModel.searchQuery
                    //dataBinding.editTextSearchQuery.text.toString()
                sharedViewModel.getSearchProducts(searchQuery, 1, true) { isEmpty ->
                    showInfoMessage(isEmpty)
                }
            }
            else -> {}
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getLayoutManagerRecyclerViewProductHome(viewMode: ProductsAdapter.Companion.ItemViewMode){
        val countColumn = if (viewMode == ProductsAdapter.Companion.ItemViewMode.CARD) 2 else 1
        /*firstVisibled = try {
            val manager = dataBinding.recyclerViewProductHome.layoutManager
            (manager as GridLayoutManager).findFirstVisibleItemPosition()
        } catch(_:Exception) {-1}*/
        if (dataBinding.recyclerViewProductHome.itemDecorationCount > 0)
            dataBinding.recyclerViewProductHome.removeItemDecorationAt(0)
        //adapter.setViewMode(sharedViewModel.filterProduct.viewmode)
        adapter.setViewMode(viewMode)
        dataBinding.recyclerViewProductHome.adapter = null
        dataBinding.recyclerViewProductHome.layoutManager = null
        dataBinding.recyclerViewProductHome.layoutManager = GridLayoutManager(requireContext(), countColumn)
        if (viewMode == ProductsAdapter.Companion.ItemViewMode.CARD)
            dataBinding.recyclerViewProductHome.addItemDecoration(DividerItemCardDecoration())
        else
            dataBinding.recyclerViewProductHome.addItemDecoration(DividerItemRowDecoration())
        dataBinding.recyclerViewProductHome.itemAnimator = null
        dataBinding.recyclerViewProductHome.adapter = adapter

        //adapter.notifyDataSetChanged()

        //log("first visible = $firstVisibled")
/*        if (firstVisibled != -1)
            //Handler(Looper.getMainLooper()).post {
                dataBinding.recyclerViewProductHome.scrollToPosition(firstVisibled)
                //(dataBinding.recyclerViewProductHome.layoutManager as GridLayoutManager).scrollToPositionWithOffset(8, 0)
            //}*/

    }

    private fun openDetailProductFragment(idProduct: Int, indexImage: Int) {
        sharedViewModel.products.value.find { it.id == idProduct } ?.let{product ->
            homeViewModel.pushStackMode(HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL)
            //val fragmentTransaction: FragmentTransaction = childFragmentManager.beginTransaction()
                activity?.supportFragmentManager?.let {
                    hideFab()
                    val fragmentTransaction = it.beginTransaction()
                    val brand = SharedViewModel.getProductBrend(product.brand)
                    val actionSale = SharedViewModel.getProductPromotion(product.discount, product.sold ?: 0)
                    val fragment = DetailProductFragment.newInstance(product, brand, actionSale, indexImage)
                    fragmentTransaction.add(com.example.shoplocalxml.R.id.layoutRoot, fragment)
                    fragmentTransaction.addToBackStack("DETAIL_FRAGMENT")
                    fragmentTransaction.commit()
                }


        }





/*        val gson = Gson()
        val product = sharedViewModel.products.value.find { it.id == idProduct }
        product?.let{
            val productExtra = gson.toJson(it)
            val intent = Intent(requireContext(), UserMessagesActivity::class.java)
            intent.putExtra("product", productExtra)
            val brand = SharedViewModel.getProductBrend(it.brand)
            intent.putExtra("brand", brand)
            activity?.startActivity(intent)
        }*/
    }


    /* override fun onStop() {
        (activity as MainActivity).setFabVisibility(false)
        super.onStop()
    }*/
}

/*
recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
app:spanCount="2"
 */

//<!--android:src="@android:drawable/ic_dialog_email"-->