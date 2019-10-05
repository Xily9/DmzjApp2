package com.xily.dmzj2.utils

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.xily.dmzj2.App
import com.xily.dmzj2.R
import com.xily.dmzj2.data.DataManager
import com.xily.dmzj2.data.pref.PrefDelegate
import org.koin.android.ext.android.inject

/**
 * Created by Xily on 2017/10/24.
 */

object ThemeUtil {
    private var dialog: AlertDialog? = null
    private val colorList = intArrayOf(
        R.color.red,
        R.color.orange,
        R.color.pink,
        R.color.green,
        R.color.blue,
        R.color.purple,
        R.color.teal,
        R.color.brown,
        R.color.dark_blue,
        R.color.dark_purple
    )
    private val styleList = intArrayOf(
        R.style.AppThemeRed,
        R.style.AppThemeOrange,
        R.style.AppThemePink,
        R.style.AppThemeGreen,
        R.style.AppThemeBlue,
        R.style.AppThemePurple,
        R.style.AppThemeTeal,
        R.style.AppThemeBrown,
        R.style.AppThemeDarkBlue,
        R.style.AppThemeDarkPurple
    )
    private var theme: Int by PrefDelegate(4)

    fun setTheme(act: Activity) {
        act.setTheme(styleList[theme])
    }

    fun showSwitchThemeDialog(activity: Activity) {
        val context = App.instance.applicationContext
        val linearLayout = LinearLayout(context)
        val padding = dp2px(20f)
        linearLayout.setPadding(padding, padding, padding, padding)
        linearLayout.gravity = Gravity.CENTER_HORIZONTAL
        val frameLayout = FrameLayout(context)
        val colorList = colorList
        for (i in colorList.indices) {
            val button = Button(context)
            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(context.resources.getColor(colorList[i]))
            gradientDrawable.shape = GradientDrawable.OVAL
            button.background = gradientDrawable
            val layoutParams = FrameLayout.LayoutParams(dp2px(40f), dp2px(40f))
            layoutParams.leftMargin = dp2px(50f) * (i % 5) + dp2px(5f)
            layoutParams.topMargin = dp2px(50f) * (i / 5) + dp2px(5f)
            button.setOnClickListener {
                theme = i
                dialog!!.dismiss()
                activity.recreate()
            }
            if (i == theme) {
                button.text = "✔"
                button.setTextColor(Color.parseColor("#ffffff"))
                button.textSize = 15f
                button.gravity = Gravity.CENTER
            }
            frameLayout.addView(button, layoutParams)
        }
        linearLayout.addView(frameLayout)
        dialog = AlertDialog.Builder(activity).setTitle("设置主题").setView(linearLayout).show()
    }
}
