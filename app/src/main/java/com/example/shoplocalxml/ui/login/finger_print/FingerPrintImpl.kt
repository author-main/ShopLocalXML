package com.example.shoplocalxml.ui.login.finger_print

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorageImpl

class FingerPrintImpl(val activity: FragmentActivity): FingerPrint() {
    override var passwordStorage: PasswordStorage? = null
    override fun authenticate(email: String): Boolean {
        return true
    }
}