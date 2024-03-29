package com.example.shoplocalxml.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.animation.DecelerateInterpolator
import com.example.shoplocalxml.toPx

class ZoomImageView: androidx.appcompat.widget.AppCompatImageView, GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
    private var onScaleImage: ((Boolean) -> Unit)? = null
    fun setOnScaleImage(value: (Boolean)-> Unit) {
        onScaleImage = value
    }
    private var flingAnimate = false
    private var animDoubleZoom: DoubleTapAnimator? = null
    private val clickOffset = 3
    private var startTouch = PointF(0f, 0f)
    private var widthDrawable = 0f
    private var heightDrawable = 0f
    private var widthView = 0f
    private var heightView = 0f
    private enum class ZoomMode {NONE, ZOOM, MOVE}
    private var mode = ZoomMode.NONE
    private val matrix = Matrix()
    private var minScale = 1f
    private val maxScale = 5f
    private var saveScale    = 1f
    private var gestureDetector = GestureDetector(context, this).apply {
        setOnDoubleTapListener(this@ZoomImageView)
    }
    private var scaleDetector   = ScaleGestureDetector(context, ScaleListener())
    private var lastTouchX = 0f
    private var lastTouchY = 0f

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private fun roundScale(value: Float) =
        (value * 1000).toInt()

    private fun isScaledImage() =
        roundScale(minScale) != roundScale(saveScale)

    override fun setImageURI(uri: Uri?) {
        val widthBorder = 5.toPx
        super.setImageURI(uri)
        val sourceBitmap = (drawable as BitmapDrawable).bitmap
        val bitmap = Bitmap.createBitmap(
            sourceBitmap.width + widthBorder * 2,
            sourceBitmap.height + widthBorder * 2,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(
            sourceBitmap,
            Rect(0, 0, sourceBitmap.width, sourceBitmap.height),
            Rect(widthBorder, widthBorder, bitmap.width - widthBorder, bitmap.height - widthBorder),
            null
        )
        setImageDrawable(BitmapDrawable(resources, bitmap))
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT)
        if (drawable != null) {
            val bitmap = (drawable as BitmapDrawable).bitmap
            canvas.drawBitmap(
                bitmap, matrix,
                null
            )
        }
    }

    fun setOriginalScale(): Boolean{
       if (roundScale(saveScale) != roundScale(minScale)) {
            saveScale = minScale
            matrix.reset()
            matrix.postScale(
                saveScale, saveScale
            )
            matrix.postTranslate(
                (widthView - widthDrawable * saveScale) / 2f,
                (heightView - heightDrawable * saveScale) / 2f
            )
           return true
        }
        return false
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthView = w.toFloat()
        heightView = h.toFloat()
        val bitmap = (drawable as BitmapDrawable).bitmap
        widthDrawable  = bitmap.width.toFloat()
        heightDrawable = bitmap.height.toFloat()
        lastTouchX = 0f
        lastTouchY = 0f
        val calcScale = (w.toFloat() / widthDrawable).coerceAtMost(h.toFloat() / heightDrawable)
        saveScale = if (calcScale > maxScale) maxScale else calcScale
        minScale = saveScale
        matrix.postScale(
            saveScale, saveScale
        )
        matrix.postTranslate(
            (w - widthDrawable * saveScale) / 2f,
            (h - heightDrawable * saveScale) / 2f
        )
    }

    private fun getTranslatePos(): PointF {
        val matrixValue = FloatArray(9)
        matrix.getValues(matrixValue)
        return PointF (matrixValue[Matrix.MTRANS_X], // извлекаем из matrix перемещение x, y
                       matrixValue[Matrix.MTRANS_Y]
               )
    }


    private fun limitMove(delta: Float, sizeView: Float, sizeContent: Float) =
        if (sizeView >= sizeContent) 0f else delta

    private fun limitTranslate(){
        val transPos = getTranslatePos()
        val x = placeBound(transPos.x, widthView,  widthDrawable  * saveScale)
        val y = placeBound(transPos.y, heightView, heightDrawable * saveScale)
        if (x != 0f || y!=0f)
            matrix.postTranslate(x, y)
    }

    private fun placeBound(value: Float, viewSize: Float, contentSize: Float): Float {
        val offsetFrom: Float
        val offsetTo: Float
        if (contentSize <= viewSize) {
            offsetFrom = 0f
            offsetTo = viewSize - contentSize
        } else {
            offsetFrom = viewSize - contentSize
            offsetTo   = 0f
        }
        if (value < offsetFrom)
            return -value + offsetFrom
        if (value > offsetTo)
            return -value + offsetTo
        return 0f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (minScale == maxScale) return true
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        val currX = event.x
        val currY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onScaleImage?.invoke(isScaledImage())
                flingAnimate = false
                mode = ZoomMode.MOVE
                lastTouchX = event.x
                lastTouchY = event.y
                startTouch.x = event.x
                startTouch.y = event.y
            }
            MotionEvent.ACTION_POINTER_UP -> {
                mode = ZoomMode.NONE
            }
            MotionEvent.ACTION_UP -> {
                mode = ZoomMode.NONE
                val xDiff = kotlin.math.abs(currX - startTouch.x)
                val yDiff = kotlin.math.abs(currY - startTouch.y)
                if (xDiff < clickOffset && yDiff < clickOffset)
                    performClick()
            }
           MotionEvent.ACTION_MOVE -> {
                if (mode == ZoomMode.MOVE) {
                        val dx = currX - lastTouchX
                        val dy = currY - lastTouchY
                        val nX = limitMove(dx, widthView, widthDrawable * saveScale )
                        val nY = limitMove(dy, heightView, heightDrawable * saveScale )
                        matrix.postTranslate(
                            nX,
                            nY
                        )
                        limitTranslate()
                        invalidate()
                        lastTouchX = currX
                        lastTouchY = currY
                }
           }
        }
        return true
    }

    private fun onAnimateMove(dx: Float, dy: Float, duration: Long) {
        val animateStart = Matrix(matrix)
        val posTrans = getTranslatePos()
        val animateInterpolator = DecelerateInterpolator()
        val startTime = System.currentTimeMillis()
        val endTime = startTime + duration
        val w = widthDrawable  * saveScale
        val h = heightDrawable * saveScale
        val maxOffsetX = (w - widthView).coerceAtLeast(0f)
        val maxOffsetY = (h - heightView).coerceAtLeast(0f)
        val minPosX = ((widthView  - w) / 2f).coerceAtLeast(0f)
        val minPosY = ((heightView - h) / 2f).coerceAtLeast(0f)

        fun onAnimateStep() {
            fun getOffset(curTrans: Float, deltaTrans: Float, minPos: Float, maxOffset: Float): Float {
                if (maxOffset == 0f) return 0f
                val calcTrans = curTrans + deltaTrans
                if (calcTrans > minPos) {
                    return minPos - curTrans
                } else {
                    if (kotlin.math.abs(calcTrans) > maxOffset )
                        return - (maxOffset + curTrans)
                }
                return deltaTrans
            }
            val curTime = System.currentTimeMillis()
            val percentTime = (curTime - startTime).toFloat() / (endTime - startTime).toFloat()
            val percentDistance: Float = animateInterpolator
                .getInterpolation(percentTime)
            val tdX = percentDistance * dx
            val tdY = percentDistance * dy
            val curDx = getOffset(posTrans.x, tdX, minPosX, maxOffsetX)
            val curDy = getOffset(posTrans.y, tdY, minPosY, maxOffsetY)
            matrix.set(animateStart)
            matrix.postTranslate(curDx, curDy)
            invalidate()
            if (!flingAnimate)
                return
            if (percentTime < 1.0f)
                post { onAnimateStep() }
        }
        if (maxOffsetX == 0f && maxOffsetY == 0f)
            return
        flingAnimate = true
        post {
            onAnimateStep()
        }
    }

    override fun onDown(e: MotionEvent): Boolean {return false}
    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean {return false}
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {return false}
    override fun onLongPress(e: MotionEvent) {}
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val distanceTimeFactor = 0.4f
        val totalDx = (distanceTimeFactor * velocityX/2)
        val totalDy = (distanceTimeFactor * velocityY/2)
        onAnimateMove(totalDx, totalDy,
            (1000 * distanceTimeFactor).toLong())
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {return false}

    override fun onDoubleTap(e: MotionEvent): Boolean {
        if (minScale == maxScale) return true
        val origScale = saveScale
        val transPos = getTranslatePos()
        saveScale = if (saveScale == maxScale)
            minScale
        else
            maxScale
        matrix.reset()
        val centerX = (widthView - widthDrawable   * saveScale) / 2f
        val centerY = (heightView - heightDrawable * saveScale) / 2f
        animDoubleZoom = DoubleTapAnimator(transPos, PointF(centerX, centerY), origScale, saveScale)
        post(
            animDoubleZoom!!
        )
        return false
    }

    private fun stopZoomAnimate(){
        animDoubleZoom?.let {
            removeCallbacks(it)
            animDoubleZoom = null
        }
    }

    private inner class DoubleTapAnimator(
        private val startTrans: PointF,
        private val endTrans:   PointF,
        private val startScale: Float,
        private val endScale:   Float
    ): Runnable{
        private var scale = startScale
        private var x = startTrans.x
        private var y = startTrans.y
        private val fraction = 10f
        private var stepScale = (endScale - startScale) / fraction
        private var stepX = (endTrans.x - startTrans.x) / fraction
        private var stepY = (endTrans.y - startTrans.y) / fraction
        override fun run() {
            scale   += stepScale
            x       += stepX
            y       += stepY
            var rangeFrom = startScale
            var rangeTo   = endScale
            if (startScale > endScale) {
                rangeFrom = endScale
                rangeTo   = startScale
            }
            if (roundScale(scale) !in roundScale(rangeFrom)..roundScale(rangeTo)){
                stopZoomAnimate()
                return
            }
            matrix.reset()
            matrix.postScale(scale, scale)
            matrix.postTranslate(x, y)
            invalidate()
            post(this)
         }
    }

    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        return false
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
           if (minScale != maxScale) {
               mode = ZoomMode.ZOOM
               onScaleImage?.invoke(true)
           }
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (minScale == maxScale) return true
            var scaleFactor = detector.scaleFactor
            val origScale = saveScale
            saveScale *= scaleFactor
            if (saveScale > maxScale) {
                saveScale = maxScale
                scaleFactor = maxScale / origScale
            } else if (saveScale < minScale) {
                saveScale = minScale
                scaleFactor = minScale / origScale
            }
            if (widthDrawable * saveScale <= widthView
                || heightDrawable * saveScale <= heightView)
                matrix.postScale(scaleFactor, scaleFactor, widthView / 2,
                    heightView / 2)
            else
                matrix.postScale(scaleFactor, scaleFactor,
                    detector.focusX, detector.focusY
                )
            limitTranslate()
            invalidate()
            return true
        }
    }
}