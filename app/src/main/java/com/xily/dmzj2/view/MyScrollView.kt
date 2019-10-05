package com.xily.dmzj2.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class MyScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {
    private var onScrollChangeListener: ((Int) -> Unit)? = null
    fun setOnScrollChangeListener(onScrollChangeListener: ((Int) -> Unit)) {
        this.onScrollChangeListener = onScrollChangeListener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollChangeListener?.invoke(t)
    }
}