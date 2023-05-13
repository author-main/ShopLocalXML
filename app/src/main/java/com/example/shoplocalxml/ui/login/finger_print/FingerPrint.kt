package com.example.shoplocalxml.ui.login.finger_print

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage

interface FingerPrint {
    val passwordStorage: PasswordStorage
    fun setActivity(activity: FragmentActivity)
    fun onComplete(): Boolean
}