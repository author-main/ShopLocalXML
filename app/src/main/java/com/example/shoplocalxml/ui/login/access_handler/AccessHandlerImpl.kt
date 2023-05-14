package com.example.shoplocalxml.ui.login.access_handler

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.ui.login.finger_print.FingerPrint
import com.example.shoplocalxml.ui.login.finger_print.FingerPrintImpl
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorageImpl
import java.net.PasswordAuthentication

class AccessHandlerImpl: AccessHandler {
    override var passwordStorage: PasswordStorage? = PasswordStorageImpl()
    private var fingerPrint: FingerPrint? = null

    override fun setActivityFingerPrint(activity: FragmentActivity) {
        fingerPrint = FingerPrintImpl(activity)
    }

    override fun onLogin(
        action: ((token: String?) -> Unit)?,
        email: String,
        password: String,
        finger: Boolean
    ) {
        passwordStorage?.putPassword(password)
    }

    override fun onRegister(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
        TODO("Not yet implemented")
    }

    override fun onRestore(action: ((result: Boolean) -> Unit)?, email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun onFingerPrint(action: ((token: String?) -> Unit)?, email: String) {
        TODO("Not yet implemented")
    }
}