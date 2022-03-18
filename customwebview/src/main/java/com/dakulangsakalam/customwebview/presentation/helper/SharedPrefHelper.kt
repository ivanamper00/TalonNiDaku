package com.dakulangsakalam.customwebview.presentation.helper

import android.content.Context
import android.content.ContextWrapper
import com.dakulangsakalam.customwebview.presentation.utils.writeLogs

class SharedPrefHelper(context: Context): ContextWrapper(context) {

    val sharedPreferences by lazy {
        getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    }

    companion object{
        private const val SHARED_PREFS = "sharedPrefs"
        const val APP_FRESH_OPEN = "APP_FRESH_OPEN"
        var INSTANCE : SharedPrefHelper? = null

        fun getInstance(context: Context): SharedPrefHelper {
            writeLogs("Shared Preferences instance: $INSTANCE")
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPrefHelper(context).also {
                    INSTANCE = it
                }
            }
        }
    }
}