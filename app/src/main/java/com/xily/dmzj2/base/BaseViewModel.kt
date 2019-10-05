package com.xily.dmzj2.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Created by Xily on 2019/10/5.
 */
abstract class BaseViewModel : ViewModel() {
    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) =
        viewModelScope.launch(context, start, block)

    fun launch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: (suspend CoroutineScope.(Throwable) -> Unit)? = null,
        finallyBlock: (suspend CoroutineScope.() -> Unit)? = null
    ) {
        viewModelScope.launch {
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