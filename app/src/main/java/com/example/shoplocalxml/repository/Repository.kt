package com.example.shoplocalxml.repository

import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.ui.login.access_handler.AccessHandler
import com.example.shoplocalxml.ui.login.access_handler.AccessHandlerImpl

class Repository {
    private val accessHandler: AccessHandler = AccessHandlerImpl()

    /**
     * Установить FragmentActivity для BiometricPrompt
     * (используется для входа по отпечатку)
     * @param activity FragmentActivity, в даном случае MainActivity
     */
    fun setActivityFingerPrint(activity: FragmentActivity) {
        accessHandler.setActivityFingerPrint(activity)
    }

    /**
     * Проверить сохранен ли пароль пользователя в PasswordStorage
     * @return true, если пароль сохранен в хранилище
     * (используется для входа по отпечатку)
     */
    fun existPassword(): Boolean {
        return accessHandler.passwordStorage?.existPassword() ?: false
    }
}