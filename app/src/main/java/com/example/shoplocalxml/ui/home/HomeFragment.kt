package com.example.shoplocalxml.ui.home

import android.animation.ValueAnimator.REVERSE
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.MainActivity
import com.example.shoplocalxml.R
import com.example.shoplocalxml.SharedViewModel
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.FragmentHomeBinding
import com.example.shoplocalxml.toPx
import com.example.shoplocalxml.ui.history_search.OnSearchHistoryListener
import com.example.shoplocalxml.ui.history_search.SearchHistoryPanel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var dataBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
         dataBinding = FragmentHomeBinding.inflate(inflater, container, false)
         dataBinding.buttonTask.setOnClickListener {
            val searchHistrotyPanel = SearchHistoryPanel(dataBinding.layoutRoot, activity as OnSearchHistoryListener)
            val items = mutableListOf<String>()
            items.add("Samsung")
            items.add("NVidia")
            items.add("AMD")
            items.add("Intel")
            items.add("AOC")
            items.add("Corsair")
            items.add("Kingstone")
            items.add("Xiaomi")
            searchHistrotyPanel.show(items)
        }
        return dataBinding.root
    }
}