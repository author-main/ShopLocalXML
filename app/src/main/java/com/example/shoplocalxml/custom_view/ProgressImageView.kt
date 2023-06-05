package com.example.shoplocalxml.custom_view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toPx
import kotlinx.coroutines.delay

class ProgressImageView: AppCompatImageView, ValueAnimator.AnimatorUpdateListener {
    enum class ImageState {
        LOADED,
        UPLOADED,
    }
    private var state = ImageState.LOADED
    private val defaultBitmap: Bitmap? = run{
        val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_default)
        drawable?.toBitmap(width = 24.toPx, height = 24.toPx, Bitmap.Config.ARGB_8888)
    }
    private val matrix = Matrix()
    private val gradientWidht = 40.toPx
    private val delta = kotlin.math.sqrt(gradientWidht * gradientWidht/2f)
    private var animatedValue: Float = 0f
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null
    private var gradientBitmap: Bitmap? = null
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        animator = ValueAnimator().apply {
            startDelay = 150
            duration = 1200
            interpolator = DecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener (this@ProgressImageView)
            setFloatValues(-1f, 0f);
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        getGradientDrawable()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (state == ImageState.LOADED && animator?.isRunning == true) {
            gradientBitmap?.let {
                matrix.reset()
                matrix.postRotate(45f)
                matrix.postTranslate(animatedValue, -delta)
                canvas?.drawBitmap(it, matrix, paint)
            }
        }
    }

    fun setImageBitmap(bm: Bitmap?, state: ImageState) {
        this.state = state
        val bitmap = if (state == ImageState.UPLOADED && bm == null) {
                        scaleType = ScaleType.CENTER_INSIDE
                        defaultBitmap
                    }
                     else {
                        scaleType = ScaleType.FIT_CENTER
                        bm
                     }
        setImageBitmap(bitmap)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        if (state == ImageState.LOADED) {
            getGradientDrawable()
            animator?.start()
        } else {
            stopAnimator()
        }
        super.setImageBitmap(bm)
    }

    private fun getGradientDrawable(){
        if (width * height > 0) {
            val colors: IntArray = intArrayOf(
                Color.TRANSPARENT,
                0xffdadada.toInt(),
                Color.TRANSPARENT
            )
            val min = height//minOf(width, height)
            val gradientHeight = (kotlin.math.sqrt((2 * min * min).toDouble()) + 2 * delta).toInt()
            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
            val bitmap = Bitmap.createBitmap(gradientWidht,gradientHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            gradientDrawable.setBounds(0, 0, gradientWidht, gradientHeight)
            gradientDrawable.draw(canvas)
            gradientBitmap = bitmap
            animator?.setFloatValues(-gradientWidht.toFloat(), (width + min).toFloat());
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        animatedValue = animation.animatedValue as Float
        invalidate()
    }

    fun setDefaultDrawable(){
        setImageBitmap(null, ImageState.UPLOADED)
    }

    private fun stopAnimator(){
        gradientBitmap = null
        animator?.end()
    }
}

