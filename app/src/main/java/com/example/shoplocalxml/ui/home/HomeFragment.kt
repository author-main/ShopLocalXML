package com.example.shoplocalxml.ui.home

import android.animation.ValueAnimator.REVERSE
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
import androidx.lifecycle.ViewModelProvider
import com.example.shoplocalxml.R
import com.example.shoplocalxml.databinding.FragmentHomeBinding
import com.example.shoplocalxml.toPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private var countMessages = 7
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
        dataBinding.eventhandler = homeViewModel

       /* _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return dataBinding.root
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
}