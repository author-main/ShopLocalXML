package com.example.shoplocalxml.ui.detail_product

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.example.shoplocalxml.R
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.toPx

class ImageIndicator: LinearLayout {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        context.obtainStyledAttributes(attrs, R.styleable.ImageIndicator).run {
            count = getInt(R.styleable.ImageIndicator_countImages, 1)
            selectedIndex = getInt(R.styleable.ImageIndicator_selectedIndex, 0)
            sizeSym = getInt(R.styleable.ImageIndicator_sizeSymbol, 14)
            recycle()
        }
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    private val arrayTextView = ArrayList<TextView>()
    private var sizeSym = 14
    private val sym = getStringResource(R.string.fCharPassword)
    val interval = 4.toPx
    var count = 1
    var selectedIndex = 0
        set (value) {
            field = value
            if (selectedIndex != value)
                setSelectedIndex(value)

        }

    init {
        addTextView()
    }
    @JvmName("setSelectedIndex_")
    private fun setSelectedIndex(vaue: Int){

    }
    private fun addTextView() {
        val textView = TextView(context)
        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )
        textView.layoutParams = layoutParams
        textView.setPadding(0)
        textView.minWidth = 0
        textView.minHeight = 0
        textView.text = sym
        this.addView(textView)
        arrayTextView.add(textView)
    }
}