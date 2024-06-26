package com.justsafe.libview.core

import android.content.Context
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager


object LBusManager {

    private var application: Context? = null

    /**
     * LocalBroadcastManager中也是一个单例，所以我们可以也使用一个单例保存起来，方便使用的过程中调用
     * 就不需要每次都获取上下文了
     */
    private var localBroadcastManager: LocalBroadcastManager? = null

    fun init(context: Context) {
        val applicationContext = context.applicationContext
        application = applicationContext
        localBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)
    }

    fun register(receiver: LBusReceiver<*>, intentFilter: IntentFilter) {
        Log.e("LBus", "register")
        localBroadcastManager?.registerReceiver(receiver, intentFilter)
    }

    fun unregister(receiver: LBusReceiver<*>) {
        localBroadcastManager?.unregisterReceiver(receiver)
    }

    fun send(event: LBusEvent) {
        if (event.action.isEmpty()) {
            // 如果Action为空，那么就不让它发出去了
            Log.e("LBus", "LBusEvent action is empty")
            return
        }
        Log.e("LBus", "sendBroadcast(event.intent)")
        localBroadcastManager?.sendBroadcast(event.intent)
    }

}