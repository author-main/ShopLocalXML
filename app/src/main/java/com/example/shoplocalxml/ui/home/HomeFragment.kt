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
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.FactoryViewModel
import com.example.shoplocalxml.OnBackPressed
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.FragmentHomeBinding
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.history_search.OnSearchHistoryListener
import com.example.shoplocalxml.ui.history_search.SearchHistoryPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnBackPressed {
    private val sharedViewModel: SharedViewModel by activityViewModels(
        factoryProducer = {
            FactoryViewModel(
                this,
                AppShopLocal.repository
            )
        }
    )
    private var searchHistoryPanel: SearchHistoryPanel? = null
    private lateinit var dataBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       /* val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]*/
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
        sharedViewModel.querySearch.observe(viewLifecycleOwner) {
            searchHistoryPanel?.setSearchQuery(it)
        }



        dataBinding.editTextSearchQuery.doAfterTextChanged {
            sharedViewModel.setQuerySearch(it.toString())
        }

        dataBinding.editTextSearchQuery.setOnEditorActionListener { v, _, _ ->
            var result = false
            val query = (v as EditTextExt).text.toString()
            if (query.isNotBlank()) {
                hideKeyboard()
                result = true
                dataBinding.buttonBack.visibility = View.GONE
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
                val items = mutableListOf<String>()
                items.add("Samsung")
                items.add("NVidia")
                items.add("AMD")
                items.add("Intel")
                items.add("AOC")
                items.add("Corsair")
                items.add("Kingstone")
                items.add("Xiaomi")
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
                        dataBinding.buttonBack.visibility = View.GONE
                        hideSearchHistoryPanel()
                    }

                    override fun clickSearchHistoryItem(value: String) {
                        dataBinding.editTextSearchQuery.setText(value)
                    }

                    override fun deleteSearchHistoryItem(value: String) {

                    }
                })
            searchHistoryPanel?.show(items, start)
        }
        dataBinding.buttonBack.visibility = View.VISIBLE
    }

    private fun hideSearchHistoryPanel() {
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
        if (searchHistoryPanel != null) {
            searchHistoryPanel?.hide()
            searchHistoryPanel = null
            dataBinding.buttonBack.visibility = View.GONE
        }
        hideKeyboard()
    }

    private fun hideKeyboard(){
        dataBinding.editTextSearchQuery.clearFocus()
        val imm = applicationContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(dataBinding.editTextSearchQuery.windowToken, 0)
    }

    private fun searchProducts(query: String){

    }

    private fun isNotShowSearchPanel() = searchHistoryPanel == null
}