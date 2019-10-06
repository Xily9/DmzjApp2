package com.xily.dmzj2.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.xily.dmzj2.utils.setDarkStatusIcon
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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
        //设置布局内容
        setContentView(getLayoutId())
        setDarkStatusIcon(true)
        //初始化控件
        initViews(savedInstanceState)
    }

    /**
     * 初始化views
     *
     * @param savedInstanceState
     */
    abstract fun initViews(savedInstanceState: Bundle?)

    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) =
        lifecycleScope.launch(context, start, block)

    fun launch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: (suspend CoroutineScope.(Throwable) -> Unit)? = null,
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null
    ) {
        lifecycleScope.launch {
            try {
                tryBlock()
            } catch (e: CancellationException) {
            } catch (e: Exception) {
                catchBlock?.let {
                    it(e)
                }
            } finally {
                finallyBlock?.let {
                    it()
                }
            }
        }
    }
}