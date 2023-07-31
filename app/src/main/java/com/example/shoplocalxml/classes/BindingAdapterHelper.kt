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

 /*   @BindingAdapter("android:text")
    @JvmStatic
    fun bindMessageTitle(view: TextView, message: UserMessage?) {
        val title = getStringArrayResource(R.array.typemessage)
        val text = message?.let{
            title[it.type]
        } ?: title[0]
        view.text = text
    }*/


    @BindingAdapter("app:srcCompat")
    @JvmStatic
    fun bindSrcCompat(imageView: ImageView, @DrawableRes drawableId: Int) {
        val drawable = AppCompatResources.getDrawable(AppShopLocal.applicationContext, drawableId)
        imageView.setImageDrawable(drawable)
    }


    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(view: EditTextExt, value: Int) {
        view.setText(value.toString());
    }

    @InverseBindingAdapter(attribute = "android:text")
    @JvmStatic
    fun getText(view: EditTextExt): Int {
        return getInteger(view.text)
        //return Integer.parseInt(view.text.toString());
    }
}


/*
android:text="@{String.valueOf(Integer)}"
@BindingAdapter("android:text")
    public static void setText(TextView view, int value) {
        view.setText(Integer.toString(value));
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static int getText(TextView view) {
        return Integer.parseInt(view.getText().toString());
    }
 */