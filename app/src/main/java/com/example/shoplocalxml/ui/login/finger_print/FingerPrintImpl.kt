package com.example.shoplocalxml.ui.login.finger_print

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.R
import com.example.shoplocalxml.getStringResource
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorageImpl
import javax.crypto.Cipher

class FingerPrintImpl(private val activity: FragmentActivity): FingerPrint() {
    override var passwordStorage: PasswordStorage? = null
    override var onComplete: ((password: String?) -> Unit)? = null

    override fun promptAuthenticate() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getStringResource(R.string.biometric_title))
            .setConfirmationRequired(false)
            .setNegativeButtonText(getStringResource(R.string.button_cancel))
            .build()
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding") // не используется
        val biometricPrompt = createBiometricPrompt()
        biometricPrompt.authenticate(
            promptInfo,
            BiometricPrompt.CryptoObject(cipher)
        )
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onComplete?.invoke(null)
            }
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onComplete?.invoke(passwordStorage?.getPassword())
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }
}