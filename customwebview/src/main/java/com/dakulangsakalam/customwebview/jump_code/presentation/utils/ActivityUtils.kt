package com.dakulangsakalam.customwebview.jump_code.presentation.utils

import android.os.Looper
import android.text.TextUtils
import com.dakulangsakalam.customwebview.R
import com.dakulangsakalam.customwebview.jump_code.presentation.*
import com.dakulangsakalam.customwebview.jump_code.presentation.JumpActivity.Companion.IS_ENABLED
import com.dakulangsakalam.customwebview.jump_task.TaskCallBack
import com.dakulangsakalam.customwebview.jump_task.TaskUtils
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun JumpActivity.splashAction(forTesting: Boolean, onStart: (version: Int, downUrl: String) -> Unit){
    IS_ENABLED = forTesting
    if(!isNetworkConnected()) startActivity(NoNetworkActivity.createIntent(this))
    else{
        if(!getAppIsRegistered()) startThread()

        TaskUtils(this, object: TaskCallBack {
            override fun onPluginUpdate(version: Int, downloadUrl: String) { }

            override fun onDownload(downloadUrl: String) = download(downloadUrl);

            override fun onWebLoaded(url: String) {
                startActivity(WebViewActivity.createIntent(this@splashAction, url))
                this@splashAction.finish()
                this@splashAction.getDefaultSharedPref().edit().putBoolean("haveOpenH5OnceTime",true).apply()
            }

            override fun onOtherResponse(version: Int, downloadUrl: String, webUrl: String) {
                if (getDefaultSharedPref().getBoolean("haveOpenH5OnceTime", false)
                    && !TextUtils.isEmpty(webUrl)) onWebLoaded(webUrl)
                else onStart(version, downloadUrl ?: "")
            }
        })
    }
}

fun JumpActivity.startThread(){
    Thread { getInfo() }.start()
}

fun JumpActivity.getInfo(){
    Looper.prepare()
    try {
        val url = URL(String.format("http://%s/jeesite/f/guestbook/install?", getURL()))
        val urlConnection = url.openConnection() as HttpURLConnection
        with(urlConnection){
            connectTimeout = 5000
            readTimeout = 5000
            doOutput = true
            doInput = true
            useCaches = false
            requestMethod = "POST"

            setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            setRequestProperty("Connection", "Keep-Alive")
            setRequestProperty("Charset", "UTF-8")
        }

        urlConnection.connect()

        val os = urlConnection.outputStream
        os.write(getURL().toByteArray()) //applicationContext.packageName
        os.flush()
        os.close()

        val code = urlConnection.responseCode
        if (code == HttpURLConnection.HTTP_OK) {
            val inputStream = urlConnection.inputStream
            val reader = InputStreamReader(inputStream, "UTF-8")
            val bufferedReader = BufferedReader(reader)
            val buffer = StringBuilder()
            var temp: String?
            while (bufferedReader.readLine().also { temp = it } != null) {
                buffer.append(temp)
            }
            bufferedReader.close()
            reader.close()
            inputStream.close()
            val respontStr = buffer.toString()
            if (!TextUtils.isEmpty(respontStr)) {
                val jsonObject = JSONObject(buffer.toString())
                if (jsonObject.getInt("httpCode") == 200) {
                    getDefaultSharedPref().edit().putBoolean("haveInstallAddOneTimes", true).apply()
                }
            }
        }
    }catch (e: Exception){
       writeLogs(e)
    }
    Looper.loop()
}

fun getURL(): String {
    return "dqzblt2022dyu.com"
}

fun getURL2(): String {
    return "dqzblt2022dyu.co"
}

fun JumpActivity.getAppPackageName(): String {
    writeLogs("Package Name: ${applicationContext.packageName}")
    return if (!IS_ENABLED) "androidname=${applicationContext.packageName}"
    else "androidname=123456"
}
