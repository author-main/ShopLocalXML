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
import android.view.VelocityTracker
import com.example.shoplocalxml.log
import kotlin.math.abs


class ZoomImageView: androidx.appcompat.widget.AppCompatImageView, GestureDetector.OnDoubleTapListener, GestureDetector.OnGestureListener {
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
        /*posX = (w - widthDrawable * saveScale) / 2f
        posY = (h - heightDrawable * saveScale) / 2f*/

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

    private fun limitTranslate() {
       /* val matrixValue = FloatArray(9)
        matrix.getValues(matrixValue);
        val mX = matrixValue[Matrix.MTRANS_X] // извлекаем из matrix перемещение x, y
        val mY = matrixValue[Matrix.MTRANS_Y]*/
        val transPos = getTranslatePos()
        /*val x = placeBound(mX, widthView,  widthDrawable  * saveScale)
        val y = placeBound(mY, heightView, heightDrawable * saveScale)*/
        val x = placeBound(transPos.x, widthView,  widthDrawable  * saveScale)
        val y = placeBound(transPos.y, heightView, heightDrawable * saveScale)
        if (x != 0f || y!=0f)
            matrix.postTranslate(x, y);
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
                    performClick();
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


    override fun onDown(e: MotionEvent): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent) {

    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent) {

    }

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }


    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
        return false
    }

    override fun onDoubleTap(e: MotionEvent): Boolean {
        val origScale = saveScale
        /*val aMatrix = FloatArray(9)
        matrix.getValues(aMatrix)
        val curX = aMatrix[Matrix.MTRANS_X]
        val curY = aMatrix[Matrix.MTRANS_Y]*/
        val transPos = getTranslatePos()
        saveScale = if (saveScale == maxScale)
            minScale
        else
            maxScale
        matrix.reset()
        val centerX = (widthView - widthDrawable   * saveScale) / 2f
        val centerY = (heightView - heightDrawable * saveScale) / 2f
        //animDoubleZoom = DoubleTapAnimator(PointF(curX, curY), PointF(centerX, centerY), origScale, saveScale)
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
        /*init {
            stepScale   = (endScale - startScale) / fraction
            stepX       = (endTrans.x - startTrans.x) / fraction
            stepY       = (endTrans.y - startTrans.y) / fraction
            matrix.reset()
        }*/
        override fun run() {
            scale   += stepScale
            var rangeFrom = 0f
            var rangeTo   = 0f
            if (startScale < endScale) {
                rangeFrom = startScale
                rangeTo   = endScale
            } else {
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


/*
public class TouchImageView extends ImageView implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    Matrix matrix;

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1f;
    float maxScale = 3f;
    float[] m;

    int viewWidth, viewHeight;
    static final int CLICK = 3;
    float saveScale = 1f;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;

    ScaleGestureDetector mScaleDetector;

    Context context;

    public TouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }

    GestureDetector mGestureDetector;

    private void sharedConstructing(Context context) {
        super.setClickable(true);
        this.context = context;
        mGestureDetector = new GestureDetector(context, this);
        mGestureDetector.setOnDoubleTapListener(this);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix = new Matrix();
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                mGestureDetector.onTouchEvent(event);

                PointF curr = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        last.set(curr);
                        start.set(last);
                        mode = DRAG;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float fixTransX = getFixDragTrans(deltaX, viewWidth,
                                    origWidth * saveScale);
                            float fixTransY = getFixDragTrans(deltaY, viewHeight,
                                    origHeight * saveScale);
                            matrix.postTranslate(fixTransX, fixTransY);
                            fixTrans();
                            last.set(curr.x, curr.y);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        if (xDiff < CLICK && yDiff < CLICK)
                            performClick();
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                }

                setImageMatrix(matrix);
                invalidate();
                return true; // indicate event was handled
            }

        });
    }

    public void setMaxZoom(float x) {
        maxScale = x;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        // Double tap is detected
        Log.i("MAIN_TAG", "Double tap detected");
        float origScale = saveScale;
        float mScaleFactor;

        if (saveScale == maxScale) {
            saveScale = minScale;
            mScaleFactor = minScale / origScale;
        } else {
            saveScale = maxScale;
            mScaleFactor = maxScale / origScale;
        }

        matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2,
                viewHeight / 2);

        fixTrans();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth
                    || origHeight * saveScale <= viewHeight)
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2,
                        viewHeight / 2);
            else
                matrix.postScale(mScaleFactor, mScaleFactor,
                        detector.getFocusX(), detector.getFocusY());

            fixTrans();
            return true;
        }
    }

    void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight
                * saveScale);

        if (fixTransX != 0 || fixTransY != 0)
            matrix.postTranslate(fixTransX, fixTransY);
    }

    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }


    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            // Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0
                    || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();

            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight
                    - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth
                    - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);
        }
        fixTrans();
    }
}

 */