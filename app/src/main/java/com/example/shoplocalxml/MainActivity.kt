package com.example.shoplocalxml

import android.Manifest
import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shoplocalxml.AppShopLocal.Companion.appComponent
import com.example.shoplocalxml.AppShopLocal.Companion.imageDownloadManager
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.custom_view.SnackbarExt
import com.example.shoplocalxml.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale


class MainActivity : AppCompatActivity(), OnOpenShopListener, OnBottomNavigationListener, OnSpeechRecognizer {
    private var animatedFabShow = false
    private var animatedFabHide = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedViewModel: SharedViewModel
    val viewModelComponent = appComponent.viewModelComponent().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        viewModelComponent.inject(this)
        sharedViewModel = viewModelComponent.factory.create(SharedViewModel::class.java)
        lifecycle.addObserver(imageDownloadManager)
        binding = ActivityMainBinding.inflate(layoutInflater)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        sharedViewModel.setOnCloseApp {
            this.finish()
        }
        setContentView(binding.root)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, _, _ ->
            navController.currentDestination?.let{destination ->
                if (destination.id == R.id.nav_login)
                    bottomNavigationView.visibility = View.GONE
                else {
                    if (bottomNavigationView.visibility == View.GONE) {
                        bottomNavigationView.animation = AnimationUtils.loadAnimation(applicationContext, R.anim.slide_in_right)
                        bottomNavigationView.visibility = View.VISIBLE
                        bottomNavigationView.animate()
                    }
                }
            }
            bottomNavigationView.setupWithNavController(navController)
        }
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setColorFilter(this.getColor(R.color.EditTextFont))
          fab.setOnClickListener {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
            val fragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment
            fragment?.let{
                if (it is OnFabListener) {
                    (it as OnFabListener).onFabClick()                }
            }
        }
        if (ActivityCompat.checkSelfPermission(baseContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            log("not granted...")
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS),101)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if ( v is EditTextExt) {
                if (v.lossFocusOutside) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        v.clearFocus()
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun openShop() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        sharedViewModel.getListBrend()
        navController.graph.setStartDestination(R.id.nav_home)
        navController.navigate(R.id.action_nav_login_to_nav_home)
    }

     private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
            val fragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment
            fragment?.let{
                if (it is OnBackPressed) {
                    (it as OnBackPressed).backPressed()
                }
            } ?: run {
            }
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let{ data ->
                (data.extras?.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)).let { matches ->
                    val value = matches?.get(0)
                    if (!value.isNullOrEmpty()) {
                        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                        navHost?.childFragmentManager?.primaryNavigationFragment?.let{fragment ->
                            if (fragment is OnSpeechRecognizer)
                                (fragment as OnSpeechRecognizer).recognize(value)
                        }
                    }

                }
            }
        }
    }

    override fun recognize(value: String?) {
        if (SpeechRecognizer.isRecognitionAvailable(applicationContext)) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getStringResource(R.string.prompt_speechrecognizer))
            resultLauncher.launch(intent)
        } else {
            vibrate(400)
            val snackbarExt = SnackbarExt(binding.root, getStringResource(R.string.message_login_error))
            snackbarExt.type = SnackbarExt.Companion.SnackbarType.ERROR
            snackbarExt.show()
        }

    }

    override fun setVisibilityBottomNavigation(value: Boolean) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        if (value) bottomNavigationView.visibility = View.VISIBLE
        else bottomNavigationView.visibility = View.GONE
    }

    fun setFabVisibility(value: Boolean) {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fun isFabVisibled() = fab.alpha == 1f
        if (value && isFabVisibled()) return
        if (!value && !isFabVisibled()) return
        if (value && animatedFabShow) return
        if (!value && animatedFabHide) return
        val animValueFrom = if (value) 0f else 1f
        val animValueTo   = if (value) 1f else 0f
        val animator =
            ValueAnimator.ofFloat(animValueFrom, animValueTo)
        animator.duration = 300
        animator.addListener(object: AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                if (value) {
                    fab.alpha = 0f
                    fab.visibility = View.VISIBLE
                    animatedFabShow = true
                }
                else
                    animatedFabHide = true
            }

            override fun onAnimationEnd(animation: Animator) {
                animatedFabShow = false
                animatedFabHide = false
                if (!value) {
                    fab.alpha = 0f
                    fab.visibility = View.GONE
                }
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationRepeat(animation: Animator) {
            }
        })
        animator.addUpdateListener { valueAnimator ->
            fab.alpha = valueAnimator.animatedValue as Float
        }
        animator.start()
    }
}