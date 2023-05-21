package com.example.shoplocalxml

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.databinding.ActivityMainBinding
import com.example.shoplocalxml.ui.login.LoginFragment
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity(), OnOpenShopListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.appBarMain.fab.visibility = View.GONE
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
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.appBarMain.fab.visibility = View.VISIBLE
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }


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
        navController.popBackStack(R.id.nav_login, true)
        navController.graph.setStartDestination(R.id.nav_home)
        navController.navigate(R.id.nav_home)
        setActionBar()
    }
}