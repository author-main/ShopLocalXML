package com.example.shoplocalxml.ui.detail_product

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.size
import com.example.shoplocalxml.R
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toPx


class ImageIndicator: LinearLayout {
    private val images = ArrayList<ImageView>()
    private var sizeSym = 8.toPx
    //private val sym = getStringResource(R.string.fCharPassword)
    private var interval = 4.toPx
    private var count = 1
        set(value) {
            field = value
            setCount(value)
        }
    var selectedIndex = 0
        set (value) {
            if (selectedIndex != value)
                setSelectedIndex(value)
            field = value
        }

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        context.obtainStyledAttributes(attrs, R.styleable.ImageIndicator).run {
            sizeSym = getDimensionPixelSize(R.styleable.ImageIndicator_sizeSymbol, 8.toPx)
            interval = getDimensionPixelSize(R.styleable.ImageIndicator_intervalSymbol, 4.toPx)
            count = getInt(R.styleable.ImageIndicator_countImages, 1)
            selectedIndex = getInt(R.styleable.ImageIndicator_selectedIndex, 0)
            recycle()
        }
        gravity = Gravity.CENTER_VERTICAL
        orientation = LinearLayout.HORIZONTAL
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    private val colorSymbol = context.getColor(R.color.PrimaryDark)
    private val selectedColorSymbol = context.getColor(R.color.EditTextBackgroundDark)

   /* init {
        addImageView()
    }*/
    @JvmName("setSelectedIndex_")
    private fun setSelectedIndex(value: Int){
        log("selected index = $selectedIndex, value = $value")
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), selectedColorSymbol, colorSymbol)
        colorAnimation.duration = 250
        colorAnimation.addUpdateListener { animator -> images[selectedIndex].setColorFilter(animator.animatedValue as Int) }
        colorAnimation.start()
      /*  val colorAnimation1 = ValueAnimator.ofObject(ArgbEvaluator(), colorSymbol, selectedColorSymbol)
        colorAnimation1.duration = 250
        colorAnimation1.addUpdateListener { animator -> images[value].setColorFilter(animator.animatedValue as Int) }
        colorAnimation1.start()*/
    }

   /* override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val paddingWidth = paddingRight + paddingEnd
        val paddingHeight = paddingTop + paddingBottom
        val measureHeight = MeasureSpec.getSize(heightMeasureSpec)
        val heightSymbol = if (measureHeight < sizeSym) sizeSym else measureHeight
        val widthSymbols  = count * sizeSym + interval * (count - 1)
        setMeasuredDimension(widthSymbols + paddingWidth, heightSymbol + paddingHeight)
    }*/

    @JvmName("setCount_")
    private fun setCount(value: Int) {
        val layoutParams = ViewGroup.LayoutParams(
        count * sizeSym + interval * (count - 1),
            sizeSym
        )
        this.layoutParams = layoutParams
        images.clear()
        for (i in 0 until value) {
            addImageView(i)
        }
        //setSelectedIndex(selectedIndex)
    }

    private fun addImageView(index: Int) {
        AppCompatResources.getDrawable(context, R.drawable.ic_circle)?.let {drawable ->
            val imageView = ImageView(context)
            //imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            drawable.setTint(colorSymbol)
            imageView.setImageDrawable(drawable)
            val layoutParams = LayoutParams(
                sizeSym, sizeSym
                //LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            )
            if (index < count - 1)
                layoutParams.marginEnd = interval
            imageView.layoutParams = layoutParams
            //imageView.setPadding(interval, 0, interval, 0)
            images.add(imageView)
            addView(imageView)
        }


 /*       val textView = TextView(context)
        val layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        )
        textView.layoutParams = layoutParams
        textView.setPadding(0)
        textView.minWidth = 0
        textView.minHeight = 0
        textView.text = sym
        textView.textSize = sizeSym
        this.addView(textView)
        arrayTextView.add(textView)*/
    }
}