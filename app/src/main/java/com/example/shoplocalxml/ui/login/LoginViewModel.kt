package com.example.shoplocalxml.ui.login

import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplocalxml.PasswordSymbol
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.login.access_handler.AccessHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private var accessHandler: AccessHandler? = null
    var onChangePassword: ((count: Int, type: PasswordSymbol) -> Unit)? = null
    var onValidEmail: (() -> String?)? = null
    private val KEY_FINGER      = 10
    private val KEY_BACKSPACE   = 11
    private var userPassword    = ""


    fun setAccessHandler(value: AccessHandler) {
        accessHandler = value
    }

    fun onClick(index: Int){
        if (index in 0..11) {
            var typeKey = PasswordSymbol.NUMBER
            var changed = false
            when (index) {
                KEY_FINGER -> {
                    changed = true
                    typeKey = PasswordSymbol.FINGER_PRINT
                    userPassword = "*****"
                }
                KEY_BACKSPACE -> {
                    typeKey = PasswordSymbol.BACKSPACE
                    if (userPassword.isNotBlank()) {
                        changed = true
                        userPassword = userPassword.substring(0, userPassword.length - 1)
                    }
                }
                else -> {
                    if (userPassword.length < 5) {
                        changed = true
                        userPassword = "$userPassword$index"
                    }
                }
            }
            if (changed) {
                onChangePassword?.invoke(userPassword.length, typeKey)
                if (userPassword.length == 5) {
                    viewModelScope.launch {
                        delay(700)
                        onLogin()
                    }

                }
            }
        }
    }

    private fun onLogin(){
        val validEmail = onValidEmail?.invoke()
         if (!validEmail.isNullOrBlank()) {
             log (validEmail)
         }
        userPassword = ""
    }
}