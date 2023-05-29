package com.example.shoplocalxml.custom_view

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.log

class CheckableImageView : AppCompatImageView, Checkable, View.OnClickListener {
    private var stateDrawable: StateListDrawable? = null
    private var mChecked = false

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val drawable = attrs?.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", -1)
        drawable?.let{
            stateDrawable = AppCompatResources.getDrawable(applicationContext, it) as StateListDrawable
        }
        setOnClickListener(this)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked) mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        return drawableState
    }

    override fun toggle() {
        isChecked = !mChecked
            stateDrawable?.let{stateListDrawable->
                stateListDrawable.state = if (isChecked)
                    intArrayOf(android.R.attr.state_checked)
                else
                    intArrayOf(-android.R.attr.state_checked)
                setImageDrawable(stateListDrawable.current)
            }
    }
    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun setChecked(checked: Boolean) {
        if (mChecked == checked) return
        mChecked = checked
        refreshDrawableState()
    }

    override fun onClick(v: View?) {
        toggle()
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}