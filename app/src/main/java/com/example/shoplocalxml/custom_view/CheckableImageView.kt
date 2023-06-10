package com.example.shoplocalxml.custom_view

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import androidx.annotation.ColorRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.product_item.product_card.ProductCard

@BindingMethods(BindingMethod(type = CheckableImageView::class, attribute = "app:onChecked", method = "setOnCheckedListener")/*,
    BindingMethod(type = CheckableImageView::class, attribute = "app:onChecked", method = "setOnChecked")*/
    )
class CheckableImageView : AppCompatImageView, Checkable, View.OnClickListener {

   /* private var onChecked: ((value: Boolean)->Unit)? = null
    fun setOnChecked(value: ((value: Boolean)->Unit)?) {
        onChecked = value
    }*/

    interface OnCheckedListener {
        fun onChecked(value: Boolean)
    }
    private var onCheckedListener: OnCheckedListener? = null
    fun setOnCheckedListener(value: OnCheckedListener){
        onCheckedListener = value
    }
    private var stateDrawable: StateListDrawable? = null
    private var mChecked = false
    //private var onChecked: ((checked: Boolean) -> Unit)? = null

    private var checkedDrawable:   Drawable? = null
    private var uncheckedDrawable: Drawable? = null

    /*fun setOnCheckedListener(value: (checked: Boolean) -> Unit){
        onChecked = value
    }*/

    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val drawable = attrs?.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", -1)
        drawable?.let{
            stateDrawable = AppCompatResources.getDrawable(applicationContext, it) as StateListDrawable
            stateDrawable?.let{stateListDrawable->
                val savedState = stateListDrawable.state
                stateListDrawable.state = intArrayOf(android.R.attr.state_checked)
                checkedDrawable = stateListDrawable.current
                stateListDrawable.state = intArrayOf(-android.R.attr.state_checked)
                uncheckedDrawable = stateListDrawable.current
                stateListDrawable.state = savedState

            }

        }

        context.obtainStyledAttributes(attrs, R.styleable.CheckableImageView).run {
            val tintChecked = getColor(R.styleable.CheckableImageView_tintCheckedDrawable, -1)
            val tintUnchecked = getColor(R.styleable.CheckableImageView_tintUncheckedDrawable, -1)
            if (tintChecked != -1)
                checkedDrawable?.setTint(tintChecked)
            if (tintUnchecked != -1)
                uncheckedDrawable?.setTint(tintUnchecked)
            recycle()
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
    }
    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun setChecked(checked: Boolean) {
        if (mChecked == checked) return
        mChecked = checked
        if (mChecked)
            setImageDrawable(checkedDrawable)
        else
            setImageDrawable(uncheckedDrawable)
        refreshDrawableState()
        //onChecked?.invoke(mChecked)
        //onChecked?.invoke(mChecked)
        //onCheckedListener?.onChecked(mChecked)
    }

    override fun onClick(v: View?) {
        toggle()
        onCheckedListener?.onChecked(mChecked)
    }

    companion object {
        private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
    }
}