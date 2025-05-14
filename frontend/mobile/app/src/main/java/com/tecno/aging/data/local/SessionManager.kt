
package com.tecno.aging.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SessionManager {
    private const val PREFS_NAME = "tecno_aging_prefs"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_PROFILE = "user_profile"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    fun saveAuthToken(token: String?) {
        prefs.edit { putString(KEY_TOKEN, token) }
    }

    fun getAuthToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun saveUserId(userId: String?) {
        prefs.edit { putString(KEY_USER_ID, userId) }
    }

    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun saveUserProfile(profile: String?) {
        prefs.edit { putString(KEY_PROFILE, profile) }
    }

    fun getUserProfile(): String? = prefs.getString(KEY_PROFILE, null)

    fun clearSession() {
        prefs.edit {
            remove(KEY_TOKEN)
            remove(KEY_USER_ID)
            remove(KEY_PROFILE)
        }
    }
}