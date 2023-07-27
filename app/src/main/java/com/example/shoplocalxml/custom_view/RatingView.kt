package com.example.shoplocalxml.custom_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.toPx
import kotlin.math.roundToInt


class RatingView(context: Context, attrs: AttributeSet? = null)
 : View(context, attrs) {
    /*private val df = DecimalFormat("#.#").apply {
        roundingMode = RoundingMode.HALF_EVEN
    }*/
    private var interval = 1.toPx
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmapStar: Bitmap? = null
    private var bitmapBackgroundStar: Bitmap? = null
    private var tintColor = applicationContext.getColor(R.color.colorAccent)
    private var tintBackgroundColor = applicationContext.getColor(R.color.EditTextBackgroundDark)
    private var count = 1f
    //private val minSize = getMinSize()

    init {
            setLayerType(LAYER_TYPE_SOFTWARE, null) // удаления участка canvas
            context.obtainStyledAttributes(attrs, R.styleable.RatingView).run {
                val attrTintColor = getColor(R.styleable.RatingView_tintStar, -1)
                val attrTintBackgroundColor = getColor(R.styleable.RatingView_tintBackgroundStar, -1)
                if (attrTintColor != -1) {
                    tintColor = attrTintColor
                }
                if (attrTintBackgroundColor != -1)
                    tintBackgroundColor = attrTintBackgroundColor
                count = getFloat(R.styleable.RatingView_count, 1f).coerceIn(1f, 5f)
                interval = getDimensionPixelSize(R.styleable.RatingView_intervalStar, 1)
                recycle()
            }
    }

   /* private fun getMinSize(): Size {
        val minValue = 8.toPx
        val minWidth  = minValue * 5 + minValue/2 // minValue/2 = 4 * 1dp -> расстояние между звездами
        return Size(0,minValue)
    }*/

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        val step = bitmapStar?.let {
            (it.width + interval).toFloat()
        } ?: 0f
        var posX = paddingStart.toFloat()
        val posY = paddingTop.toFloat()
        val intPart: Int = count.toInt()
        var remains: Float = count - intPart
        var lastPos = 0f
        for (i in 0..4) {
            if (i < intPart)
                bitmapStar?.let {
                    canvas.drawBitmap(it, posX, posY, paint)
                }
            else
                bitmapBackgroundStar?.let {
                    canvas.drawBitmap(it, posX, posY, paint)
                    if (lastPos == 0f)
                        lastPos = posX
                }
            posX += step
        }

        bitmapStar?.let {
            if (remains > 0f) {
                remains = (remains * 10.0f).roundToInt() / 10.0f // округляем до одного знака
                val widthStar = remains * it.width
                val rect = RectF().apply {
                    set(lastPos, posY, lastPos+widthStar, posY + it.height.toFloat())
                }
                paint.color = Color.TRANSPARENT
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                canvas.drawRect(rect, paint)
                paint.reset()
                val rectSource =  Rect().apply {
                    set(0, 0, widthStar.toInt(), it.height)
                }

                canvas.drawBitmap(it, rectSource, rect, paint)
                //canvas?.drawBitmap(it, 0f, 0f, paint)
            }
        }


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val paddingWidth = paddingRight + paddingEnd
        val paddingHeight = paddingTop + paddingBottom
        val heightStars = MeasureSpec.getSize(heightMeasureSpec)
        val widthStars  = heightStars * 5 + interval * 4
        val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_star)
        drawable?.setTint(tintColor)
        bitmapStar = drawable?.toBitmap(heightStars, heightStars, Bitmap.Config.ARGB_8888)
        drawable?.setTint(tintBackgroundColor)
        bitmapBackgroundStar = drawable?.toBitmap(heightStars, heightStars, Bitmap.Config.ARGB_8888)
        setMeasuredDimension(widthStars + paddingWidth, heightStars + paddingHeight)
    }

    /*val drawable = attrs?.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", -1)
    drawable?.let{
        stateDrawable = AppCompatResources.getDrawable(AppShopLocal.applicationContext, it) as StateListDrawable
        stateDrawable?.let{stateListDrawable->
            val savedState = stateListDrawable.state
            stateListDrawable.state = intArrayOf(android.R.attr.state_checked)
            checkedDrawable = stateListDrawable.current
            stateListDrawable.state = intArrayOf(-android.R.attr.state_checked)
            uncheckedDrawable = stateListDrawable.current
            stateListDrawable.state = savedState

        }
    }*/

   /* override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        log("resize $w, $h")
    }*/
    fun setCount(value: Float){
        count = value
        invalidate()
    }
}
