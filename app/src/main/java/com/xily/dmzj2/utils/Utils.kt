package com.xily.dmzj2.utils

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.xily.dmzj2.App
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.regex.Pattern

/**
 * ToastUtil
 */
fun toast(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toast.makeText(App.instance, message, Toast.LENGTH_SHORT).show()
    }
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Activity.longToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.longToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

/**
 * SnackBarUtil
 */
fun showMessage(view: View, text: String) {
    Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
}

fun Activity.showMessage(text: String) {
    Snackbar.make(window.decorView, text, Snackbar.LENGTH_SHORT).show()
}

fun Fragment.showMessage(text: String) {
    activity?.showMessage(text)
}

/**
 * ColorUtil
 */
fun Activity.getAttrColor(resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

fun Fragment.getAttrColor(resId: Int): Int {
    return activity?.getAttrColor(resId) ?: 0
}

/**
 * otherTools
 */
inline fun <reified T : Activity> Activity.startActivity() {
    val intent = Intent(this, T::class.java)
    this.startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivity() {
    val intent = Intent(context, T::class.java)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivity(bundle: Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
}

inline fun <reified T : Activity> Fragment.startActivity(bundle: Bundle) {
    val intent = Intent(context, T::class.java)
    intent.putExtras(bundle)
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int) {
    val intent = Intent(this, T::class.java)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int) {
    val intent = Intent(context, T::class.java)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Activity.startActivityForResult(requestCode: Int, bundle: Bundle) {
    val intent = Intent(this, T::class.java)
    intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Fragment.startActivityForResult(requestCode: Int, bundle: Bundle) {
    val intent = Intent(context, T::class.java)
    intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Service> Activity.startService() {
    val intent = Intent(this, T::class.java)
    startService(intent)
}

inline fun checkTime(func: () -> Unit) {
    val startTime = System.currentTimeMillis()
    func()
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    debug(msg = "用时:$duration")
}

fun random(min: Int, max: Int): Int {
    val random = Random()
    return random.nextInt(max) % (max - min + 1) + min
}

fun getWeeks(startTime: Long, endTime: Long): Int {
    if (endTime < startTime) {
        return 1
    }
    val res = ((endTime - startTime) / (7 * 24 * 60 * 60 * 1000L) + 1).toInt()
    return if (res <= 0 || res >= 22) {
        1
    } else res
}

fun sha512(str: String): String {
    val messageDigest: MessageDigest
    var encodeStr = ""
    try {
        messageDigest = MessageDigest.getInstance("SHA-512")
        messageDigest.update(str.toByteArray(charset("UTF-8")))
        encodeStr = byte2Hex(messageDigest.digest())
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return encodeStr
}


fun md5(string: String): String {
    val md5: MessageDigest
    var encodeStr = ""
    try {
        md5 = MessageDigest.getInstance("MD5")
        encodeStr = byte2Hex(md5.digest(string.toByteArray()))
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return encodeStr
}

/**
 * 　　* 将byte转为16进制
 * 　　* @param bytes
 * 　　* @return
 */
private fun byte2Hex(bytes: ByteArray): String {
    val stringBuffer = StringBuilder()
    var temp: String
    for (aByte in bytes) {
        temp = Integer.toHexString(aByte.toInt() and 0xFF)
        if (temp.length == 1) {
            //1得到一位的进行补0操作
            stringBuffer.append("0")
        }
        stringBuffer.append(temp)
    }
    return stringBuffer.toString()
}

fun getWeekChinese(i: Int): String {
    return when (i) {
        0 -> "日"
        1 -> "一"
        2 -> "二"
        3 -> "三"
        4 -> "四"
        5 -> "五"
        6 -> "六"
        7 -> "日"
        else -> "无"
    }
}

fun getWeekNumber(str: String): Int {
    return when (str) {
        "一" -> 1
        "二" -> 2
        "三" -> 3
        "四" -> 4
        "五" -> 5
        "六" -> 6
        "日" -> 7
        else -> 1
    }
}

fun String.isInt(): Boolean {
    val pattern = Pattern.compile("^[-+]?[\\d]*$")
    return pattern.matcher(this).matches()
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.visibleOrGone() {
    visibility = if (visibility == View.VISIBLE) {
        View.GONE
    } else {
        View.VISIBLE
    }
}


/**
 * 高斯模糊
 */
fun rsBulr(context: Context, source: Bitmap, radius: Float, scale: Float): Bitmap {
    val inputBmp = if (scale == 1f) {
        source.copy(source.config, true)
    } else {
        val width = Math.round(source.width * scale)
        val height = Math.round(source.height * scale)
        Bitmap.createScaledBitmap(source, width, height, false)
    }
    val renderScript = RenderScript.create(context)
    val input = Allocation.createFromBitmap(renderScript, inputBmp)
    val output = Allocation.createTyped(renderScript, input.type)
    // Load up an instance of the specific script that we want to use.
    val scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    scriptIntrinsicBlur.setInput(input)
    // Set the blur radius
    scriptIntrinsicBlur.setRadius(radius)
    // Start the ScriptIntrinsicBlur
    scriptIntrinsicBlur.forEach(output)
    // Copy the output to the blurred bitmap
    output.copyTo(inputBmp)
    renderScript.destroy()
    return inputBmp
}