package com.example.shoplocalxml.custom_view

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R
import com.example.shoplocalxml.toPx
import com.google.android.material.snackbar.Snackbar

class SnackbarExt(view: View, text: String, onClick: ()->Unit = {}) {
    var type = SnackbarType.ERROR
        set(value) {
            setTypeSnackbar(value)
            field = value
        }
    private val snackbar = Snackbar.make(
        view, text,
        Snackbar.LENGTH_LONG)
    private val onClickListener: View.OnClickListener = View.OnClickListener { onClick() }

    init{
        instance = snackbar
    }

    private fun setTypeSnackbar(value: SnackbarType) {
        val drawable = AppCompatResources.getDrawable(applicationContext, R.drawable.roundrect)
        drawable?.let {
            val snackbarView = snackbar.view
            it.setTint(value.color)
            snackbarView.background = it
        }
    }
    fun show(){
        snackbar.setActionTextColor(Color.BLUE)
        val snackbarView = snackbar.view
        val actionTextColor = when(type) {
            SnackbarType.INFO       -> Color.BLUE
            SnackbarType.WARNING    -> Color.BLUE
            SnackbarType.ERROR      -> Color.BLUE
        }
        snackbar.setActionTextColor(actionTextColor)
        snackbarView.setPadding(8.toPx, 0, 8.toPx, 0 )
        val params: CoordinatorLayout.LayoutParams = snackbarView.layoutParams as CoordinatorLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.BOTTOM + Gravity.CENTER_HORIZONTAL
        params.bottomMargin = 16.toPx
        snackbarView.layoutParams = params
        setTypeSnackbar(type)
        val idTextView = com.google.android.material.R.id.snackbar_text
        val textView = snackbarView.findViewById<TextView>(idTextView)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        val font = applicationContext.resources.getFont(R.font.robotocondensed_regular)
        textView.typeface = font
        textView.textSize = 14f
        textView.letterSpacing = 0f
        textView.setTextColor(applicationContext.getColor(R.color.EditTextFont))
        val snackbarActionTextView =
            snackbar.view.findViewById<View>(com.google.android.material.R.id.snackbar_action) as TextView
        snackbarActionTextView.setTextColor(applicationContext.getColor(R.color.colorBrend))
        val fontActionView = applicationContext.resources.getFont(R.font.robotocondensed_regular)
        snackbarActionTextView.typeface = fontActionView
        snackbarActionTextView.letterSpacing =  0.0f
        snackbarActionTextView.transformationMethod = null
        snackbar.show()
    }
    fun setAction(text: String) {
        snackbar.setAction(text, onClickListener)
    }

    companion object {
        lateinit var instance: Snackbar
        enum class SnackbarType(val color: Int) {
            INFO(applicationContext.getColor(R.color.EditTextBackgroundDark)),
            WARNING(applicationContext.getColor(R.color.colorAccent)),
            ERROR(applicationContext.getColor(R.color.EditTextBorderErrorDark))
        }
        fun hideSnackbar(){
            if (this::instance.isInitialized)
                instance.dismiss()
        }
    }
}