package com.example.webviewsample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {

    private var myWebView: WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myWebView = WebView(this).apply {
            settings.mediaPlaybackRequiresUserGesture = false
            WebView.setWebContentsDebuggingEnabled(true)
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Simulate a click after 2 seconds
                    simulateClickAfterDelay()
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onPermissionRequest(request: PermissionRequest) {
                    request.grant(request.resources)
                }
            }
        }
        val cameraInterface = JavaScriptInterface(this)
        myWebView!!.addJavascriptInterface(cameraInterface, "Camera")
        // Load the local HTML page
        myWebView!!.loadUrl("file:///android_asset/index.html")
        setContentView(myWebView)
    }

    override fun onResume() {
        myWebView!!.evaluateJavascript("(function() { window.dispatchEvent(startCameraEvent); })();"
        ) {
            Log.e("startCamera", "js event received")
        }
        super.onResume()
    }

    override fun onPause() {
        myWebView!!.evaluateJavascript("(function() { window.dispatchEvent(stopCameraEvent); })();"
        ) {
            Log.e("stopCamera", "js event received")
        }
        super.onPause()
    }

    private fun simulateClickAfterDelay() {
        Handler(Looper.getMainLooper()).postDelayed({
            myWebView?.let { webView ->

                val eventTime = SystemClock.uptimeMillis()
                val x = webView.width / 2f // Center X
                val y = webView.height / 2f // Center Y


                val downEvent = MotionEvent.obtain(
                    eventTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0
                )

                val upEvent = MotionEvent.obtain(
                    eventTime, eventTime + 100, MotionEvent.ACTION_UP, x, y, 0
                )

                webView.dispatchTouchEvent(downEvent)
                webView.dispatchTouchEvent(upEvent)

                downEvent.recycle()
                upEvent.recycle()

                Log.d("WebViewSample", "Simulated click at center of WebView")
            }
        }, 500) // Delay of .5 seconds
    }
}