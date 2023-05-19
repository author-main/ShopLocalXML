package com.example.shoplocalxml.ui.login.access_handler

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.liveData
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.isConnectedNet
import com.example.shoplocalxml.log
import com.example.shoplocalxml.repository.database_api.DatabaseApi
import com.example.shoplocalxml.repository.database_api.DatabaseApiImpl
import com.example.shoplocalxml.ui.login.finger_print.FingerPrint
import com.example.shoplocalxml.ui.login.finger_print.FingerPrintImpl
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorageImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
        performAction: () -> Unit,
        action: (token: String?) -> Unit
    ) {
        if (!isConnectedNet()) {
            action(null)
            return
        }
        if (finger) {
            fingerPrint?.onComplete = {storagePassword ->
                storagePassword?.let{
                    performLogin(email, it, true, performAction, action)
                }
            }
            fingerPrint?.promptAuthenticate()
        } else
            performLogin(email, password, false, performAction, action)
    }

    private fun performLogin(email: String, password: String, finger: Boolean, performAction: () -> Unit, action: (token: String?) -> Unit){
        performAction()
        var token: String? = null
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = databaseApi.loginUser(email, password)
                response.body()?.let {dbUser ->
                    token = dbUser.token
                    if (!token.isNullOrBlank()) {
                        /*val savedUser = User.getUserData()
                        if (savedUser == null) {*/
                            dbUser.id = null
                            dbUser.token = null
                            dbUser.password = null
                            dbUser.saveUserData()
                        //}
                        if (!finger)
                            passwordStorage?.putPassword(password)
                    }
                }
            } catch(_:Exception){
                //log(e.message)
            } finally {
                action(token)
            }
        }
    }

    /*override fun onRegister(vararg userdata: String, action: (result: Boolean) -> Unit) {
        TODO("Not yet implemented")
    }*/

    override fun onRegister(user: User, action: (result: Boolean) -> Unit) {
        var result = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = databaseApi.registerUser(user)
                result = response.body()?.id?.let{
                    it > 0
                } ?: false
            } catch (_: Exception) {
            } finally {
                action(result)
            }
        }
    }

    override fun onRestore(user: User, action: (result: Boolean) -> Unit) {
        var result = false
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = databaseApi.restoreUser(user)
                result = response.body()?.id?.let{
                    it > 0
                } ?: false
            } catch (_: Exception) {
            } finally {
                action(result)
            }
        }
    }

}