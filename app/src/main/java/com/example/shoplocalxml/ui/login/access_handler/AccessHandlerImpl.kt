package com.example.shoplocalxml.ui.login.access_handler

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.liveData
import com.example.shoplocalxml.log
import com.example.shoplocalxml.repository.database_api.DatabaseApi
import com.example.shoplocalxml.repository.database_api.DatabaseApiImpl
import com.example.shoplocalxml.ui.login.finger_print.FingerPrint
import com.example.shoplocalxml.ui.login.finger_print.FingerPrintImpl
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorageImpl
import java.net.PasswordAuthentication

class AccessHandlerImpl(private val databaseApi: DatabaseApiImpl): AccessHandler {
    override var passwordStorage: PasswordStorage? = PasswordStorageImpl()
    private var fingerPrint: FingerPrint? = null
    override fun setActivityFingerPrint(activity: FragmentActivity) {
        fingerPrint = FingerPrintImpl(activity)
        fingerPrint?.passwordStorage = passwordStorage
    }

    override fun onLogin(
        email: String,
        password: String,
        finger: Boolean,
        action: (token: String?) -> Unit
    ) {
        if (finger) {
            fingerPrint?.onComplete = {storagePassword ->
                storagePassword?.let{
                    performLogin(email, it, true, action)
                }
            }
            fingerPrint?.promptAuthenticate()
        } else
            performLogin(email, password, false, action)
    }

    private fun performLogin(email: String, password: String, finger: Boolean, action: (token: String?) -> Unit){
        databaseApi
        val token: String? = "1"
        if (token != null && !finger)
            passwordStorage?.putPassword(password)
        action(token)
    }

    override fun onRegister(vararg userdata: String, action: (result: Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun onRestore(email: String, password: String, action: (result: Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

}