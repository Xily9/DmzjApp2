package com.xily.dmzj2.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.xily.dmzj2.App
import com.xily.dmzj2.R
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
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

fun ImageView.load(path: Any?, type: Int = 1) {
    val img = if (type == 1) R.drawable.ic_default_image_vertical else R.drawable.ic_default_image
    val options = RequestOptions()
        .placeholder(img)
        .error(img)
    if (path is String) {
        val glideUrl = GlideUrl(
            path, LazyHeaders.Builder()
                .addHeader("Referer", "http://m.dmzj.com")
                .build()
        )
        Glide.with(context).load(glideUrl)
            .apply(options)
            .into(this)
    } else {
        Glide.with(context).load(path).apply(options).into(this)
    }
}

fun Int.toTimeStr(): String {
    val time = System.currentTimeMillis() / 1000
    val seconds = time - this
    if (seconds < 10) {
        return "刚刚"
    }
    if (seconds < 60) {
        return "${seconds}秒前"
    }
    val minutes = seconds / 60
    if (minutes < 60) {
        return "${minutes}分钟前"
    }
    val hours = minutes / 60
    if (hours < 24) {
        return "${hours}小时前"
    }
    /*val calendar = Calendar.getInstance()
    val day1 = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date(this * 1000L)
    val day2 = calendar.get(Calendar.DAY_OF_MONTH)
    if (day1 == day2) {
        return SimpleDateFormat("HH:mm", Locale.CHINA).format(this * 1000L)
    }*/
    return SimpleDateFormat("MM-dd", Locale.CHINA).format(this * 1000L)
}