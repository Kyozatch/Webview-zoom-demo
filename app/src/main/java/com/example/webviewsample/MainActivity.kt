package com.example.webviewsample

import android.os.Bundle
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

    fun onCameraStreamReady() {
        Thread {
            try {
                clickOnWebViewAfterDelay()
            } catch (e: Exception) {
                Log.e("onCameraStreamReady", e.message!!)
            }
        }.start()
    }

    private fun clickOnWebViewAfterDelay() {
        val eventTime = SystemClock.uptimeMillis()
        val downEvent = MotionEvent.obtain(
            eventTime, eventTime, MotionEvent.ACTION_DOWN, 0f, 0f, 0
        )
        val upEvent = MotionEvent.obtain(
            eventTime, eventTime + 100, MotionEvent.ACTION_UP, 0f, 0f, 0
        )

        myWebView?.dispatchTouchEvent(downEvent)
        myWebView?.dispatchTouchEvent(upEvent)

        downEvent.recycle()
        upEvent.recycle()

        Log.e("MainActivity", "Simulated click at x:0 y:0 of WebView")
    }
}