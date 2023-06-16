package com.example.shoplocalxml.ui.home

import com.example.shoplocalxml.ui.product_item.BottomSheetProductMenu.Companion.MenuItemProduct
import android.animation.ValueAnimator
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnScrollChangeListener
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
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
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.MainActivity
import com.example.shoplocalxml.OnBackPressed
import com.example.shoplocalxml.OnBottomNavigationListener
import com.example.shoplocalxml.OnFabListener
import com.example.shoplocalxml.OnSpeechRecognizer
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.FragmentHomeBinding
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.history_search.OnSearchHistoryListener
import com.example.shoplocalxml.ui.history_search.SearchHistoryPanel
import com.example.shoplocalxml.ui.product_item.BottomSheetProductMenu
import com.example.shoplocalxml.ui.product_item.ProductsAdapter
import com.example.shoplocalxml.ui.product_item.item_card.DividerItemDecoration
import com.example.shoplocalxml.ui.product_item.product_card.OnProductItemListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnBackPressed, OnSpeechRecognizer, OnFabListener {

   // private lateinit var sharedViewModel: SharedViewModel
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
        dataBinding.recyclerViewProductHome.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //log("last visible position = ${layoutManager.findLastVisibleItemPosition()}")
                super.onScrolled(recyclerView, dx, dy)
                (activity as MainActivity).setFabVisibility(recyclerView.canScrollVertically(-1))


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
            sharedViewModel.getProducts(1, "MCAwIC0xIC0xIDAgMC4wLTAuMCAwIDE=")
        }


        lifecycleScope.launch {
            sharedViewModel.products.collect {
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
                    (dataBinding.recyclerViewProductHome.adapter as ProductsAdapter).setProducts(it)
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
        //(activity as MainActivity).setFabVisibility(false)
        dataBinding.recyclerViewProductHome.smoothScrollToPosition(0)
    }
}

/*
recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

app:layoutManager="androidx.recyclerview.widget.GridLayoutManager"
app:spanCount="2"
 */

//<!--android:src="@android:drawable/ic_dialog_email"-->