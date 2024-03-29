package com.example.shoplocalxml.ui.login

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.AppShopLocal.Companion.repository
import com.example.shoplocalxml.RepositoryViewModel
import com.example.shoplocalxml.TypeRequest
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.dagger.ActivityMainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityMainScope
class LoginViewModel @Inject constructor(): RepositoryViewModel(repository) {
    private val repository = getRepository()!!
    var onChangePassword: ((count: Int, kei: Int) -> Unit)? = null
    var onValidEmail: (() -> String?)? = null
    var onPerformLogin: () -> Unit = {}
    var onRegisterUser: (() -> Unit)? = null
    var onRestoreUser: (() -> Unit)? = null
    var onRequestProcessed: ((data: Any?, typeRequest: TypeRequest, result: Boolean)-> Unit)? = null
    private var userPassword    = ""

    fun setActivityFingerPrint(activity: FragmentActivity) {
        repository.setActivityFingerPrint(activity)
    }

    fun existPassword() = repository.existPassword()

    fun onClick(index: Int){
        if (index in 0..11) {
            //val key = index
            var changed = false
            when (index) {
                KEY_FINGER -> {
                    changed = true
                }
                KEY_BACKSPACE -> {
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
                val fingerPrint = index == KEY_FINGER
                if (!fingerPrint) onChangePassword?.invoke(userPassword.length, index)
                if (userPassword.length == 5 || fingerPrint)
                    onLogin(fingerPrint)
            }
        } else {
            when (index) {
                KEY_REG -> {
                    onRegisterUser?.invoke()
                }
                KEY_REST -> {
                    onRestoreUser?.invoke()
                }
            }
        }
    }

    fun getUserEmail(): String? = AppShopLocal.userShop.email//repository.shopUser?.email

    fun performRegisterUser(user: User){
        repository.onRegister(user) {
            onRequestProcessed?.invoke(user, TypeRequest.USER_REGISTER, it)
        }
    }

    fun performRestoreUser(user: User){
        repository.onRestore(user) {
            onRequestProcessed?.invoke(user, TypeRequest.USER_RESTORE, it)
        }
    }

    private fun onLogin(finger: Boolean){
        fun performOpenShop(value: Boolean){
            viewModelScope.launch {
                delay(300)
                onRequestProcessed?.invoke(null, TypeRequest.USER_LOGIN, value)
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

    companion object {
        const val KEY_FINGER      = 10
        const val KEY_BACKSPACE   = 11
        const val KEY_REG         = 12
        const val KEY_REST        = 13
    }
}