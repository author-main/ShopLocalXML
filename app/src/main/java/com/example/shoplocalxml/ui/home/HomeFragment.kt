package com.example.shoplocalxml.ui.home

import android.animation.ValueAnimator.REVERSE
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
import com.example.shoplocalxml.R
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


class HomeFragment : Fragment() {
    private var countMessages = 7
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var dataBinding: FragmentHomeBinding
    //private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    //private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        showUnreadMessage(countMessages)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        dataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        //dataBinding.eventhandler = sharedViewModel

       /* _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        dataBinding.buttonTask.setOnClickListener {
            val searchHistrotyPanel = SearchHistoryPanel(dataBinding.layoutRoot, object: OnSearchHistoryListener{
                override fun clearHistory() {
                    clearSearchHistory()
                }

                override fun clickItem(value: String) {
                    clickSearchHistoryItem(value)
                }

                override fun deleteItem(value: String) {
                    deleteSearchHistoryItem(value)
                }
            })
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    private fun showUnreadMessage(count: Int) {
        if (count > 0) {
            val buttonMessage = activity?.findViewById<FrameLayout>(R.id.buttonMessage)
            buttonMessage?.let { button ->
                val layoutMessageCount = button.findViewById<FrameLayout>(R.id.layoutMessageCount)
                layoutMessageCount.alpha = 0f
                layoutMessageCount.clearAnimation()

                val textMessageCount = button.findViewById<TextView>(R.id.textMessageCount)
                textMessageCount.text = count.toString()
                textMessageCount.alpha = 0f

                val imageMessage = button.findViewById<ImageView>(R.id.imageMessage)
                imageMessage.bringToFront()

                val imageMessageCount = button.findViewById<ImageView>(R.id.imageMessageCount)
                val center = imageMessageCount.width / 2f
                val animScale = ScaleAnimation(
                    0F, 1F, 0F, 1F,
                    center, center
                )
                animScale.duration = 400

                val animTranslater = TranslateAnimation(-5f, 5f, 0f,0f)
                animTranslater.duration = 30
                animTranslater.repeatCount = 7
                animTranslater.repeatMode = REVERSE

                val animScale1 = ScaleAnimation(
                    1F, 0.56F, 1F, 0.56F,
                    32.toPx.toFloat(), 0f
                    //button.width.toFloat(), 0f
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

    }


    private fun clearSearchHistory(){

    }

    private fun clickSearchHistoryItem(value: String){
        (activity?.findViewById<EditTextExt>(R.id.editTextSearchQuery))?.setText(value)
    }

    private fun deleteSearchHistoryItem(value: String){

    }

}