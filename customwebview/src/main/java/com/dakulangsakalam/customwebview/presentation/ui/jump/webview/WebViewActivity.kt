package com.dakulangsakalam.customwebview.presentation.ui.jump.webview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import android.webkit.WebView.HitTestResult
import android.webkit.WebView.WebViewTransport
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import com.dakulangsakalam.customwebview.databinding.ActivityWebViewBinding
import com.dakulangsakalam.customwebview.presentation.helper.PermissionHelper
import com.dakulangsakalam.customwebview.presentation.showToast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.system.exitProcess

class WebViewActivity : AppCompatActivity() {

    lateinit var binding : ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(this)
        binding = ActivityWebViewBinding.inflate(inflater, null, false)
        setContentView(binding.root)

        val url = intent.getStringExtra(URL)
        binding.webView.loadUrl(url ?: "")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    Toast.makeText(applicationContext, "Press again to exit the program", Toast.LENGTH_SHORT).show()
                    exitTime = System.currentTimeMillis()
                } else {
                    finish()
                    exitProcess(0)
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object{
        var exitTime: Long = 0
        const val URL = "url"

        fun createIntent(context: Context): Intent = Intent(context, WebViewActivity::class.java)
        fun createIntent(context: Context, url: String): Intent {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(URL, url);
            return intent
        }
    }

}