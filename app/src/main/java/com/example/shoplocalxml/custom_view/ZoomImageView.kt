package com.example.shoplocalxml.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.INVALID_POINTER_ID
import android.view.ScaleGestureDetector
import com.example.shoplocalxml.log


class ZoomImageView: androidx.appcompat.widget.AppCompatImageView {
    private val matrixDraw = Matrix()
    private var widthDrawable = 0f
    private var heightDrawable = 0f

    private var widthView = 0f
    private var heightView = 0f

    /*private enum class ZoomMode {NONE, ZOOM, MOVE, CLICK}
    private var mode = ZoomMode.NONE*/
    private val matrix = Matrix()
    private val minScale = 1f
    private val maxScale = 7f
    private var scale    = 1f
    private var scaleDetector: ScaleGestureDetector? = null
    private var pivotPointX = 0f
    private var pivotPointY = 0f
    private var posX = 0f
    private var posY = 0f
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var activePointerId = INVALID_POINTER_ID

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
    }

     override fun onDraw(canvas: Canvas) {
        //super.onDraw(canvas)
        canvas.drawColor(Color.TRANSPARENT)
        if (drawable != null) {
            /*val data = floatArrayOf()
            matrix.getValues(data)*/
            matrixDraw.set(matrix)
            /*canvas.save()
            canvas.translate(posX, posY)*/
            //matrixDraw.postTranslate(posX, posY)
            /*matrix.postScale(
                scale, scale,
                pivotPointX,
                pivotPointY
            )*/
            matrixDraw.postTranslate(posX, posY)

            val bitmap = (drawable as BitmapDrawable).bitmap
            canvas.drawBitmap(
                bitmap, matrixDraw,
                null
            )

           // canvas.restore()
            matrixDraw.reset()
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
        scale =
            (w.toFloat() / widthDrawable).coerceAtMost(h.toFloat() / heightDrawable)
        posX = (w - widthDrawable * scale) / 2f
        posY = (h - heightDrawable * scale) / 2f
        matrix.reset()
        matrix.postScale(
            scale, scale
        )
    }

  /*  private fun getPivot(x: Float, y: Float){



        //scale = 2f
        pivotPointX = 0f
        pivotPointY =  0f
        invalidate()
    }*/

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



    private fun getPivots(x: Float, y: Float){
        /*val sWidth  = widthDrawable  * scale
        val sHeight = heightDrawable * scale*/
        val pointX = (x  - posX) / scale
        val pointY = (y  - posY) / scale
     /*   if (pointX > widthDrawable) pointX = widthDrawable
        if (pointX <0) pointX = 0f

        if (pointY > heightDrawable) pointY = heightDrawable
        if (pointY <0) pointY = 0f*/

        pivotPointX = pointX
        pivotPointY = pointY
        log("scale = $scale, x = $pointX, y = $pointY")
    }



    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector?.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

              /*  val pointX = event.x
                val pointY = event.y
                val w = widthDrawable  * scale
                val h = heightDrawable * scale

                val rect = RectF(
                    posX,
                    posY,
                    posX + widthDrawable  * scale,
                    posY + heightDrawable  * scale
                )

                if (rect.contains(pointX, pointY)) {
                    pivotPointX = posX
                    pivotPointY = posY
                }*/


               // log(rect)
                /*pivotPointX = event.x + posX
                pivotPointY = event.y + posY*/
                getPivots(event.x, event.y)

                lastTouchX = event.x
                lastTouchY = event.y
                //activePointerId = event.getPointerId(0)
                //log(activePointerId)
            }

        /*    MotionEvent.ACTION_UP -> {
                pivotPointX = 0f
                pivotPointY = 0f
                    // activePointerId = INVALID_POINTER_ID;
            }

           MotionEvent.ACTION_CANCEL -> {
               pivotPointX = 0f
               pivotPointY = 0f
                   //   activePointerId = INVALID_POINTER_ID;
           }*/

           MotionEvent.ACTION_MOVE -> {
                //val pointerIndex = event.findPointerIndex(activePointerId)
                val x = event.x//.getX(pointerIndex)
                val y = event.y//getY(pointerIndex)
                if (!scaleDetector!!.isInProgress) {
                    val dx = x - lastTouchX
                    val dy = y - lastTouchY
                    posX += dx
                    posY += dy

                 /*   val w = widthDrawable * scale
                    val h = heightDrawable * scale
                    val dw = widthView  - w
                    val dh = heightView - h

                    if (dw < 0) {
                        if (posX > 0) posX = 0f
                        else
                        if (posX < dw )
                           posX = dw
                    } else {
                        posX = dw / 2f
                    }


                    if (dh < 0) {
                        if (posY > 0) posY = 0f
                        else
                            if (posY < dh )
                                posY = dh
                    } else {
                        posY = dh / 2f
                    }*/
                    invalidate();
                }
                lastTouchX = x
                lastTouchY = y
           }
            /*MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = (event.action * MotionEvent.ACTION_POINTER_INDEX_MASK) shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = event.getPointerId(pointerIndex);
                if (pointerId == activePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    lastTouchX = event.getX(newPointerIndex);
                    lastTouchY = event.getY(newPointerIndex);
                    activePointerId = event.getPointerId(newPointerIndex)
                }
            }*/
        }
        return true
    }



    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            //log("${detector.focusX}, ${detector.focusY}")
                    //getPivots(detector.focusX, detector.focusY)
            /*pivotPointX = detector.focusX
            pivotPointY = detector.focusY*/
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            //var detectorScale = detector.scaleFactor
            val curScale = scale
            scale *= detector.scaleFactor
            if (scale > maxScale) {
                scale = maxScale
                    //detectorScale = maxScale / curScale
            } else if (scale < minScale) {
                scale = minScale
                //detectorScale = minScale / curScale
            }

           /* posX = posX / curScale * scale
            posY = posY / curScale * scale*/
            getPivots(detector.focusX, detector.focusY)
            matrix.reset()
            matrix.postScale(
                scale, scale,
                pivotPointX,
                pivotPointY
            )

            //scale = Math.max(0.05f, scale)

            //int scrollX = (int) ((getScrollX() + scaleDetector.getFocusX()) * scaleDetector.getScaleFactor() - scaleDetector.getFocusX());

            //getPivot(detector.focusX, detector.focusY)
            /*pivotPointX = detector.focusX
            pivotPointY = detector.focusY*/
            //getPivots(detector.focusX, detector.focusY)
            /*var pivotPointX =  detector.focusX
            var pivotPointY =  detector.focusY
            log("$pivotPointX, $pivotPointY")*/


            //log("pivotX = $pivotPointX, pivotY = $pivotPointY")


            invalidate()


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