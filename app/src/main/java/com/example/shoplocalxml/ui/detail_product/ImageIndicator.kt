package com.example.shoplocalxml.ui.detail_product

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.example.shoplocalxml.R
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.toPx

class ImageIndicator: LinearLayout {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        context.obtainStyledAttributes(attrs, R.styleable.ImageIndicator).run {
            count = getInt(R.styleable.ImageIndicator_countImages, 1)
            selectedIndex = getInt(R.styleable.ImageIndicator_selectedIndex, 0)
            recycle()
        }
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    val indicatorSym = getStringResource(R.string.fCharPassword)
    val interval = 4.toPx
    var count = 1
    var selectedIndex = 0
        set (value) {
            field = value
            if (selectedIndex != value)
                setSelectedIndex(value)

        }
    @JvmName("setSelectedIndex_")
    private fun setSelectedIndex(vaue: Int){

    }
}