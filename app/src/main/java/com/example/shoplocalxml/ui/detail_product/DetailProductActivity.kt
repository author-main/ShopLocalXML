package com.example.shoplocalxml.ui.detail_product

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoplocalxml.databinding.ActivityDetailProductBinding
import com.example.shoplocalxml.databinding.ActivityFilterBinding

class DetailProductActivity: AppCompatActivity() {
    private lateinit var dataBinding: ActivityDetailProductBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        dataBinding = ActivityDetailProductBinding.inflate(layoutInflater)
    }
}