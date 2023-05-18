package com.example.shoplocalxml.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import com.example.shoplocalxml.R
import com.example.shoplocalxml.alpha
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toPx
import java.util.stream.IntStream


@SuppressLint("RestrictedApi")
class EditTextExt(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {
    private val INPUTTYPE_PASSWORD = 18
    var onValidValue: ((text: String) -> Boolean)? = null
    private var drawableEnd: Drawable? = null
    var checked = false
    var drawableAction: Drawable? = null
    private val paint = Paint()
    private var drawableRight: Drawable? = null
    private var roundBackgroundColor = Color.parseColor("#FF393E46")
        set(value){
            field = value
            setRoundBackground()
        }
    private var borderColor     = Color.TRANSPARENT
        set(value){
            field = value
            setRoundBackground()
        }
    private var textColor       = Color.parseColor("#FFBEBEBE")
    private var hintColor       = 0
    /*private var drawableRightColor = Color.parseColor("#FFBEBEBE")
        set (value) {
            field = value
            drawableRight?.setTint(value)
        }*/

    private var drawableOnClick: (() -> Unit)? = null



    init {
        drawableEnd = compoundDrawablesRelative[2]
        if (drawableEnd != null)
            setCompoundDrawablesRelative(null, null, drawableEnd, null)
        textSize = 16f
        setTextColor(textColor)
        val sHintColor = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textColorHint")
        hintColor = if (sHintColor != null)
            Color.parseColor(sHintColor)
        else
            textColor.alpha(0.5f)
        setHintTextColor(hintColor)
        setSingleLine()
        maxLines = 1
/*        val attributes =
            intArrayOf(android.R.attr.textColorHint)
        /*val ta = obtainStyledAttributes(context, R.style.EditTextExt, attributes)
        log(ta.getString(0))*/
        val a = context.obtainStyledAttributes(attrs, intArrayOf(0), 0, com.example.shoplocalxml.R.style.EditTextExt)
        a.recycle()*/

        context.obtainStyledAttributes(attrs, R.styleable.EditTextExt).let {
            val reference = it.getResourceId(R.styleable.EditTextExt_drawableAction, -1)
            setDrawableAction(reference)
            it.recycle()
        }

        if (inputType == INPUTTYPE_PASSWORD) {
            setTransformationMethod()
            //transformationMethod = PasswordTransformationMethod.getInstance()
        }
      /*  if (inputType == 18) {
            transformationMethod = object : PasswordTransformationMethod() {
                override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
                    return super.getTransformation(source, view)
                }

                override fun onFocusChanged(
                    view: View?,
                    sourceText: CharSequence?,
                    focused: Boolean,
                    direction: Int,
                    previouslyFocusedRect: Rect?
                ) {
                    super.onFocusChanged(
                        view,
                        sourceText,
                        focused,
                        direction,
                        previouslyFocusedRect
                    )
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    super.beforeTextChanged(s, start, count, after)
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    super.onTextChanged(s, start, before, count)
                }

                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)
                }
            }
        }*/



    }


    fun changePasswordChar(char: Char) {

    }

    @JvmName("setDrawableAction_")
    private fun setDrawableAction(value: Int) {
        if (value != -1) {
            drawableAction = AppCompatResources.getDrawable(context, value)
            val color = context.getColor(R.color.drawable_tint_color).alpha(0.5f)
            drawableAction?.setTint(color)
        }
    }

    fun setDrawableOnClick(action: () -> Unit){
        drawableOnClick = action
    }


    override fun performClick(): Boolean {
        return super.performClick()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (it.action == MotionEvent.ACTION_DOWN) {
                performClick()
                drawableRight?.let{icon ->
                    val bounds = icon.bounds;
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    val placeBounds = Rect().apply {
                        this.left    = measuredWidth - compoundPaddingRight
                        this.top     = compoundPaddingTop
                        this.right   = measuredWidth - 8.toPx
                        this.bottom  = measuredHeight - compoundPaddingBottom
                    }
                   /*if (drawableOnClick != null && placeBounds.contains(x, y)) {
                        drawableOnClick?.let{onClick ->*/
                    if (drawableEnd != null && placeBounds.contains(x, y)) {
                            if (drawableAction != null) {
                                checked = !checked
                                val drawable = if (checked) drawableAction else drawableEnd
                                changeDrawableEnd(drawable)
                            }

                                //setCompoundDrawablesRelative(null, null, drawableAction, null)
                            //onClick()
                            drawableOnClick?.invoke()

                        event.action = MotionEvent.ACTION_CANCEL
                    }
                }
            }
        }

        return super.onTouchEvent(event)
    }

    private fun changeDrawableEnd(drawable: Drawable?){
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            null,
            drawable,
            null
        )
        if (drawableAction != null) {
            if (inputType == INPUTTYPE_PASSWORD) {
                if (checked)
                    setTransformationMethod(reset = true)
                else
                    setTransformationMethod()
            }
        }
    }


    override fun onEditorAction(actionCode: Int) {
        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            clearFocus()
        }
        super.onEditorAction(actionCode)
    }

    private fun setTransformationMethod(reset: Boolean = false){
        transformationMethod = if (reset)
            HideReturnsTransformationMethod.getInstance()
        else
            PasswordTransformationMethod.getInstance()
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        if (!focused) {
            validateValue()
        }
        else
            borderColor = Color.TRANSPARENT
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
    }

    private fun setRoundBackground(){
        val width  = measuredWidth
        val height = measuredHeight
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        paint.color = roundBackgroundColor
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        val round = 12.toPx.toFloat()
        val canvas = Canvas(bitmap)
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), round, round, paint)
        if (borderColor != Color.TRANSPARENT) {
            paint.style = Paint.Style.STROKE
            val strokeWidth = 1.toPx.toFloat()+0.5f
            paint.strokeWidth = strokeWidth
            paint.color = borderColor
            canvas.drawRoundRect(
                strokeWidth,
                strokeWidth,
                width.toFloat() - strokeWidth,
                height.toFloat() - strokeWidth,
                round,
                round,
                paint
            )
        }
        this.background = BitmapDrawable(resources, bitmap)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val horzPadding = 8.toPx
        setPadding(horzPadding,
                   paddingTop,
                   horzPadding,
                   paddingBottom
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setRoundBackground()
    }

   /* private fun getColorState(): ColorStateList{
        val states = arrayOf(
            intArrayOf(android.R.attr.defaultValue) // enabled
            /*intArrayOf(android.R.attr.state_enabled), // enabled
            intArrayOf(-android.R.attr.state_enabled), // disabled
            intArrayOf(-android.R.attr.state_checked), // unchecked
            intArrayOf(android.R.attr.state_pressed)  // pressed*/
        )

        val colors = intArrayOf(
            Color.RED/*,
            Color.RED,
            Color.GREEN,
            Color.BLUE*/
        )

        return ColorStateList(states, colors)
    }*/

    private fun getDrawableRight(drawable: Drawable?){
        drawable?.let{icon ->
            drawableRight = icon
            val color = context.getColor(R.color.drawable_tint_color).alpha(0.5f)
            icon.setTint(color)
        }
    }

    override fun setCompoundDrawablesRelative(
        start: Drawable?,
        top: Drawable?,
        end: Drawable?,
        bottom: Drawable?
    ) {
        getDrawableRight(end)
        super.setCompoundDrawablesRelative(start, top, end, bottom)
    }

    fun validateValue(): Boolean{
        val correct =
            onValidValue?.let{
                it(text.toString())
            } ?: true
        //log("inputType = $inputType")
       /* when (inputType) {
          33 -> {
              isCorrect = text?.let {email ->
                  !(email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
              } ?: false
              val color = if (isCorrect) Color.TRANSPARENT else context.getColor(R.color.EditTextBorderErrorDark)
              borderColor = color
          }
        }*/
        borderColor = if (correct) Color.TRANSPARENT else context.getColor(R.color.EditTextBorderErrorDark)
        return correct
    }

    override fun setCompoundDrawables(
        left: Drawable?, top: Drawable?,
        right: Drawable?, bottom: Drawable?
    ) {
        getDrawableRight(right)
        super.setCompoundDrawables(left, top, right, bottom)
    }
}

/*
<?xml version="1.0" encoding="utf-8"?>
 <!-- res/drawable/edittext_rounded_corners.xml -->
 <selector xmlns:android="http://schemas.android.com/apk/res/android">

   <item android:state_pressed="true" android:state_focused="true">
     <shape>
       <solid android:color="#FF8000"/>
       <stroke
         android:width="2.3dp"
         android:color="#FF8000" />
       <corners
         android:radius="15dp" />
     </shape>
   </item>

   <item android:state_pressed="true" android:state_focused="false">
     <shape>
       <solid android:color="#FF8000"/>
       <stroke
         android:width="2.3dp"
         android:color="#FF8000" />
       <corners
         android:radius="15dp" />
     </shape>
   </item>

   <item android:state_pressed="false" android:state_focused="true">
     <shape>
       <solid android:color="@color/black_overlay"/>
       <stroke
         android:width="2.3dp"
         android:color="#FF8000" />
       <corners
         android:radius="15dp" />
     </shape>
   </item>

   <item android:state_pressed="false" android:state_focused="false">
     <shape>
       <gradient
         android:startColor="@color/black_overlay"
         android:centerColor="@color/black_overlay"
         android:endColor="@color/black_overlay"
         android:angle="270"
         />
       <stroke
         android:width="0.7dp"
         android:color="#BDBDBD" />
       <corners
         android:radius="15dp" />
     </shape>
   </item>

   <item android:state_enabled="true">
     <shape>
       <padding
         android:left="4dp"
         android:top="4dp"
         android:right="4dp"
         android:bottom="4dp"
         />
     </shape>
   </item>

 </selector>
 */

/*class PasswordTransformationMethodExt(private val char: Char? = null): PasswordTransformationMethod(){
    private class PasswordCharSequence(source: CharSequence, private val char: Char?) : CharSequence {
        private var mSource: CharSequence = source
        override val length: Int
            get() = mSource.length

        override fun chars(): IntStream {
            return super.chars()
        }

        override fun codePoints(): IntStream {
            return super.codePoints()
        }

        override fun get(index: Int): Char {
            return char ?: '•'//getStringResource(R.string.fCharPassword)[0]
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return mSource.subSequence(startIndex, endIndex)
        }
    }


    override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
        return PasswordCharSequence(source!!, char)
    }
}*/