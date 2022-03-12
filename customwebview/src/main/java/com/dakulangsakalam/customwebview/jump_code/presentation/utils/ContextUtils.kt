package com.dakulangsakalam.customwebview.jump_code.presentation

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.dakulangsakalam.customwebview.jump_code.presentation.utils.writeLogs

fun JumpActivity.isNetworkConnected() : Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            }
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

fun JumpActivity.getAppIsRegistered(): Boolean {
    writeLogs("getAppIsRegistered ${getDefaultSharedPref().getBoolean("haveInstallAddOneTimes",false)}")
    return getDefaultSharedPref().getBoolean("haveInstallAddOneTimes",false)
}

fun JumpActivity.getDefaultSharedPref(): SharedPreferences{
    return PreferenceManager.getDefaultSharedPreferences(this)
}

fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

