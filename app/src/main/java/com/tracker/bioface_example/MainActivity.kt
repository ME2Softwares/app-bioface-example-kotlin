package com.tracker.bioface_example

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    private val urlRegister: String = "https://app.authme2.com.br/sso/9fa9da30-230b-4d09-98a6-58d8bb44d787"
    private lateinit var webview: WebView
    private lateinit var progress: ProgressBar
    private lateinit var buttonRegister: Button
    private lateinit var buttonLogin: Button
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private val FILECHOOSER_RESULTCODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        webview = findViewById(R.id.webview)
        progress = findViewById(R.id.progress)
        buttonRegister = findViewById(R.id.button_register)
        buttonLogin = findViewById(R.id.button_login)

        startWebViewRegister()
        promptHasPermissionCamera()

        buttonRegister.setOnClickListener {
            startWebViewRegister()
        }

        buttonLogin.setOnClickListener {
            promptForUserIdAndLoadUrl()
        }
    }

    private fun promptHasPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    private fun startWebViewRegister() {
        webview.settings.javaScriptEnabled = true
        webview.settings.mediaPlaybackRequiresUserGesture = false
        webview.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }
        }
        setWebViewClient(webview)
        webview.loadUrl(urlRegister)
    }

    private fun promptForUserIdAndLoadUrl() {
        val editText = EditText(this)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Insira o userId")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                val userId = editText.text.toString()
                if (userId.isNotEmpty()) {
                    val loginUrl = "https://app.authme2.com.br/liveness/9fa9da30-230b-4d09-98a6-58d8bb44d787/$userId"
                    webview.settings.javaScriptEnabled = true
                    webview.settings.mediaPlaybackRequiresUserGesture = false
                    webview.webChromeClient = object : WebChromeClient() {
                        override fun onPermissionRequest(request: PermissionRequest?) {
                            request?.grant(request.resources)
                        }
                    }
                    setWebViewClient(webview)
                    webview.loadUrl(loginUrl)

                } else {
                    Toast.makeText(this, "Por favor, insira um userId válido.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    private fun setWebViewClient(webView: WebView?) {
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progress.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress.visibility = View.GONE
            }
        }
    }
}