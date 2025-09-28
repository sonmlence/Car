package com.example.car.pref

import android.content.Context

class Prefs(context: Context) {
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun isOnBoardShown(): Boolean =
        prefs.getBoolean("onBoardingShown", false)

    fun setOnBoardShown() {
        prefs.edit().putBoolean("onBoardingShown", true).apply()
    }

    fun isUserLoggedIn(): Boolean =
        prefs.getBoolean("user_logged_in", false)

    fun setUserLoggedIn(loggedIn: Boolean) {
        prefs.edit().putBoolean("user_logged_in", loggedIn).apply()
    }

    fun saveUserInfo(name: String?, photoUrl: String?) {
        prefs.edit()
            .putString("user_name", name)
            .putString("user_photo", photoUrl)
            .apply()
    }

    fun getUserName(): String? = prefs.getString("user_name", null)
    fun getUserPhoto(): String? = prefs.getString("user_photo", null)
}
