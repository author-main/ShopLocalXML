package com.example.shoplocalxml.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.example.shoplocalxml.log


class ZoomImageView: androidx.appcompat.widget.AppCompatImageView {
    private enum class ZoomMode {NONE, ZOOM, MOVE, CLICK}
    private var mode = ZoomMode.NONE
    private val matrix = Matrix()
    private val minScale = 1f
    private val maxScale = 3f
    private var scale    = 1f
    private var scaleDetector: ScaleGestureDetector? = null
    private var pivotPointX = 0f
    private var pivotPointY = 0f
    private var posX = 0f
    private var posY = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        //scaleType = ScaleType.MATRIX
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
    }

     override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        if (drawable != null) {
            canvas.save()
            canvas.translate(posX, posY)
            matrix.postScale(
                scale, scale,
                pivotPointX,
                pivotPointY
            )
            val bitmap = (drawable as BitmapDrawable).bitmap
            canvas.drawBitmap(
                bitmap, matrix,
                null
            )
            canvas.restore()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val width = bitmap.width
        val height = bitmap.height
        lastTouchX = 0f
        lastTouchY = 0f
        scale =
            (w.toFloat() / width).coerceAtMost(h.toFloat() / height)
        posX = (w  - width * scale) / 2f
        posY = (h - height * scale) / 2f
    }

   /* override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        val width = drawable.intrinsicWidth
        val height = drawable.intrinsicHeight
        posX = 0f
        posY = 0f
        lastTouchX = 0f
        lastTouchY = 0f
        scale =
            (layoutParams.width.toFloat() / width).coerceAtMost(layoutParams.height.toFloat() / height)
        pivotPointX =
            (layoutParams.width  - width * scale) / 2f
        pivotPointY =
            (layoutParams.height - height * scale) / 2f
    }*/





    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector?.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            //return super.onScale(detector)
            mode = ZoomMode.ZOOM
            return true
        }

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            var detectorScale = detector.scaleFactor
            val curScale = scale
            scale *= detectorScale
            if (scale > maxScale) {
                scale = maxScale
                detectorScale = maxScale / curScale
            } else if (scale < minScale) {
                scale = minScale
                detectorScale = minScale / curScale
            }


          /*  var mScaleFactor = detector.scaleFactor
            val origScale = saveScale
            saveScale *= mScaleFactor
            if (saveScale > maxScale) {
                saveScale = maxScale
                mScaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                mScaleFactor = minScale / origScale
            }
            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) matri!!.postScale(mScaleFactor, mScaleFactor, viewWidth / 2.toFloat(), viewHeight / 2.toFloat()) else matri!!.postScale(mScaleFactor, mScaleFactor, detector.focusX, detector.focusY)
            fixTrans()*/
            return true
        }
    }
}