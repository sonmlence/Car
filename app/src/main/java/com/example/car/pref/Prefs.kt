package com.example.car.pref

import android.content.Context

class Prefs(context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun isOnBoardShown(): Boolean {
        return prefs.getBoolean("onboard_shown", false)
    }

    fun setOnBoardShown() {
        prefs.edit().putBoolean("onboard_shown", true).apply()
    }
}
