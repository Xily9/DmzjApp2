package com.xily.dmzj2.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        catchBlock: (suspend CoroutineScope.(Throwable) -> Unit) = {},
        finallyBlock: (suspend CoroutineScope.() -> Unit) = {}
    ) {
        viewModelScope.launch {
            try {
                tryBlock()
            } catch (e: CancellationException) {
            } catch (e: Exception) {
                catchBlock(e)
            } finally {
                finallyBlock()
            }
        }
    }

    fun <T> getData(block: suspend CoroutineScope.() -> T): LiveData<T> {
        val result = MutableLiveData<T>()
        launch(tryBlock = {
            result.value = block()
        }, catchBlock = {
            it.printStackTrace()
            result.value = null
        })
        return result
    }

    fun <T> getResultData(block: suspend CoroutineScope.() -> Result<T>): LiveData<Result<T>> {
        val result = MutableLiveData<Result<T>>()
        launch(tryBlock = {
            result.value = block()
        }, catchBlock = {
            it.printStackTrace()
            result.value = Result.failure(it)
        })
        return result
    }
}