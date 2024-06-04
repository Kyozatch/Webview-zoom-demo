package com.example.webviewsample

import android.os.Bundle
import android.util.Log
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {

    private var myWebView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myWebView = WebView(this)
        myWebView!!.settings.javaScriptEnabled = true
        myWebView!!.getSettings().setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);
        myWebView!!.getSettings().mediaPlaybackRequiresUserGesture = false;

        myWebView!!.setWebViewClient(WebViewClient())
        myWebView!!.setWebChromeClient(object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                request.grant(request.resources)
            }
        })

        val cameraInterface = JavaScriptInterface(this)
        myWebView!!.addJavascriptInterface(cameraInterface, "Camera")

        myWebView!!.loadUrl("file:///android_asset/index.html")
        setContentView(myWebView)
    }

    override fun onPause() {
        myWebView!!.evaluateJavascript("(function() { window.dispatchEvent(stopCameraEvent); })();"
        ) {
            Log.e("stopCamera", "js event received")
        }
        super.onPause()
    }
}