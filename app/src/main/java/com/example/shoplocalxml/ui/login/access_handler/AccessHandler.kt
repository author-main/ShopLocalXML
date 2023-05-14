package com.example.shoplocalxml.ui.login.access_handler

import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage

interface AccessHandler {
    var passwordStorage: PasswordStorage?
    fun setActivityFingerPrint(activity: FragmentActivity)
    fun onLogin(action: ((token: String?) -> Unit)?, email: String, password: String, finger: Boolean = false)
    fun onRegister(action: ((result: Boolean) -> Unit)?, vararg userdata: String)
    fun onRestore(action: ((result: Boolean) -> Unit)?, email: String, password: String)
    fun onFingerPrint(action: ((token: String?) -> Unit)?, email: String)
}