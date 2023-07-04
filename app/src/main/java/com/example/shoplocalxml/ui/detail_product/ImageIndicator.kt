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
import com.example.shoplocalxml.alpha
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ImageIndicator: LinearLayout {
    private var initialized = false
    private val images = ArrayList<ImageView>()
    private var sizeSym = 8.toPx
    //private val sym = getStringResource(R.string.fCharPassword)
    private var interval = 4.toPx
    var count = 0
        set(value) {
            field = value
            setCount(value)
        }
    private var prevIndex = -1
    var selectedIndex = -1
        set (value) {
            prevIndex = field
            field = getNormalizeSelectedIndex(value)
            setSelectedIndex(field)
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
    private val colorSymbol = context.getColor(R.color.EditTextBackgroundDark)
    private val selectedColorSymbol = context.getColor(R.color.colorBrend)

    private fun getNormalizeSelectedIndex(value: Int): Int {
        return if (count == 0 || value < 0) -1
        else
            if (value > count - 1) count - 1
            else value
    }

    @JvmName("setSelectedIndex_")
    private fun setSelectedIndex(value: Int){
        if (!initialized) {
            if (value > -1)
                images[value].setColorFilter(selectedColorSymbol)
            initialized = true
            return
        }
      //  log( "$prevIndex, $selectedIndex")

            CoroutineScope(Dispatchers.Main).launch {
                if (prevIndex != -1) {
                    val colorAnimation =
                        ValueAnimator.ofObject(ArgbEvaluator(), selectedColorSymbol, colorSymbol)
                    colorAnimation.duration = 350
                    colorAnimation.addUpdateListener { animator ->
                        val color = animator.animatedValue as Int
                        images[prevIndex].setColorFilter(color)
                    }
                    colorAnimation.start()
                }

                if (value != -1) {
                    val colorAnimation1 =
                        ValueAnimator.ofObject(ArgbEvaluator(), colorSymbol, selectedColorSymbol)
                    colorAnimation1.duration = 350
                    colorAnimation1.addUpdateListener { animator ->
                        images[value].setColorFilter(
                            animator.animatedValue as Int
                        )
                    }
                    colorAnimation1.start()
                }
            }


    }

    @JvmName("setCount_")
    private fun setCount(value: Int) {
        initialized = false
        this.removeAllViews()
        images.clear()
        val widthView = value * sizeSym + interval * (value - 1)
        val heightView = sizeSym

       val params = if (layoutParams == null)
            ViewGroup.LayoutParams(
                widthView,
                heightView
            ) else {
                layoutParams.apply {
                    width = widthView
                    height = heightView
                }
        }




        /*val layoutParams = ViewGroup.LayoutParams(
        value * sizeSym + interval * (value - 1),
            sizeSym
        )
        this.layoutParams = layoutParams*/
        layoutParams = params

        for (i in 0 until value) {
            addImageView(i)
        }
    }

    private fun addImageView(index: Int) {
        AppCompatResources.getDrawable(context, R.drawable.ic_circle)?.let {drawable ->
            val imageView = ImageView(context)
            drawable.setTint(colorSymbol)
            imageView.setImageDrawable(drawable)
            val layoutParams = LayoutParams(
                sizeSym, sizeSym
                //LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
            )
            if (index < count - 1)
                layoutParams.marginEnd = interval
            imageView.layoutParams = layoutParams
            images.add(imageView)
            addView(imageView)
        }
    }
}