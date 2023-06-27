package com.example.shoplocalxml.ui.product_item

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.shoplocalxml.paddingProductCard


class DividerItemCardDecoration : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
            .let { if (it == RecyclerView.NO_POSITION) return else it }
        val evenPosition = (position + 1) % 2 == 0
        val halfPadding = paddingProductCard / 2
        if (evenPosition)
            outRect.left = halfPadding
        else
            outRect.right = halfPadding
        parent.adapter?.let{
            val limit = if (evenPosition) it.itemCount-3 else it.itemCount - 2
            if (position < limit)
                outRect.bottom = paddingProductCard
        }
    }
}