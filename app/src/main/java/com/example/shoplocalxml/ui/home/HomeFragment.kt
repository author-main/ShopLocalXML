package com.example.shoplocalxml.ui.home

import android.animation.ValueAnimator
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.AppShopLocal.Companion.repository
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.MainActivity
import com.example.shoplocalxml.OnBackPressed
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.FragmentHomeBinding
import com.example.shoplocalxml.log
import com.example.shoplocalxml.repository.Repository
import com.example.shoplocalxml.sharedViewModel
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.history_search.OnSearchHistoryListener
import com.example.shoplocalxml.ui.history_search.SearchHistoryPanel
import com.example.shoplocalxml.ui.history_search.SearchQueryStorage
import com.example.shoplocalxml.ui.history_search.SearchQueryStorageInterface
import com.example.shoplocalxml.ui.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnBackPressed {
    private lateinit var homeViewModel: HomeViewModel
    //private lateinit var sharedViewModel: SharedViewModel
    /*by activityViewModels(
                factoryProducer = {
                    FactoryViewModel(
                        requireActivity() as MainActivity,
                        AppShopLocal.repository
                    )
                }
             )*/
    private var searchHistoryPanel: SearchHistoryPanel? = null
    private lateinit var dataBinding: FragmentHomeBinding
    /*val Fragment.sharedViewModel: SharedViewModel
        get() = (activity as MainActivity).sharedViewModel*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
         /*sharedViewModel =
             (requireActivity() as MainActivity).sharedViewModel*/
         dataBinding = FragmentHomeBinding.inflate(inflater, container, false)
/*         dataBinding.buttonTask.setOnClickListener {
            searchHistoryPanel = SearchHistoryPanel(dataBinding.layoutRoot, activity as OnSearchHistoryListener)
            val items = mutableListOf<String>()
            items.add("Samsung")
            items.add("NVidia")
            items.add("AMD")
            items.add("Intel")
            items.add("AOC")
            items.add("Corsair")
            items.add("Kingstone")
            items.add("Xiaomi")
            searchHistoryPanel?.show(items)
        }*/
        /*sharedViewModel.querySearch.observe(viewLifecycleOwner) {
            searchHistoryPanel?.setSearchQuery(it)
        }*/

        homeViewModel.modeSearchProduct.observe(viewLifecycleOwner) {
            if (it == HomeViewModel.Companion.HomeMode.NULL) {
                sharedViewModel.closeApp()
            } else {
                val visible = if (it != HomeViewModel.Companion.HomeMode.MAIN)
                    View.VISIBLE else View.GONE
                dataBinding.buttonBack.visibility = visible
            }
        }



        dataBinding.editTextSearchQuery.doAfterTextChanged {
            //sharedViewModel.setQuerySearch(it.toString())
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


        dataBinding.editTextSearchQuery.setDrawableOnClick {
            log("microphone click...")
        }

        dataBinding.editTextSearchQuery.setOnClickListener {
            if (isNotShowSearchPanel()) {
                /*val items = mutableListOf<String>()
                items.add("Samsung")
                items.add("NVidia")
                items.add("AMD")
                items.add("Intel")
                items.add("AOC")
                items.add("Corsair")
                items.add("Kingstone")
                items.add("Xiaomi")*/
                homeViewModel.pushStackMode(HomeViewModel.Companion.HomeMode.SEARCH_QUERY)
                val items = sharedViewModel.getSearchHistoryItems()
                showSearchHistoryPanel(items, (it as EditTextExt).text.toString())
                /*val query = (it as EditTextExt).text.toString()
                if (query.isNotBlank())
                    searchHistoryPanel?.setSearchQuery(query)*/
            }
        }
        /*dataBinding.editTextSearchQuery.setOnFocusChangeListener { v, hasFocus ->
            log("focus changed...")
            if (hasFocus) {
                val items = mutableListOf<String>()
                items.add("Samsung")
                items.add("NVidia")
                items.add("AMD")
                items.add("Intel")
                items.add("AOC")
                items.add("Corsair")
                items.add("Kingstone")
                items.add("Xiaomi")
                showSearchHistoryPanel(items)
                val query = (v as EditTextExt).text.toString()
                if (query.isNotBlank())
                    searchHistoryPanel?.setSearchQuery(query)
            }
        }*/

        dataBinding.buttonBack.setOnClickListener {
            performBack()
        }
        return dataBinding.root
    }


    private fun showSearchHistoryPanel(items: List<String>, start: String) {
        if (isNotShowSearchPanel()) {
            searchHistoryPanel =
                SearchHistoryPanel(dataBinding.layoutRoot, object: OnSearchHistoryListener{
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
        }
        //dataBinding.buttonBack.visibility = View.VISIBLE
    }

    private fun hideSearchHistoryPanel() {
        sharedViewModel.saveSearchHistory()
        searchHistoryPanel?.hide()
        searchHistoryPanel = null
    }

    override fun onStart() {
        super.onStart()
        showUnreadMessage(27)
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
        if (isNotShowSearchPanel())
            dataBinding.editTextSearchQuery.text?.clear()
        else
            hideSearchHistoryPanel()
        homeViewModel.popStackMode()
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


}