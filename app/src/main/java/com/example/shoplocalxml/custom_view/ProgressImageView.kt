package com.example.shoplocalxml.custom_view

import android.R.attr.angle
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import com.example.shoplocalxml.R


class ProgressImageView: AppCompatImageView, ValueAnimator.AnimatorUpdateListener {
    private var animatedValue: Float = 0f
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var animator: ValueAnimator? = null
    private var gradientBitmap: Bitmap? = null
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    init {
        animator = ValueAnimator().apply {
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
        gradientBitmap?.let{canvas?.drawBitmap(it, animatedValue, 0f, paint)}
    }

    override fun setImageBitmap(bm: Bitmap?) {
        if (bm != null) {
            stopAnimator()
        } else {
            getGradientDrawable()
            animator?.start()
        }
        super.setImageBitmap(bm)
    }

    private fun getGradientDrawable(){


        /*val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source,
            0,
            0,
            source.getWidth(),
            source.getHeight(),
            matrix,
            true
        )*/


        if (width * height > 0) {
            val colors: IntArray = intArrayOf(
                Color.TRANSPARENT,
                Color.GRAY,
                Color.TRANSPARENT
            )
            val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
            val bitmap = Bitmap.createBitmap(50, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            gradientDrawable.setBounds(0, 0, 50, height)
            gradientDrawable.draw(canvas)
            gradientBitmap = bitmap
            animator?.setFloatValues(-50f, width.toFloat());
        }

       // canvas.drawRect(0f, 0f, 100f, 500f, paint)*/


        /*   private Bitmap makeRadGrad() {
            RadialGradient gradient = new RadialGradient(200, 200, 200, 0xFFFFFFFF,
                0xFF000000, android.graphics.Shader.TileMode.CLAMP);
            Paint p = new Paint();
            p.setDither(true);
            p.setShader(gradient);

            Bitmap bitmap = Bitmap.createBitmap(400, 400, Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            c.drawCircle(200, 200, 200, p);

            return bitmap;
        }*/
    }

    //private fun drawableIsNull() = drawable == null

    override fun onAnimationUpdate(animation: ValueAnimator) {
        animatedValue = animation.animatedValue as Float
        invalidate()
    }

    fun setDefaultDrawable(){
        setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_default))
        stopAnimator()
    }

    private fun stopAnimator(){
        animator?.end()
        gradientBitmap = null
    }

}