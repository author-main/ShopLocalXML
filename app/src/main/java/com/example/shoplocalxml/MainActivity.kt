package com.example.shoplocalxml

import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.shoplocalxml.AppShopLocal.Companion.repository
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.ActivityMainBinding
import com.example.shoplocalxml.ui.history_search.OnSearchHistoryListener
import com.example.shoplocalxml.ui.login.LoginFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), OnOpenShopListener, OnSearchHistoryListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

   private val sharedViewModel: SharedViewModel by viewModels(factoryProducer = {
        FactoryViewModel(
            this,
            repository
        )
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.appBarMain.toolbar.visibility  = View.GONE
        binding.appBarMain.fab.visibility = View.GONE
        setContentView(binding.root)
    }


    private fun setActionBar(){
        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.nav_home)
                showUnreadMessage(22)
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.appBarMain.fab.visibility = View.VISIBLE

        binding.appBarMain.toolbar.navigationIcon = null
        val animate = AnimationUtils.loadAnimation(this,
            R.anim.slide_in_top)
        binding.appBarMain.toolbar.visibility  = View.VISIBLE
        val buttonMessage = binding.appBarMain.includeButtonMessage.buttonMessage
        buttonMessage.setOnClickListener {
            showUserMessages()
        }
        binding.appBarMain.toolbar.animation = animate
        binding.appBarMain.toolbar.animate()

        //val navController = findNavController(R.id.nav_host_fragment_content_main)


    }

    fun getEditTextSearchQuery() = binding.appBarMain.editTextSearchQuery

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus;
            if ( v is EditTextExt) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun openShop() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
       /* val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        val fragment = navHostFragment?.childFragmentManager?.fragments?.get(0)
        fragment?.let{
            val transaction = navHostFragment.childFragmentManager.beginTransaction()
                transaction.remove(it as LoginFragment)
                val result = transaction.commit()
            log("remove fragment $result...")
        }*/
        /*
        вместо
        navController.popBackStack(R.id.nav_login, true)
        для анимации используем в action
            app:popUpTo="@+id/nav_login"
            app:popUpToInclusive="true"
         */
        navController.graph.setStartDestination(R.id.nav_home)
        navController.navigate(R.id.action_nav_login_to_nav_home)
        setActionBar()
    }

     override fun onStart() {
        super.onStart()
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val destination = navController.findDestination(R.id.nav_login)
        destination?.let {
            if (navController.currentDestination != it)
                setActionBar()
        }
    }


    private fun showUnreadMessage(count: Int) {
        if (count > 0) {
           /* val buttonMessage = binding.appBarMain.includeButtonMessage.buttonMessage
                //activity?.findViewById<FrameLayout>(R.id.buttonMessage)
            buttonMessage.let { button ->*/
                val layoutMessageCount = binding.appBarMain.includeButtonMessage.layoutMessageCount
                layoutMessageCount.alpha = 0f
                layoutMessageCount.clearAnimation()

                val textMessageCount = binding.appBarMain.includeButtonMessage.textMessageCount
                textMessageCount.text = count.toString()
                textMessageCount.alpha = 0f

                val imageMessage = binding.appBarMain.includeButtonMessage.imageMessage
                imageMessage.bringToFront()

                val imageMessageCount = binding.appBarMain.includeButtonMessage.imageMessageCount
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
            //}
        }

    }


    override fun clearHistory() {

    }

    override fun clickItem(value: String) {
        binding.appBarMain.editTextSearchQuery.setText(value)
    }

    override fun deleteItem(value: String) {

    }

    private fun showUserMessages() {
        log("show user messages...")
    }
}