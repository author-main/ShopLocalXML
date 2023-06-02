package com.example.shoplocalxml

import android.app.Activity
import android.content.Intent
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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.shoplocalxml.AppShopLocal.Companion.repository
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.custom_view.SnackbarExt
import com.example.shoplocalxml.databinding.ActivityMainBinding
import com.example.shoplocalxml.ui.login.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Locale


class MainActivity : AppCompatActivity(), OnOpenShopListener, OnBottomNavigationListener, OnSpeechRecognizer {
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    /*private val sharedViewModel: SharedViewModel by viewModels(factoryProducer = {
        FactoryViewModel(
            this,
            repository
        )
    })*/

    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedViewModel = run {
            val factory = FactoryViewModel(this, repository, savedInstanceState)
            ViewModelProvider(this, factory)[SharedViewModel::class.java]
        }


        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.appBarMain.fab.visibility = View.GONE
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        sharedViewModel.setOnCloseApp {
          //  log("id = ${sharedViewModel.idViewModel}")
            this.finish()
        }

        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_cart, R.id.nav_profile
            )
        )*/
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
    }


   /* private fun setActionBar(){
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
            if (destination.id == R.id.nav_home) {


                /*val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                val fragment = navHostFragment?.childFragmentManager?.findFragmentById(R.id.nav_login)
                navHostFragment?.childFragmentManager?.fragments?.forEach {
                    if (it is HomeFragment)
                        setOnSearchHistoryPanelListener(it as OnSearchHistoryPanelListener)
                }*/
                showUnreadMessage(22)
            }
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
        binding.appBarMain.editTextSearchQuery.doAfterTextChanged {
            sharedViewModel.setQuerySearch(it.toString())
        }
        binding.appBarMain.editTextSearchQuery.setOnEditorActionListener { v, actionId, _ ->
            var result = false
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = (v as EditTextExt).text.toString()
                if (query.isNotBlank()) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    result = true
                    hideSearchHistoryPanel()
                    searchProducts(query)
                }
            }
            result
        }
        binding.appBarMain.editTextSearchQuery.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                navHostFragment?.childFragmentManager?.fragments?.forEach {
                    if (it is HomeFragment)
                        setOnSearchHistoryPanelListener(it as OnSearchHistoryPanelListener)
                }
                showSearchHistoryPanel()
            }
        }



        binding.appBarMain.toolbar.animation = animate
        binding.appBarMain.toolbar.animate()

        //val navController = findNavController(R.id.nav_host_fragment_content_main)


    }*/

    //fun getEditTextSearchQuery() = binding.appBarMain.editTextSearchQuery

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus;
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
        /*val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.visibility = View.VISIBLE*/
      //  setActionBar()
    }

    /*override fun onStart() {
        super.onStart()
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val destination = navController.findDestination(R.id.nav_login)
        destination?.let {
            if (navController.currentDestination != it)
                setActionBar()
        }
    }*/


    /*  private fun showUnreadMessage(count: Int) {
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

    }*/

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
            val fragment = navHostFragment?.childFragmentManager?.primaryNavigationFragment
            fragment?.let{
                if (it is OnBackPressed) {
                    (it as OnBackPressed).backPressed()
                }
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
//        sharedViewModel.getProducts(1, "MCAwIC0xIC0xIDAgMC4wLTAuMCAwIDE=")
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        if (value) bottomNavigationView.visibility = View.VISIBLE
        else bottomNavigationView.visibility = View.GONE
    }
}