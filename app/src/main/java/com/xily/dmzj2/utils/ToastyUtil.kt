package com.xily.dmzj2.utils

import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.xily.dmzj2.App
import es.dmoral.toasty.Toasty

/**
 * Created by Xily on 2019/4/11.
 */
fun toastError(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.error(App.instance, message).show()
    }
}

fun Activity.toastError(message: String) {
    Toasty.error(this, message).show()
}

fun Fragment.toastError(message: String) {
    activity?.toastError(message)
}

fun toastInfo(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.info(App.instance, message).show()
    }
}

fun Activity.toastInfo(message: String) {
    Toasty.info(this, message).show()
}

fun Fragment.toastInfo(message: String) {
    activity?.toastInfo(message)
}

fun toastSuccess(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.success(App.instance, message).show()
    }
}

fun Activity.toastSuccess(message: String) {
    Toasty.success(this, message).show()
}

fun Fragment.toastSuccess(message: String) {
    activity?.toastSuccess(message)
}

fun toastWarning(message: String) {
    Handler(Looper.getMainLooper()).post {
        Toasty.warning(App.instance, message).show()
    }
}

fun Activity.toastWarning(message: String) {
    Toasty.warning(this, message).show()
}

fun Fragment.toastWarning(message: String) {
    activity?.toastWarning(message)
}