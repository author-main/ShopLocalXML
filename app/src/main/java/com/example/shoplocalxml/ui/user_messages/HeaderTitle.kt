package com.example.shoplocalxml.ui.user_messages

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.shoplocalxml.R
import com.example.shoplocalxml.databinding.HeaderTitleBinding

class HeaderTitle(private val parent: ViewGroup, private val title: String, private val onBackPressed:() -> Unit) {
    //private val dataBinding: HeaderTitleBinding = DataBindingUtil.inflate( LayoutInflater.from(parent.context), R.layout.header_title, parent, true)

    init{
        val layoutHeaderTitle =  LayoutInflater.from(parent.context)
            .inflate(R.layout.header_title, parent, false) as FrameLayout

        layoutHeaderTitle.findViewById<TextView>(R.id.textTitle).text = title
        layoutHeaderTitle.findViewById<ImageView>(R.id.buttonBack).setOnClickListener {
            onBackPressed()
        }
        parent.addView(layoutHeaderTitle, 0)
    }
}