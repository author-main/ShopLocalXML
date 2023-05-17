package com.example.shoplocalxml.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.R


class DialogProgress(context: Context): Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //setContentView(R.layout.progress_dialog)
        setCancelable(false)
        window?.let {
            val layoutParams = WindowManager.LayoutParams()
            setContentView(R.layout.progress_dialog)
            layoutParams.copyFrom(it.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            it.attributes = layoutParams
            it.setBackgroundDrawableResource(android.R.color.transparent)
            it.setDimAmount(0F)
        }
    }
    companion object {
        private var instance: DialogProgress? = null
        private fun getInstance(context: Context): DialogProgress?{
            if (instance == null)
                instance = DialogProgress(context)
            return instance
        }
        fun show(context: Context) {
            getInstance(context)?.show()
        }
        fun dismiss(){
            instance?.let{
                it.dismiss()
                instance = null
            }
        }
    }
}



