package com.tracker.bioface_example

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
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
    private val urlRegister = "https://app.authme2.com.br/sso/9fa9da30-230b-4d09-98a6-58d8bb44d787"
    private lateinit var webview: WebView
    private lateinit var progress: ProgressBar
    private lateinit var buttonRegister: Button
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        webview = findViewById(R.id.webview)
        progress = findViewById(R.id.progress)
        buttonRegister = findViewById(R.id.button_register)
        buttonLogin = findViewById(R.id.button_login)

        promptHasPermissionCamera()
        startWebViewRegister()

        buttonRegister.setOnClickListener {
            startWebViewRegister()
        }

        buttonLogin.setOnClickListener {
            promptForUserIdAndLoadUrl()
        }
    }

    private fun promptHasPermissionCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    private fun startWebViewRegister() {
        configureWebView()
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
                    val loginUrl =
                        "https://app.authme2.com.br/liveness/9fa9da30-230b-4d09-98a6-58d8bb44d787/$userId"
                    configureWebView()
                    webview.loadUrl(loginUrl)
                } else {
                    Toast.makeText(this, "Por favor, insira um userId válido.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
        dialog.show()
    }

    private fun configureWebView() {
        webview.settings.javaScriptEnabled = true
        webview.settings.mediaPlaybackRequiresUserGesture = false
        webview.addJavascriptInterface(WebAppInterface(), "AndroidInterface")
        webview.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                request?.grant(request.resources)
            }
        }
        webview.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress.visibility = View.GONE
                Log.d("WebView", "Finished loading URL: $url")

                val jsCode = """
                    (function() {
                        function notifyRouteChange() {
                            AndroidInterface.onRouteChange(window.location.pathname);
                        }
                        window.addEventListener('popstate', notifyRouteChange);
                        (function(history){
                            var pushState = history.pushState;
                            var replaceState = history.replaceState;
                            history.pushState = function(state) {
                                var ret = pushState.apply(history, arguments);
                                notifyRouteChange();
                                return ret;
                            };
                            history.replaceState = function(state) {
                                var ret = replaceState.apply(history, arguments);
                                notifyRouteChange();
                                return ret;
                            };
                        })(window.history);
                        notifyRouteChange();
                    })();
                """.trimIndent()

                view?.evaluateJavascript(jsCode, null)
            }
        }
    }

    private fun mostrarAlerta(titulo: String, mensagem: String) {
        runOnUiThread {
            AlertDialog.Builder(this@MainActivity)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton("OK", null)
                .show()
        }
    }

    inner class WebAppInterface {
        @JavascriptInterface
        fun onRouteChange(newRoute: String) {
            Log.d("WebView", "Route changed to: $newRoute")
            runOnUiThread {
                when (newRoute) {
                    "/notification/Success" -> {
                        mostrarAlerta("Sucesso", "Sua identidade biométrica foi realizada com sucesso.")
                    }
                }
            }
        }
    }
}