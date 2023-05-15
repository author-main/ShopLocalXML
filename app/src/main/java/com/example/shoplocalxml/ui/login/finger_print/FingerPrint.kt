package com.example.shoplocalxml.ui.login.finger_print

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage

abstract class FingerPrint {
    abstract var passwordStorage: PasswordStorage?
    abstract fun authenticate(email: String): Boolean
    companion object {
        fun canAuthenticate() =
            BiometricManager.from(applicationContext)
                .canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
    }
}