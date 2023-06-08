package com.example.shoplocalxml.classes

import androidx.databinding.BindingAdapter
import com.example.shoplocalxml.ui.product_item.RatingView

class BindingAdapterHelper {
    companion object {
        @BindingAdapter("app:count")
        fun setReviewCountStar(view: RatingView?, count: Float?) {
            count?.let {
                view?.setCount(it)
            }

        }
    }
}