package com.common.viewmodel

import androidx.lifecycle.viewModelScope
import com.common.BaseResponse
import com.common.BaseResponseDB
import com.common.throwe.BaseResponseThrowable
import com.common.throwe.ThrowableHandler
import com.common.viewmodel.LiveDataEvent.Companion.SUCCESS
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

/**
 *create by 2024/2/27
 * ViewModel 基础类
 *@author zt
 */
open class BaseViewModel() : BaseLifeViewModel() {

    protected var tag = BaseViewModel::class.simpleName

    private var stateView = StateView()

    private fun launchUi(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch { block() }

    /**
     * 直接获取结果的
     */
    fun <T> async(
        request: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        error: suspend CoroutineScope.(BaseResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit = {}
    ) {
        launchUi {
            handleRequest(withContext(Dispatchers.IO) {
                request
            }, {
                success(it)
            }, {
                error(it)
            }, {
                complete()
            })
        }
    }

    //过滤结果
    fun <T> asyncExecute(
        request: suspend CoroutineScope.() -> BaseResponse<T>,
        success: (T) -> Unit,
        error: suspend CoroutineScope.(BaseResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit = {}
    ) {
        launchUi {
            handleRequest(withContext(Dispatchers.IO) {
                request
            }, { response ->
                executeResponse(response) {
                    success(it)
                }
            }, {
                error(it)
            }, {
                complete()
            })
        }
    }

    /**
     * 带loading的请求
     */
    fun <T> async(
        request: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        showDialog: Boolean = true,
        error: suspend CoroutineScope.(BaseResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit = {
            if (showDialog) {
                stateView.isLoading.value = false
            }
        }

    ) {
        if (showDialog) {
            stateView.isLoading.value = true
        }
        launchUi {
            handleRequest(withContext(Dispatchers.IO) {
                request
            }, {
                viewModelScope
                success(it)
            }, {
                error(it)
            }, {
                complete()
            })
        }
    }

    private suspend fun <T> handleRequest(
        block: suspend CoroutineScope.() -> T,
        success: suspend CoroutineScope.(T) -> Unit,
        error: suspend CoroutineScope.(BaseResponseThrowable) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                success(block())
            } catch (e: java.lang.Exception) {
                error(ThrowableHandler.handleThrowable(e))
            } finally {
                complete()
            }
        }
    }

    //网络过滤返回数据
    private suspend fun <T> executeResponse(
        response: BaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            if (response.code == SUCCESS) success(response.data)
            else {
                error(BaseResponseThrowable(response.code, response.errorMsg))
            }
        }
    }

    var mEventHub: SingleLiveEvent<LiveDataEvent> =
        SingleLiveEvent<LiveDataEvent>()

    fun <T> setBean(
        bean: T,
        insertFunction: suspend (T) -> Unit,
        getFunction: suspend () -> Flow<List<T>>,
        eventType: Int
    ) {
        viewModelScope.launch {
            insertFunction(bean)
            getFunction().collect {
                mEventHub.value = LiveDataEvent(eventType, it)
            }
        }
    }
}

