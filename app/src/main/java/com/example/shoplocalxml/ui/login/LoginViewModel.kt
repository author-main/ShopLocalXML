package com.example.shoplocalxml.ui.login

import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.PasswordSymbol
import com.example.shoplocalxml.log
import com.example.shoplocalxml.repository.Repository
import com.example.shoplocalxml.ui.dialog.DialogProgress
import com.example.shoplocalxml.ui.login.access_handler.AccessHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    var onChangePassword: ((count: Int, type: PasswordSymbol) -> Unit)? = null
    var onValidEmail: (() -> String?)? = null
    var openShop: ((open: Boolean) -> Unit)? = null
    var onPerformLogin: () -> Unit = {}
    private val KEY_FINGER      = 10
    private val KEY_BACKSPACE   = 11
    private var userPassword    = ""

    fun setActivityFingerPrint(activity: FragmentActivity) {
        repository.setActivityFingerPrint(activity)
    }

    fun existPassword() = repository.existPassword()

    fun onClick(index: Int){
        if (index in 0..11) {
            var typeKey = PasswordSymbol.NUMBER
            var changed = false
            when (index) {
                KEY_FINGER -> {
                    changed = true
                    typeKey = PasswordSymbol.FINGER_PRINT
                    //userPassword = "*****"
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
                val fingerPrint = typeKey == PasswordSymbol.FINGER_PRINT
                if (!fingerPrint) onChangePassword?.invoke(userPassword.length, typeKey)
                if (userPassword.length == 5 || fingerPrint) {
                    onLogin(fingerPrint)
                    /*viewModelScope.launch {
                        delay(700)
                        onLogin(typeKey == PasswordSymbol.FINGER_PRINT)
                    }*/

                }
            }
        }
    }

    fun getUserEmail(): String? = repository.shopUser?.email

    private fun onLogin(finger: Boolean){
        fun performOpenShop(value: Boolean){
            viewModelScope.launch {
                delay(300)
                openShop?.invoke(value)
                userPassword = ""
            }
        }
        val email = onValidEmail?.invoke()
        if (!email.isNullOrBlank()) {
            repository.onLogin(email, userPassword, finger, onPerformLogin) { result ->
                performOpenShop(result)
            }
        } else {
            if (!finger)
                performOpenShop(false)
        }

    }
}