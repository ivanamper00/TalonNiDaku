package com.haybakubo.gamebirich88

import android.os.Bundle
import android.os.CountDownTimer
import com.dakulangsakalam.customwebview.presentation.ui.jump.JumpActivity
import com.dakulangsakalam.customwebview.presentation.utils.writeLogs
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SplashActivity : JumpActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashAction(true) { version, downUrl ->
            writeLogs("Version: $version \n Url: $downUrl")
            startActivity(MainActivity.createIntent(this@SplashActivity))
        }
    }
}