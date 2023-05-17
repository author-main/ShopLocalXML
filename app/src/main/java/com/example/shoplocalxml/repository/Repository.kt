package com.example.shoplocalxml.repository

import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.isConnectedNet
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage
import com.example.shoplocalxml.repository.database_api.DatabaseApiImpl
import com.example.shoplocalxml.repository.database_handler.DatabaseHandler
import com.example.shoplocalxml.repository.database_handler.DatabaseHandlerImpl
import com.example.shoplocalxml.ui.dialog.DialogProgress
import com.example.shoplocalxml.ui.login.access_handler.AccessHandler
import com.example.shoplocalxml.ui.login.access_handler.AccessHandlerImpl

class Repository {
    /**
     * shopUser - данные текущего пользователя
     */
    val shopUser: User?
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
     * Запрос пользователя на вход в систему
     * @param email String email пользователя
     * @param password String пароль пользователя
     * @param finger Boolean, true если вход по отпечатку
     * @param performAction callback обработка события до начала проверки login,
     * отображаем DialogProgress, заполняем все символы пароля с использованием FingerPrint
     * @param action callback передает token пользователя,
     * token == null при неудачной попытке входа в систему
     */
    fun onLogin(email: String, password: String, finger: Boolean, performAction: ()-> Unit, action: (result: Boolean) -> Unit) {
        //log("connected ${isConnectedNet()}...")
        accessHandler.onLogin(email, password, finger, performAction) { it ->
            token = it
            log("token = $token")
            val result = it != null
            if (result) {
                val user = User.getUserData()
                user?.let{_user ->
                    _user.email = email
                    _user.saveUserData()
                }
            }
            action(result)
        }
    }

    /**
     * Запрос пользователя на регистрацию в системе
     * @param userdata строковый массив сведений о пользователе (см.[User])
     * @param action callback передает результат отправки данных,
     * true - данные успешно переданы
     */
    fun onRegister(vararg userdata: String, action: ((result: Boolean) -> Unit)) {
        accessHandler.onRegister(*userdata) {
            action(it)
        }
    }

    /**
     * Запрос пользователя на восстановление пароля
     * @param email String email пользователя
     * @param password String пароль пользователя
     * @param action callback передает результат отправки данных,
     * true - данные успешно переданы
     */
    fun onRestore(email: String, password: String, action: ((result: Boolean) -> Unit)) {
        accessHandler.onRestore(email, password) {
            action(it)
        }
    }

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
     * Проверить сохранен ли пароль пользователя в хранилище (см.[PasswordStorage])
     * @return true, если пароль сохранен в хранилище
     * (используется для входа по отпечатку)
     */
    fun existPassword(): Boolean {
        return accessHandler.passwordStorage?.existPassword() ?: false
    }
}