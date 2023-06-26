package com.example.shoplocalxml.classes

import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.example.shoplocalxml.custom_view.RatingView

object BindingConversionHelper {
        @BindingConversion
        @JvmStatic
        fun convertIntToString(value: Int): String {
            return value.toString()
        }
}