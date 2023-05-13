package com.example.shoplocalxml.ui.login.access_handler

interface AccessHandler {
    fun onLogin(action: ((token: String?) -> Unit)?, email: String, password: String, finger: Boolean = false)
    fun onRegister(action: ((result: Boolean) -> Unit)?, vararg userdata: String)
    fun onRestore(action: ((result: Boolean) -> Unit)?, email: String, password: String)
    fun onFingerPrint(action: ((token: String?) -> Unit)?, email: String)
    fun onRemoveUserPassword()
}