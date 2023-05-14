package com.example.shoplocalxml.ui.login.finger_print

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage

class FingerPrintImpl(activity: FragmentActivity): FingerPrint(activity) {
    override fun onComplete(): Boolean {
        return true
    }
}