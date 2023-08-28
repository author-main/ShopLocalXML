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
    //private lateinit var appBarConfiguration: AppBarConfiguration
    private var animatedFabShow = false
    private var animatedFabHide = false
    private lateinit var binding: ActivityMainBinding

    /*@Inject
    lateinit var imageDownloadManager: ImageDownloadManager*/

    /*private val sharedViewModel: SharedViewModel by viewModels(factoryProducer = {
        FactoryViewModel(
            this/*,
            repository*/
        )
    })*/
    private lateinit var sharedViewModel: SharedViewModel

    //lateinit var sharedViewModel: SharedViewModel
    val viewModelComponent = appComponent.viewModelComponent().build()


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        //log("create MainActivity...")
        //viewModelComponent = appComponent.viewModelComponent().build()
        viewModelComponent.inject(this)
        sharedViewModel = viewModelComponent.factory.create(SharedViewModel::class.java)

        //log("shop local...")
        //lifecycle.addObserver(ImageDownloadManager.getInstance())
        lifecycle.addObserver(imageDownloadManager)



        //val size = getDisplaySize()

       /* sharedViewModel = run {
            val factory = FactoryViewModel(repository)
            ViewModelProvider(this, factory)[SharedViewModel::class.java]
        }*/

        /*sharedViewModel = run {
            val factory = FactoryViewModel(this, repository)
            ViewModelProvider(this, factory)[SharedViewModel::class.java]
        }*/

        binding = ActivityMainBinding.inflate(layoutInflater)
        //binding.appBarMain.fab.visibility = View.GONE
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
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setColorFilter(this.getColor(R.color.EditTextFont))
      /*  val marginFab = bottomNavigationView.height + 16.toPx
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = marginFab
        params.marginEnd    = 16.toPx
        binding.appBarMain.fab.layoutParams = params*/


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

        /*val requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        }*/




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
        //log("open shop...")
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
        sharedViewModel.getListBrend()
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
            } ?: run {
                //log("product_detail")
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

    /*override fun onStop() {
        super.onStop()
      //  log("activity onstop....")
    }*/

  /*  override fun onStart() {
        super.onStart()
       // log("activity onstart....")
    }*/


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
       // log("start animation $value, alpha = ${binding.appBarMain.fab.alpha}...")
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