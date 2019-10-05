package com.xily.dmzj2.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.xily.dmzj2.App

private val outMetrics by lazy { App.instance.resources.displayMetrics }

/**
 * 获取设备宽度
 *
 * @return
 */
val deviceWidth: Int
    get() = outMetrics.widthPixels

/**
 * 获取设备高度
 *
 * @return
 */
val deviceHeight: Int
    get() = outMetrics.heightPixels

val cacheDir: String
    get() = App.instance.externalCacheDir!!.path

/**
 * dp转px
 *
 * @param dpValue dp值
 * @return
 */
fun dp2px(dpValue: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, outMetrics).toInt()
}

fun Activity.setStatusBarUpper() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    window.statusBarColor = Color.TRANSPARENT
}

fun isServiceRunning(ServicePackageName: String): Boolean {
    val manager = App.instance.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (ServicePackageName == service.service.className) {
            return true
        }
    }
    return false
}

fun Activity.hideSoftInput() {
    val imm = App.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

/**
 * 显示或隐藏StatusBar
 *
 * @param enable false 显示，true 隐藏
 */
fun hideStatusBar(window: Window, enable: Boolean) {
    val p = window.attributes
    if (enable) {
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    } else {
        p.flags = p.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    window.attributes = p
}

fun Activity.setDarkStatusIcon(dark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decorView = window.decorView
        var vis = decorView.systemUiVisibility
        vis = if (dark) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decorView.systemUiVisibility = vis
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#33000000")
    }
}
