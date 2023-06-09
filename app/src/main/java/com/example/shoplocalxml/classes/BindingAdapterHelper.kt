package com.example.shoplocalxml.classes

import androidx.databinding.BindingAdapter
import com.example.shoplocalxml.custom_view.RatingView

class BindingAdapterHelper {
    companion object {
        @BindingAdapter("app:count")
        @JvmStatic
        fun setReviewCountStar(view: RatingView, count: Float) {

                view.setCount(count)


        }
    }
}