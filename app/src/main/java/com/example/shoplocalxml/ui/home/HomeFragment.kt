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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.DATA_PORTION
import com.example.shoplocalxml.FILTER_KEY
import com.example.shoplocalxml.MainActivity
import com.example.shoplocalxml.OnBackPressed
import com.example.shoplocalxml.OnBottomNavigationListener
import com.example.shoplocalxml.OnFabListener
import com.example.shoplocalxml.OnSpeechRecognizer
import com.example.shoplocalxml.R
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.classes.sort_filter.Filter
import com.example.shoplocalxml.classes.sort_filter.Order
import com.example.shoplocalxml.classes.sort_filter.Sort
import com.example.shoplocalxml.classes.sort_filter.SortOrder
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.custom_view.SnackbarExt
import com.example.shoplocalxml.databinding.FragmentHomeBinding
import com.example.shoplocalxml.getStringArrayResource
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.detail_product.DetailProductFragment
import com.example.shoplocalxml.ui.filter.FilterActivity
import com.example.shoplocalxml.ui.history_search.OnSearchHistoryListener
import com.example.shoplocalxml.ui.history_search.SearchHistoryPanel
import com.example.shoplocalxml.ui.notifications.MessagesNotification
import com.example.shoplocalxml.ui.product_item.BottomSheetProductMenu
import com.example.shoplocalxml.ui.product_item.BottomSheetProductMenu.Companion.MenuItemProduct
import com.example.shoplocalxml.ui.product_item.DividerItemCardDecoration
import com.example.shoplocalxml.ui.product_item.DividerItemRowDecoration
import com.example.shoplocalxml.ui.product_item.ProductsAdapter
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener
import com.example.shoplocalxml.ui.user_messages.UserMessagesActivity
import com.example.shoplocalxml.vibrate
import com.example.shoplocalxml.visibility
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnBackPressed, OnSpeechRecognizer, OnFabListener, OnAddProductCart{
   // private lateinit var sharedViewModel: SharedViewModel
    //private var scrollPosition = 0
    private var isShowNotifications = false
    private var updateCountMessage = false
    private var countUnreadMessages = 0
    private val adapter:ProductsAdapter by lazy {
       ProductsAdapter(context = requireContext())
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
       if (result.resultCode == Activity.RESULT_OK) {
           updateFilter(result.data)
       }
   }

    private val resultMessagesLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            updateMessages(result.data)
    }




    //private var orderQuery: String = ""
    private lateinit var sharedViewModel: SharedViewModel
    /*private val sharedViewModel: SharedViewModel by activityViewModels(factoryProducer = {
        FactoryViewModel(
            requireActivity()/*,
            //this,
            repository*/
        )
    })*/


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
       /* homeViewModel =
           ViewModelProvider(this)[HomeViewModel::class.java]*/

        val mainActivity = requireActivity() as MainActivity
        sharedViewModel = mainActivity.viewModelComponent.factory.create(SharedViewModel::class.java)

        homeViewModel =
            //ViewModelProvider(requireActivity(), FactoryViewModel(requireActivity()))[HomeViewModel::class.java]
            mainActivity.viewModelComponent.factory.create(HomeViewModel::class.java)

        homeViewModel.setOnChangeMode {
            if (homeViewModel.modeFragment.value == HomeViewModel.Companion.HomeMode.MAIN)    {
                //log("save data...")
                saveHomeViewProductState()
            }
        }


     /*   sharedViewModel = run {
            val factory = FactoryViewModel(this, repository)
            ViewModelProvider(requireActivity(), factory)[SharedViewModel::class.java]
        }*/

        dataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel.modeFragment.observe(viewLifecycleOwner) {
           /* val scrollPosition = try {
                (dataBinding.recyclerViewProductHome.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            } catch(_: Exception) {-1}
            log("scroll position $scrollPosition")
            homeViewModel.getPrevMode()*/
            //log(it)

            val visibility = if (it != HomeViewModel.Companion.HomeMode.MAIN)
                View.VISIBLE else View.GONE
            dataBinding.buttonBack.visibility = visibility

           when (it) {
               HomeViewModel.Companion.HomeMode.NULL -> sharedViewModel.closeApp()
               HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL -> {
                   setVisibleDetailProductActionbarButtons(true)
                   val favorite = DetailProductFragment.favorite
                   setDetailProductFavoriteButtonIcon(
                       favorite
                   )
                   setVisibleUserMessageButton(false)
               }
               HomeViewModel.Companion.HomeMode.MAIN -> {
                   setVisibleDetailProductActionbarButtons(false)
                   setVisibleUserMessageButton(true)
               }
               else -> {

               }
           }





         /*  if (it == HomeViewModel.Companion.HomeMode.NULL) {
               sharedViewModel.closeApp()
           } else {
               val visible = if (it != HomeViewModel.Companion.HomeMode.MAIN)
                   View.VISIBLE else View.GONE
               dataBinding.buttonBack.visibility = visible
           }*/
            /*if (it == HomeViewModel.Companion.HomeMode.SEARCH_RESULT)
                dataBinding.editTextSearchQuery.borderColor = applicationContext.getColor(R.color.colorBrend)
            else
                dataBinding.editTextSearchQuery.borderColor = Color.TRANSPARENT*/
        }

        dataBinding.eventhandler = this

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
//                val bundle =
                Bundle().apply {
                    putInt("idproduct", id)
                    sharedViewModel.getProductFromId(id)?.let {
                        putBoolean("favorite", it.favorite > 0)
                    }
                    bottomSheetProductMenu.arguments = this
                }
                bottomSheetProductMenu.show(parentFragmentManager, "BOTTOMSHEET_PRODUCT")
            }

            override fun onAddCart(id: Int) {
                addProductCart(id)
            }
        })
        dataBinding.recyclerViewProductHome.adapter = adapter



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
            sharedViewModel.getMessages {messages ->
                MessagesNotification.clear()
                showUserMessages(messages)
            }
        }

        val wrapper: Context = ContextThemeWrapper(requireContext(), R.style.PopupMenu)
        val popupMenu = androidx.appcompat.widget.PopupMenu(wrapper, dataBinding.includePanelOrderFilter.buttonSort)
        val sortItems = getStringArrayResource(R.array.sort_items)
        setTextButtonOrder(sharedViewModel.sortProduct)
        for (i in sortItems.indices){
//            val item =
            popupMenu.menu.add(sortItems[i]).setOnMenuItemClickListener {
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


        //log("on create...")

      /*  lifecycleScope.launch {

            sharedViewModel.messages.collect {
                log("collect messages...")
                if (it.size > 0) {
                    //log("show user messages...")
                    showUserMessages(it)
                }
            }
        }*/

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
                    //log("set products...")



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
        /*val messagesNotification = MessagesNotification(applicationContext)
        messagesNotification.notifyMessages(listOf())*/
        //getUnreadDeliveryMessages()
        return dataBinding.root
    }


    private fun getUnreadDeliveryMessages(){
        lifecycleScope.launch {
            sharedViewModel.getUnreadDeliveryMessages {
                if (it.isNotEmpty() && !isShowNotifications) {
                    isShowNotifications = true
                    //log("show notifications")
                    MessagesNotification.getInstance()
                    MessagesNotification.notifyMessages(it)
                }
            }

        }
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

    override fun onResume() {
        super.onResume()
        if (updateCountMessage) {
            updateCountMessage = false
            showUnreadMessage(countUnreadMessages)
        } else {
                showUnreadMessage()
        }
    }

    /*override fun onStart() {
        super.onStart()
        if (updateCountMessage) {
            updateCountMessage = false
            showUnreadMessage(countUnreadMessages)
        } else
            showUnreadMessage()
    }*/

    private fun setVisibleDetailProductActionbarButtons(value: Boolean){
        val visibility = value.visibility
        val view = dataBinding.includeDetailProductButtons.layoutDetailProductButtons
        if (view.visibility != visibility)
            view.visibility = visibility
    }

    private fun setDetailProductFavoriteButtonIcon(favorite: Byte?) {
        favorite?.let {
            dataBinding.includeDetailProductButtons.buttonFavorite.setImageResource(
                if (it > 0) R.drawable.ic_favorite
                else R.drawable.ic_favorite_bs
            )
        }
    }

    private fun setVisibleUserMessageButton(value: Boolean){
        val visibility = value.visibility
        val view = dataBinding.includeButtonMessage.buttonMessage
        if (view.visibility != visibility) {
            view.visibility = visibility
            if (value)
                showUnreadMessage()
        }
    }


   /* private fun getVisibilityFromBoolean(value: Boolean) =
        if (value) View.VISIBLE else View.GONE*/

    private fun showUnreadMessage(updateCount: Int = -1) {
        fun animateCountMessages(count: Int) {
            val layoutMessageCount = dataBinding.includeButtonMessage.layoutMessageCount
            val imageMessageCount = dataBinding.includeButtonMessage.imageMessageCount
            layoutMessageCount.clearAnimation()
            layoutMessageCount.alpha = 0f
            imageMessageCount.alpha  = 0f
            imageMessageCount.drawable.setTint(requireContext().getColor(R.color.colorDiscount))
            if (countUnreadMessages > 0) {
                layoutMessageCount.alpha = 0f
                val textMessageCount = dataBinding.includeButtonMessage.textMessageCount
                textMessageCount.text = count.toString()
                textMessageCount.alpha = 0f

                val imageMessage = dataBinding.includeButtonMessage.imageMessage
                imageMessage.bringToFront()


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
            } /*else {
                layoutMessageCount.alpha = 0f
            }*/


        //dataBinding.includeButtonMessage.layoutMessageCount.visibility = View.VISIBLE

        }

        if (updateCount != -1) {
            animateCountMessages(updateCount)
        } else {
            sharedViewModel.getMessages(true) {
                //log("request count...")
                val count = if (it.size > 0) it[0].id else 0
                countUnreadMessages = count
                animateCountMessages(countUnreadMessages)
                getUnreadDeliveryMessages()
            }
        }

    }

    override fun backPressed() {
        performBack()
    }

    private fun performBack(){
        val mode = homeViewModel.modeFragment.value
       // if (homeViewModel.currentMode() == HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL) {
        if (mode == HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL) {
            activity?.supportFragmentManager?.popBackStack()
        }
        hideSearchHistoryPanel()
        //val mode = homeViewModel.modeFragment.value//homeViewModel.getStackMode()

        val curMode = homeViewModel.popStackMode()

        if (curMode == HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL) {
            dataBinding.editTextSearchQuery.text?.clear()
        }

        if (curMode == HomeViewModel.Companion.HomeMode.MAIN) {
            dataBinding.editTextSearchQuery.text?.clear()
            if (mode == HomeViewModel.Companion.HomeMode.SEARCH_RESULT) {
                val dataMode = homeViewModel.getData(HomeViewModel.Companion.HomeMode.MAIN)
                dataMode?.let{data ->
                    val changedViewMode = sharedViewModel.filterProduct.changedViewMode(data.filter)
                    if (changedViewMode)
                        getLayoutManagerRecyclerViewProductHome(data.filter.viewmode)
                    sharedViewModel.restoreDataMode(data.portionData, data.sort, data.filter, data.products)
                    //log("restored scroll ${data.scrollPosition}")
                    if (data.scrollPosition != -1) {

                        (dataBinding.recyclerViewProductHome.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
                            data.scrollPosition,
                            0
                        )
                    }
                }
                //homeViewModel.removeData(HomeViewModel.Companion.HomeMode.SEARCH_RESULT)
                homeViewModel.removeData(HomeViewModel.Companion.HomeMode.MAIN)
            }
        }

        hideKeyboard()
    }

    private fun hideKeyboard(){
        dataBinding.editTextSearchQuery.clearFocus()
        val imm = applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(dataBinding.editTextSearchQuery.windowToken, 0)
    }


    private fun getRecyclerViewProductsScrollPosition(): Int {
        return try {
            (dataBinding.recyclerViewProductHome.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
        } catch(_: Exception) {-1}
    }

    private fun saveHomeViewProductState(){
        /*val scrollPosition = try {
            (dataBinding.recyclerViewProductHome.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        } catch(_: Exception) {-1}*/
        val scrollPosition = getRecyclerViewProductsScrollPosition()
        //log("scrollPosition = $scrollPosition")
        homeViewModel.saveData(
            HomeViewModel.Companion.HomeMode.MAIN,
            sharedViewModel.sortProduct.copy(),
            sharedViewModel.filterProduct.copy(),
            sharedViewModel.portionData,
            sharedViewModel.products.value.toList(),
            scrollPosition
        )
    }

    private fun searchProducts(query: String){
        fun requestDetailProduct(emptyResult: Boolean): Boolean {
            var displaySearchResult = true
           // if (homeViewModel.modeFragment.value == HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL) {
            if (homeViewModel.existStackMode(HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL)) {
                if (emptyResult) {
                    homeViewModel.removeData(HomeViewModel.Companion.HomeMode.MAIN)
                    displaySearchResult = false
                } else {
                    homeViewModel.removeStackMode(HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL)
                    activity?.supportFragmentManager?.popBackStack()
                }
            }
            return displaySearchResult
        }
        //saveHomeViewProductState()
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
            if (requestDetailProduct(isEmpty))
                homeViewModel.pushStackMode(HomeViewModel.Companion.HomeMode.SEARCH_RESULT)
            //log(homeViewModel.modeFragment.value)
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
        val text = getStringArrayResource(R.array.sort_items)[sortOrder.sort.value]

        dataBinding.includePanelOrderFilter.buttonSort.text = text
        val sizeIcon = 24.toPx
        if (sortOrder.order == Order.DESCENDING) {
            val drawable = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_sort
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
                    R.drawable.ic_sort
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
                updateProductsWhenFilterChanges(homeViewModel.modeFragment.value!!)
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

    fun clickDetailProductFavoriteButton(){
        DetailProductFragment.favorite?.let{
            val favorite: Byte =
                if (it > 0) 0
                else 1
            setDetailProductFavoriteButtonIcon(favorite)
            DetailProductFragment.favorite = favorite
            val idProduct = DetailProductFragment.id!!
            val favoriteProduct = favorite > 0
            sharedViewModel.updateProductFavorite(idProduct, favoriteProduct)
            (dataBinding.recyclerViewProductHome.adapter as ProductsAdapter).updateProductFavorite(idProduct, favoriteProduct)
        }
    }

    fun clickDetailProductShareButton(){
        log("click share button...")
    }


    private fun openDetailProductFragment(idProduct: Int, indexImage: Int) {



        sharedViewModel.products.value.find { it.id == idProduct } ?.let{product ->
                //activity?.supportFragmentManager?.let {
                childFragmentManager.let{
                    hideFab()
                    val fragmentTransaction = it.beginTransaction()
                    val fragment = DetailProductFragment.newInstance(product, indexImage)
                    fragmentTransaction.add(R.id.layoutRoot, fragment)
                    fragmentTransaction.addToBackStack(null)//"DETAIL_FRAGMENT")
                    fragmentTransaction.commit()
                }
            homeViewModel.pushStackMode(HomeViewModel.Companion.HomeMode.PRODUCT_DETAIL)
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

    private fun showUserMessages(messages: MutableList<UserMessage>){
        homeViewModel.pushStackMode(HomeViewModel.Companion.HomeMode.USER_MESSAGES)
        val gson = Gson()
        val messagesJson = gson.toJson(messages)
        val intent = Intent(requireContext(), UserMessagesActivity::class.java)
        intent.putExtra("messages", messagesJson)
        resultMessagesLauncher.launch(intent)
        //startActivity(intent)
    }

    private fun updateMessages(intent: Intent?){
        homeViewModel.popStackMode()
        updateCountMessage = true
        intent?.let { data ->
            val readMessages   = data.getStringExtra("read_messages")
            readMessages?.let {
               val elements = it.split(',')
               countUnreadMessages -= elements.size
            }

            // Без обновления данных на сервере (убрать для обновления на сервере)
            /*
                val deleteMessages = data.getStringExtra("delete_messages")
                sharedViewModel.updateMessages(readMessages, deleteMessages)
            */
        }
    }

    /* override fun onStop() {
        (activity as MainActivity).setFabVisibility(false)
        super.onStop()
    }*/

    override fun addProductCart(idProduct: Int) {
        log("add product in cart...")
        /*sharedViewModel.addProductCart(id) {idResponse ->

        }*/
    }
}

/*
recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
app:spanCount="2"
 */

//<!--android:src="@android:drawable/ic_dialog_email"-->