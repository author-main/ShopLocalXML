package com.example.shoplocalxml.ui.login.password_storage

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProperties.KEY_ALGORITHM_RSA
import android.util.Base64
import com.example.shoplocalxml.AppShopLocal.Companion.applicationContext
import com.example.shoplocalxml.FILE_PREFERENCES
import com.example.shoplocalxml.KEY_PASSWORD
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher

class PasswordStorageImpl: PasswordStorage {
    private val sharedPrefs: SharedPreferences =
        applicationContext.getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)
    private val providerKeyStore: String = "AndroidKeyStore"
    private val alias = applicationContext.packageName
    init{
        if (!initKeys())
            generateKeys()
    }

    private fun initKeys(): Boolean{
        val keyStore = getKeyStore() ?: return false
        return try {
            val privateKey  = keyStore.getKey(alias, null)
            val certificate = keyStore.getCertificate(alias)
            privateKey != null && certificate?.publicKey != null
        } catch (e: Exception){
            false
        }
    }

    private fun generateKeys(){
        if (getKeyStore() == null)
            return
        try {
            val spec: AlgorithmParameterSpec = KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_DECRYPT
            )
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .build()
            try {
                val kpGenerator =
                    KeyPairGenerator.getInstance(KEY_ALGORITHM_RSA, providerKeyStore)
                kpGenerator.initialize(spec)
                kpGenerator.generateKeyPair()
            } catch (_: Exception){}
        } catch (_: Exception) {}
    }

    private fun getKeyStore(): KeyStore?{
        return try{
            val keyStore = KeyStore.getInstance(providerKeyStore)
            keyStore?.load(null)
            keyStore
        } catch (e: Exception) {
            sharedPrefs.edit().remove(KEY_PASSWORD).apply()
            null
        }
    }

    private fun encrypt(encryptionKey: PublicKey, data: ByteArray): String? {
        return try {
            val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey)
            val encrypted = cipher.doFinal(data)
            Base64.encodeToString(encrypted, Base64.DEFAULT)
        } catch (e: Exception){
            null
        }
    }

    override fun getDecryptCipher(): Cipher? {
        val keyStore = getKeyStore() ?: return null
        return try {
            val privateKey: PrivateKey = keyStore.getKey(alias, null) as PrivateKey
            val cipher: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            cipher
        } catch (e: Exception){
            null
        }
    }

    override fun putPassword(value: String) {
        val keyStore = getKeyStore()
        val certificate = keyStore?.getCertificate(alias) ?: return
        try {
            val passwordUTF = value.toByteArray(Charsets.UTF_8)
            val encryptPassword = encrypt(
                certificate.publicKey,
                passwordUTF
            )
            encryptPassword?.let {
                sharedPrefs.edit().putString(KEY_PASSWORD, encryptPassword).apply()
            }
        } catch (_: Exception){}
    }

    override fun getPassword(): String? {
        return try {
            val encryptedPassword = sharedPrefs.getString(KEY_PASSWORD, null) ?: return null
            val passwordBase64: ByteArray = Base64.decode(encryptedPassword, Base64.DEFAULT)
            val cipher = getDecryptCipher()
            val password = cipher?.doFinal(passwordBase64)?.toString(Charsets.UTF_8)
            password
        } catch(_:Exception){
            null
        }
    }

    override fun removePassword() {
        sharedPrefs.edit().remove(KEY_PASSWORD).apply()
    }

    override fun existPassword(): Boolean {
        return !sharedPrefs.getString(KEY_PASSWORD, null).isNullOrBlank()
    }
}