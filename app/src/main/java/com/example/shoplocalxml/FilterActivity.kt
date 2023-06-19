package com.example.shoplocalxml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.example.shoplocalxml.databinding.ActivityFilterBinding
import com.example.shoplocalxml.databinding.ActivityMainBinding

class FilterActivity : AppCompatActivity() {
    lateinit var dataBinding: ActivityFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityFilterBinding.inflate(layoutInflater)
        dataBinding.eventhandler = this
        setContentView(dataBinding.root)
        dataBinding.buttonBackFilter.setOnClickListener {
            finish()
        }
        supportActionBar?.hide()
       /* onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finish()
            }
        })*/
    }
}