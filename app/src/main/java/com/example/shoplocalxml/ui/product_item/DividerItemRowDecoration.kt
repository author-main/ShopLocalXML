package com.example.shoplocalxml.ui.product_item

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.paddingProductCard
import com.example.shoplocalxml.toPx


class DividerItemRowDecoration : ItemDecoration() {
    //private val colorDivider = applicationContext.getColor(R.color.EditTextFont).alpha(0.3f)
    private val px = 1.toPx
    private val colorDivider = applicationContext.getColor(R.color.EditTextBackgroundDark)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply{
        color = colorDivider
    }
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
            .let { if (it == RecyclerView.NO_POSITION) return else it }
        parent.adapter?.let{
            if (position < it.itemCount-1)
                outRect.bottom = paddingProductCard
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val start = parent.paddingLeft.toFloat()
        val end   = parent.width  - parent.paddingRight.toFloat()
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            val child = parent.getChildAt(i)
            //val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + paddingProductCard/2f + px
            c.drawLine(start, top, end, top+1f, paint)
        }
    }
}