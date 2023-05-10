package com.example.shoplocalxml.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.shoplocalxml.log
import com.example.shoplocalxml.toDp
import com.example.shoplocalxml.toPx

class EditTextExt(context: Context, attributes: AttributeSet) : AppCompatEditText(context, attributes) {
    private val paint = Paint()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
      /*  val width  = MeasureSpec.getSize(widthMeasureSpec)  + 2.toPx
        val height = MeasureSpec.getSize(heightMeasureSpec) + 2.toPx
        setMeasuredDimension(width, height)*/
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
      /*  paint.color = Color.BLUE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.toPx.toFloat()

        canvas?.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), 8.toPx.toFloat(), 8.toPx.toFloat(), paint)
        canvas?.drawText(
            prefix, defaultLeftPadding,
            getLineBounds(0, null).toFloat(), paint
        )*/
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
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