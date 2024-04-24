package com.justsafe.libview

import android.content.Context
import android.content.IntentFilter
import com.justsafe.libview.core.LBusEvent
import com.justsafe.libview.core.LBusManager
import com.justsafe.libview.core.LBusReceiver

object LBus {

    fun init(context: Context) {
        LBusManager.init(context)
    }

    fun register(receiver: LBusReceiver<*>, intentFilter: IntentFilter) {
        LBusManager.register(receiver, intentFilter)
    }

    fun register(receiver: LBusReceiver<*>, actionArray: Array<String>) {
        if (actionArray.isEmpty()) {
            throw IllegalArgumentException("action is empty")
        }
        val intentFilter = IntentFilter()
        actionArray.forEach { action ->
            intentFilter.addAction(action)
        }
        register(receiver, intentFilter)
    }

    fun send(lBusEvent: LBusEvent) {
        LBusManager.send(lBusEvent)
    }

}

