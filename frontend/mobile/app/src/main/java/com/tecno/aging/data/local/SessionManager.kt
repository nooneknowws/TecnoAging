
package com.tecno.aging.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SessionManager {
    private const val PREFS_NAME = "tecno_aging_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_PROFILE = "user_profile"
    private const val KEY_USER_NAME = "user_name"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    fun saveAuthToken(token: String?) {
        prefs.edit { putString(KEY_TOKEN, token) }
    }

    fun getAuthToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun saveUserName(userName: String?){
        prefs.edit {putString(KEY_USER_NAME, userName)}
    }
    fun getUserName(): String? = prefs.getString(KEY_USER_NAME, null)

    fun saveUserId(userId: String?) {
        prefs.edit { putString(KEY_USER_ID, userId) }
    }


    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun saveUserProfile(profile: String?) {
        prefs.edit { putString(KEY_USER_PROFILE, profile) }
    }

    fun getUserProfile(): String? = prefs.getString(KEY_USER_PROFILE, null)

    fun clearAuthToken() {
        val editor = prefs.edit()
        editor.remove(KEY_TOKEN)
        editor.remove(KEY_USER_NAME)
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_USER_PROFILE)
        editor.apply()
    }
}