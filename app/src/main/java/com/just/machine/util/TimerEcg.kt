package com.just.machine.util

import com.common.network.LogUtils
import java.util.Timer
import java.util.TimerTask

/**
 *create by 2024/1/17
 * ECG心电图
 *@author zt
 */
class TimerEcg {
    private var tag: String = TimerEcg::javaClass.name

    // 在类中声明 Timer 和 TimerTask
    private var timer1: Timer? = null
    private var timerTask: TimerTask? = null
    var ecgData: EcgTimerListener? = null

    fun ecgTimerListener(ecgData: EcgTimerListener) {
        this.ecgData = ecgData
    }

    // 在合适的地方初始化和启动定时器
    fun startTimer(oftenListData: MutableList<Float>): TimerEcg {
        timer1 = Timer()

        timerTask = object : TimerTask() {
            override fun run() {
                try {
                    if (oftenListData.isEmpty()) {
                        return
                    }
                    val cooY = oftenListData[0]
                    ecgData?.ecgTimerView(cooY)
                    oftenListData.removeAt(0)
                } catch (e: Exception) {
                    LogUtils.d(tag + "EcgError" + e.message)
                }
            }
        }
        timer1?.schedule(timerTask, 1, 10)
        return this
    }

    // 在需要的地方停止定时器
    fun stopTimer() {
        timerTask?.cancel()
        timer1?.cancel()
    }

    interface EcgTimerListener {
        fun ecgTimerView(cooY: Float)
    }

}