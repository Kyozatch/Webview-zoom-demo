package com.example.webviewsample

import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myWebView = WebView(this)
        myWebView.settings.javaScriptEnabled = true
        myWebView.getSettings().setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        myWebView.getSettings().mediaPlaybackRequiresUserGesture = false;

        myWebView.setWebViewClient(WebViewClient())
        myWebView.setWebChromeClient(object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }
        })

        val cameraInterface = JavaScriptInterface(this)
        myWebView.addJavascriptInterface(cameraInterface, "Camera")

        myWebView.loadUrl("file:///android_asset/index.html")
        setContentView(myWebView)
    }
}