package com.example.shoplocalxml.repository

import androidx.fragment.app.FragmentActivity
import com.example.shoplocalxml.AppShopLocal
import com.example.shoplocalxml.classes.Brend
import com.example.shoplocalxml.classes.Category
import com.example.shoplocalxml.classes.Product
import com.example.shoplocalxml.classes.Review
import com.example.shoplocalxml.classes.User
import com.example.shoplocalxml.classes.UserMessage
import com.example.shoplocalxml.dagger.AppScope
import com.example.shoplocalxml.encodeBase64
import com.example.shoplocalxml.ui.login.password_storage.PasswordStorage
import com.example.shoplocalxml.repository.database_handler.DatabaseHandler
import com.example.shoplocalxml.ui.history_search.SearchQueryStorage
import com.example.shoplocalxml.ui.login.access_handler.AccessHandler
import javax.inject.Inject

@AppScope
class Repository @Inject constructor(private val accessHandler: AccessHandler,
                                     private val databaseHandler: DatabaseHandler,
                                     private val searchQueryStorage: SearchQueryStorage){
    /**
     * shopUser - данные текущего пользователя
     */
   /* val shopUser: User?
        get() = User.getUserData()*/

    /**
     * token - уникальный токен пользователя,
     * создается при входе пользователя в систему
     */
    private var token: String? = null
    /**
     * accessHandler отвечает за обработку запросов пользователя
     * onLogin, onRestore, onRegister
     */
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
        accessHandler.onLogin(email, password, finger, performAction) {
            token = it
            val result = it != null
            if (result) {
                AppShopLocal.userShop.getUserData()
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
        searchQueryStorage.getQueries()

    fun addSearchHistoryItem(value: String) {
        searchQueryStorage.put(value)
    }

    fun deleteSearchHistoryItem(value: String) {
        searchQueryStorage.remove(value)
    }

    fun saveSearchHistory() {
        searchQueryStorage.saveQueries()
    }

    fun clearSearchHistory() {
        searchQueryStorage.removeAllQueries()
    }

    suspend fun getProducts(page: Int, order: String): List<Product>? =
        token?.let {
            databaseHandler.getProducts(it, page, order)
        }

    suspend fun getSearchProducts(uuid: String, searchQuery: String, page: Int, order: String): List<Product>? =
        token?.let {
            val query64 = encodeBase64(searchQuery)
            databaseHandler.getSearchProducts(it, uuid, query64, page, order)
        }

    suspend fun getReviewsProduct(id: Int, limit: Int): List<Review>? =
            databaseHandler.getReviewsProduct(id, limit)

    suspend fun updateProductFavorite(idProduct: Int, favorite:Boolean) {
        token?.let {
            databaseHandler.updateProductFavorite(it, idProduct, favorite)
        }
    }

    suspend fun getBrends(): List<Brend>? =
        databaseHandler.getBrends()

    suspend fun getCategories(): List<Category>? =
        databaseHandler.getCategories()

    suspend fun getMessages(requestCount: Int): List<UserMessage>? =
        token?.let {
            databaseHandler.getMessages(it, requestCount)
        }

    suspend fun updateMessages(join_read: String, join_deleted: String) {
        token?.let {
            databaseHandler.updateMessages(it, join_read, join_deleted)
        }
    }
}