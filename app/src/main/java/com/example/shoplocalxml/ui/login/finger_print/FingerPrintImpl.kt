package com.example.shoplocalxml.ui.login.finger_print

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage

class FingerPrintImpl: FingerPrint {

    override val passwordStorage: PasswordStorage
        get() = TODO("Not yet implemented")

    override fun setActivity(activity: FragmentActivity){

    }

    override fun onComplete(): Boolean {
        return true
    }
}