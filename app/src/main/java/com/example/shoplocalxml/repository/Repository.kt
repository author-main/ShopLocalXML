package com.example.shoplocalxml.repository

import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.repository.database_api.DatabaseApi
import com.example.shoplocalxml.repository.database_api.DatabaseApiImpl
import com.example.shoplocalxml.repository.database_handler.DatabaseHandler
import com.example.shoplocalxml.repository.database_handler.DatabaseHandlerImpl
import com.example.shoplocalxml.ui.login.access_handler.AccessHandler
import com.example.shoplocalxml.ui.login.access_handler.AccessHandlerImpl

class Repository {
    /**
     * user - данные текущего пользователя
     */
    val user: User?
        get() = User.getUserData()

    /**
     * token - уникальный токен пользователя,
     * создается при входе пользователя в систему
     */
    private var token: String? = null

    private val databaseApi:DatabaseApiImpl         = DatabaseApiImpl()

    /**
     * accessHandler отвечает за обработку запросов пользователя
     * onLogin, onRestore, onRegister
     */
    private val accessHandler: AccessHandler        = AccessHandlerImpl(databaseApi)

    /**
     * databaseHandler обрабатывает запросы к БД
     */
    private val databaseHandler: DatabaseHandler    = DatabaseHandlerImpl(databaseApi)

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