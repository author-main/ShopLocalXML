package com.example.shoplocalxml.custom_view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatImageView
import com.example.shoplocalxml.toPx

class AnimatedImage: AppCompatImageView, ValueAnimator.AnimatorUpdateListener {
    private val matrix = Matrix()
    private val gradientWidht = 40.toPx
    private val delta = kotlin.math.sqrt(gradientWidht * gradientWidht/2f)
    private var animatedValue: Float = -gradientWidht.toFloat()
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null
    private var gradientBitmap: Bitmap? = null
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    init {
        animator = ValueAnimator().apply {
            //startDelay = 50
            duration = 1200
            interpolator = DecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener (this@AnimatedImage)
            setFloatValues(-1f, 0f)
        }
        visibility = GONE
    }

    fun startAnimation(){
        getGradientDrawable()
        visibility = VISIBLE
        animator?.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        getGradientDrawable()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (animator?.isRunning == true) {
            gradientBitmap?.let {
                matrix.reset()
                matrix.postRotate(45f)
                matrix.postTranslate(animatedValue, -delta)
                canvas.drawBitmap(it, matrix, paint)
            }
        }
    }

    private fun getGradientDrawable(){
        if (width * height > 0) {
            val colors: IntArray = intArrayOf(
                Color.TRANSPARENT,
                0xaadadada.toInt(),
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
            animator?.setFloatValues(-gradientWidht.toFloat(), (width + min).toFloat())
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        animatedValue = animation.animatedValue as Float
        invalidate()
    }

    fun stopAnimation(){
        visibility = GONE
        gradientBitmap = null
        animator?.end()
        animatedValue = -gradientWidht.toFloat()
    }
}