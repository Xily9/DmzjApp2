package com.xily.dmzj2.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.xily.dmzj2.utils.ThemeUtil

abstract class BaseActivity : AppCompatActivity() {

    /**
     * 设置布局layout
     *
     * @return
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtil.setTheme(this)
        //设置布局内容
        setContentView(getLayoutId())
        //初始化控件
        initViews(savedInstanceState)
    }

    /**
     * 初始化views
     *
     * @param savedInstanceState
     */
    abstract fun initViews(savedInstanceState: Bundle?)

}