package com.example.shoplocalxml.ui.login.access_handler

import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage

interface AccessHandler {
    var passwordStorage: PasswordStorage?
    fun setActivityFingerPrint(activity: FragmentActivity)
    fun onLogin(email: String, password: String, finger: Boolean = false, performAction: () -> Unit, action: (token: String?) -> Unit)
    //fun onRegister(vararg userdata: String, action: (result: Boolean) -> Unit)
    fun onRegister(user: User, action: (result: Boolean) -> Unit)
    fun onRestore(user: User, action: (result: Boolean) -> Unit)
}