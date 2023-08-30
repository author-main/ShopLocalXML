package com.example.shoplocalxml.ui.user_messages

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.shoplocalxml.R

class HeaderTitle(parent: ViewGroup, title: String, onBackPressed:() -> Unit) {
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