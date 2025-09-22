package com.example.car.pref

import android.content.Context

class Prefs(context: Context) {
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun isOnBoardShown(): Boolean = prefs.getBoolean("onBoardingShown", false)

    fun setOnBoardShown() {
        prefs.edit().putBoolean("onBoardingShown", true).apply()
    }
}
