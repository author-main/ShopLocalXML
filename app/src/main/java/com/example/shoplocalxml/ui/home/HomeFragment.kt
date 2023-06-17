package com.example.shoplocalxml.ui.home

import android.animation.ValueAnimator
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
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
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.AppShopLocal.Companion.repository
import com.example.shoplocalxml.DATA_PORTION
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.MainActivity
import com.example.shoplocalxml.OnBackPressed
import com.example.shoplocalxml.OnBottomNavigationListener
import com.example.shoplocalxml.OnFabListener
import com.example.shoplocalxml.OnSpeechRecognizer
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.classes.sort_filter.Order
import com.example.shoplocalxml.classes.sort_filter.Sort
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.FragmentHomeBinding
import com.example.shoplocalxml.getStringArrayResource
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.history_search.OnSearchHistoryListener
import com.example.shoplocalxml.ui.history_search.SearchHistoryPanel
import com.example.shoplocalxml.ui.product_item.BottomSheetProductMenu
import com.example.shoplocalxml.ui.product_item.BottomSheetProductMenu.Companion.MenuItemProduct
import com.example.shoplocalxml.ui.product_item.ProductsAdapter
import com.example.shoplocalxml.ui.product_item.item_card.DividerItemDecoration
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnBackPressed, OnSpeechRecognizer, OnFabListener {

   // private lateinit var sharedViewModel: SharedViewModel

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



        val layoutManager = GridLayoutManager(requireContext(), 2)
        dataBinding.recyclerViewProductHome.layoutManager = layoutManager
        dataBinding.recyclerViewProductHome.addItemDecoration(DividerItemDecoration())
        //dataBinding.recyclerViewProductHome.itemAnimator = null
        dataBinding.recyclerViewProductHome.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //log("last visible position = ${layoutManager.findLastVisibleItemPosition()}")
                super.onScrolled(recyclerView, dx, dy)
                (activity as MainActivity).setFabVisibility(recyclerView.canScrollVertically(-1))
                if (!recyclerView.canScrollVertically(1)) {
                    val lastVisibilityPosition = layoutManager.findLastVisibleItemPosition()
                    val nextPortion = lastVisibilityPosition / DATA_PORTION + 1
                    val nextPortionPosition = nextPortion * DATA_PORTION
                    val nextRowPosition = lastVisibilityPosition + 1
                    if (nextRowPosition == nextPortionPosition) {
                        sharedViewModel.getProducts(nextPortion + 1, homeViewModel.getQueryOrder())
                    }
                }
            }
        })

        val adapter = ProductsAdapter(context = requireContext())
        adapter.setOnProductItemListener(object: OnProductItemListener{
            override fun onChangedFavorite(id: Int, value: Boolean) {
                sharedViewModel.updateProductFavorite(id, value)
            }

            override fun onClick(id: Int, index: Int) {

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
            val orderQuery = homeViewModel.getQueryOrder()
            sharedViewModel.getProducts(1, orderQuery)
        }

        val wrapper: Context = ContextThemeWrapper(requireContext(), com.example.shoplocalxml.R.style.PopupMenu)
        val popupMenu = androidx.appcompat.widget.PopupMenu(wrapper, dataBinding.includePanelOrderFilter.buttonSort)
        val sortItems = getStringArrayResource(com.example.shoplocalxml.R.array.sort_items)
        setTextButtonOrder(sortItems[sharedViewModel.sortOrder.sort.value])
        for (i in sortItems.indices){
            val item = popupMenu.menu.add(sortItems[i]).setOnMenuItemClickListener {
                menuOrderClick(i)
                setTextButtonOrder(sortItems[i])
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

        lifecycleScope.launch {
            sharedViewModel.products.collect {
                val visibility = if (it.size > 0) View.VISIBLE else View.GONE
                dataBinding.includePanelOrderFilter.panelOrderFilter.visibility = visibility

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
                    (dataBinding.recyclerViewProductHome.adapter as ProductsAdapter).setProducts(it)//, sharedViewModel.portionData)
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
        hideSearchHistoryPanel()
        if (homeViewModel.popStackMode() == HomeViewModel.Companion.HomeMode.MAIN)
            dataBinding.editTextSearchQuery.text?.clear()

        hideKeyboard()
    }

    private fun hideKeyboard(){
        dataBinding.editTextSearchQuery.clearFocus()
        val imm = applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(dataBinding.editTextSearchQuery.windowToken, 0)
    }

    private fun searchProducts(query: String){
        homeViewModel.pushStackMode(HomeViewModel.Companion.HomeMode.SEARCH_RESULT)
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
        dataBinding.recyclerViewProductHome.smoothScrollToPosition(0)
    }

    override fun onPause() {
        hideFab()
        super.onPause()
    }

    private fun hideFab(){
        (activity as MainActivity).setFabVisibility(false)
    }

    private fun setTextButtonOrder(value: String) {
        dataBinding.includePanelOrderFilter.buttonSort.text = value
        if (sharedViewModel.sortOrder.order == Order.DESCENDING) {
            val drawable = AppCompatResources.getDrawable(
                requireContext(),
                com.example.shoplocalxml.R.drawable.ic_sort
            )?.toBitmap(24.toPx, 24.toPx, Bitmap.Config.ARGB_8888)
            drawable?.let { srcBitmap ->
                val matrix = Matrix()
                matrix.preScale(1f, -1f)
                val bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, 24.toPx, 24.toPx, matrix, false)
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
        val prev = sharedViewModel.sortOrder.sort
        when (index) {
            ITEM_PRICE   ->{sharedViewModel.sortOrder.sort = Sort.PRICE}
            ITEM_POPULAR ->{sharedViewModel.sortOrder.sort = Sort.POPULAR}
            ITEM_RATING  ->{sharedViewModel.sortOrder.sort = Sort.RATING}
        }
        if (prev == sharedViewModel.sortOrder.sort)
            sharedViewModel.sortOrder.invertOrder()
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