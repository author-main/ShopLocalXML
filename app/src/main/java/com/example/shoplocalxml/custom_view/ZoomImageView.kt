package com.example.shoplocalxml.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.example.shoplocalxml.log
import kotlin.math.abs


class ZoomImageView: androidx.appcompat.widget.AppCompatImageView, GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
    private var transFlingX = 0f
    private var transFlingY = 0f
    private val handlerUI = Handler(Looper.getMainLooper())
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
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthView = w.toFloat()
        heightView = h.toFloat()
        val bitmap = (drawable as BitmapDrawable).bitmap
        widthDrawable  = bitmap.width.toFloat()
        heightDrawable = bitmap.height.toFloat()
        lastTouchX = 0f
        lastTouchY = 0f
        saveScale =
            (w.toFloat() / widthDrawable).coerceAtMost(h.toFloat() / heightDrawable)
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
        matrix.getValues(matrixValue);
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
        var offsetFrom = 0f
        var offsetTo   = 0f

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


  /*  private fun updateMatrixFling(){
        matrix.reset()
        matrix.postScale(saveScale, saveScale)
        matrix.postTranslate(transFlingX, transFlingY)
        limitTranslate()
        invalidate()
    }*/

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        val currX = event.x
        val currY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
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
                val xDiff = abs (currX - startTouch.x)
                val yDiff = abs (currY - startTouch.y)
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
             //OvershootInterpolator()
        val startTime = System.currentTimeMillis()
        val endTime = startTime + duration

        fun onAnimateStep() {
            val curTime = System.currentTimeMillis()
            val percentTime = (curTime - startTime).toFloat() / (endTime - startTime).toFloat()

            val w = widthDrawable  * saveScale
            val h = heightDrawable * saveScale
            val maxOffsetX = (w - widthView).toFloat().coerceAtLeast(0f)
            val maxOffsetY = (h - heightView).toFloat().coerceAtLeast(0f)

            val percentDistance: Float = animateInterpolator
                .getInterpolation(percentTime)
            val curDx = if (maxOffsetX > 0f) {
                /*val direction = if (dx < 0) -1 else 1
                val delta = percentDistance * dx
                val offsetX = abs(posTrans.x) + abs(delta)
                if (offsetX > maxOffsetX) {
                    direction * maxOffsetX - abs(posTrans.x)
                } else*/
                percentDistance * dx
            } else 0f
            val curDy = if (maxOffsetY > 0f) {
                percentDistance * dy
            } else 0f
            matrix.set(animateStart)
            matrix.postTranslate(curDx, curDy);
            invalidate();
            if (percentTime < 1.0f)
                post { onAnimateStep() }
        }

        post {
            onAnimateStep()
        }
    }





    override fun onDown(e: MotionEvent): Boolean {return false}
    override fun onShowPress(e: MotionEvent) {}
    override fun onSingleTapUp(e: MotionEvent): Boolean {return false}
    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {return false}
    override fun onLongPress(e: MotionEvent) {}
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
//        log("${e1.x}, ${e1.y} - ${e2.x}, ${e2.y}")
        val distanceTimeFactor = 0.4f
        val totalDx = (distanceTimeFactor * velocityX/2)
        val totalDy = (distanceTimeFactor * velocityY/2)

        onAnimateMove(totalDx, totalDy,
            (1000 * distanceTimeFactor).toLong())
        return true
    }
    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {return false}

    override fun onDoubleTap(e: MotionEvent): Boolean {
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
        handlerUI.post(
            animDoubleZoom!!
        )
        return false
    }

    private fun stopZoomAnimate(){
        animDoubleZoom?.let {
            handlerUI.removeCallbacks(it)
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
            var rangeFrom = startScale
            var rangeTo   = endScale
            /*if (startScale < endScale) {
                rangeFrom = startScale
                rangeTo   = endScale
            } else*/
            if (startScale > endScale) {
                rangeFrom = endScale
                rangeTo   = startScale
            }
            if (scale  !in rangeFrom..rangeTo) {
                stopZoomAnimate()
                return
            }
            x       += stepX
            y       += stepY
            matrix.reset()
            matrix.postScale(scale, scale)
            matrix.postTranslate(x, y)
            invalidate()
            postOnAnimation(this)
        }
    }


    override fun onDoubleTapEvent(e: MotionEvent): Boolean {
        return false
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            mode = ZoomMode.ZOOM
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {

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