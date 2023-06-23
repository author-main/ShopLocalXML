package com.example.shoplocalxml.repository

import android.graphics.Bitmap
import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.classes.image_downloader.ImageDownloadManager
import com.example.shoplocalxml.isConnectedNet
import com.example.shoplocalxml.log
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage
import com.example.shoplocalxml.repository.database_api.DatabaseApiImpl
import com.example.shoplocalxml.repository.database_handler.DatabaseHandler
import com.example.shoplocalxml.repository.database_handler.DatabaseHandlerImpl
import com.example.shoplocalxml.ui.dialog.DialogProgress
import com.example.shoplocalxml.ui.history_search.SearchQueryStorage
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

    private val databaseApi = DatabaseApiImpl()

    /**
     * accessHandler отвечает за обработку запросов пользователя
     * onLogin, onRestore, onRegister
     */
    private var accessHandler: AccessHandler        = AccessHandlerImpl(databaseApi)

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
     * @param user данные пользователя (см.[User])
     * @param action callback передает результат отправки данных,
     * true - данные успешно переданы
     */
    /*fun onRegister(vararg userdata: String, action: ((result: Boolean) -> Unit)) {
        accessHandler.onRegister(*userdata) {
            action(it)
        }
    }*/

    fun onRegister(user: User, action: ((result: Boolean) -> Unit)) {
        accessHandler.onRegister(user) {
            action(it)
        }
    }

    /**
     * Запрос пользователя на восстановление пароля
     * @param user данные пользователя (см. [User])
     * @param action callback передает результат отправки данных,
     * true - данные успешно переданы
     */
    fun onRestore(user:User, action: ((result: Boolean) -> Unit)) {
        accessHandler.onRestore(user) {
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

    fun getSearchHistoryItems(): List<String> =
        SearchQueryStorage.getInstance().getQueries()


    fun addSearchHistoryItem(value: String) {
        SearchQueryStorage.getInstance().put(value)
    }

    fun deleteSearchHistoryItem(value: String) {
        SearchQueryStorage.getInstance().remove(value)
    }

    fun saveSearchHistory() {
        SearchQueryStorage.getInstance().saveQueries()
    }

    fun clearSearchHistory() {
        SearchQueryStorage.getInstance().removeAllQueries()
    }

   /* fun downloadImage(url: String, reduce: Boolean = true, oncomplete: (Bitmap?) -> Unit){
        ImageDownloadManager.download(url, reduce, oncomplete)
    }*/

    suspend fun getProducts(page: Int, order: String): List<Product>? =
        token?.let {
           /* log("token $token")
            log("page $page")
            log("order $order")*/
            databaseHandler.getProducts(it, page, order)
        }

    suspend fun updateProductFavorite(idProduct: Int, favorite:Boolean) {
        token?.let {
            databaseHandler.updateProductFavorite(it, idProduct, favorite)
        }
    }

    suspend fun getBrends(): List<Brend>? =
        databaseHandler.getBrends()

    suspend fun getCategories(): List<Category>? =
        databaseHandler.getCategories()

}