package com.example.webviewsample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.webkit.JavascriptInterface

private const val ZOOM_INTENT = "com.android.settings.realwear_camera_zoom"
private const val SETTINGS_PACKAGE = "com.android.settings"
private const val ZOOM_RECEIVER = "com.android.settings.RealwearCameraZoomReceiver"

class JavaScriptInterface(private val mContext: Context) {
    @JavascriptInterface
    fun setZoom(zoomLevel: String?) {
        Log.e("JS INTERFACE", "Zoom level $zoomLevel")

        val intent = Intent(ZOOM_INTENT)
        intent.putExtra("zoomLevel", zoomLevel)
        intent.component = ComponentName(SETTINGS_PACKAGE, ZOOM_RECEIVER)
        mContext.sendBroadcast(intent)
    }
}
