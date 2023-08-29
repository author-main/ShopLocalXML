package com.example.shoplocalxml.classes

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.R
import com.example.shoplocalxml.custom_view.EditTextExt
import com.example.shoplocalxml.custom_view.RatingView
import com.example.shoplocalxml.getInteger


object BindingAdapterHelper {
    @BindingAdapter("app:count")
    @JvmStatic
    fun setReviewCountStar(view: RatingView, count: Float) {
        view.setCount(count)
    }

    @BindingAdapter("app:srcCompat")
    @JvmStatic
    fun bindSrcCompat(imageView: ImageView, @DrawableRes drawableId: Int) {
        val drawable = AppCompatResources.getDrawable(AppShopLocal.applicationContext, drawableId)
        imageView.setImageDrawable(drawable)
    }

    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(view: EditTextExt, value: Int) {
        view.setText(value.toString())
    }

    @InverseBindingAdapter(attribute = "android:text")
    @JvmStatic
    fun getText(view: EditTextExt): Int {
        return getInteger(view.text)
    }
}