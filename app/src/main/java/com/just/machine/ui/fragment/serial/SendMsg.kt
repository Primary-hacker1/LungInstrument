package com.just.machine.ui.fragment.serial

import com.common.network.LogUtils
import com.just.machine.util.BaseUtil
import world.shanya.serialport.SerialPort
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/*
* 线程池发送命令
* 延时发送串口消息
* */
class SendMsg(
    message: String,
    serialPort: SerialPort,
    corePoolSize: Int = 2,
    maxPoolSize: Int = 2,
    keepAliveTime: Long = 0L,
    threadSleep: Long = 300L
) {
    init {
        val myQueue: BlockingQueue<String> = LinkedBlockingQueue()
        var executor = ThreadPoolExecutor(
            corePoolSize,
            maxPoolSize,
            keepAliveTime,
            TimeUnit.MILLISECONDS,
            LinkedBlockingQueue()
        )

        try {
            executor.execute(Producer(myQueue, message, threadSleep, serialPort))
        } catch (e: Exception) {
            executor = ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                LinkedBlockingQueue()
            )
            executor.execute(Producer(myQueue, message, threadSleep, serialPort))
        }
        try {
            // 等待生产者线程完成
            executor.shutdown()
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)
            // 向队列发送停止信号
            myQueue.put("STOP")
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    internal class Producer(
        private val queue: BlockingQueue<String>,
        private val data: String,
        private val threadSleep: Long,
        private val serialPort: SerialPort
    ) : Runnable {
        override fun run() {
            try {
                queue.put(data)
                var send = ByteArray(0) // 获取 字符串并告诉BluetoothChatService发送
                try {
                    send = hexStr2Bytes(data)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                serialPort.sendData(send)

                Thread.sleep(threadSleep)// 延时300毫秒

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        private fun hexStr2Bytes(paramString: String): ByteArray {
            val i = paramString.length / 2
            val arrayOfByte = ByteArray(i)
            var j = 0
            while (true) {
                if (j >= i) return arrayOfByte
                val k = 1 + j * 2
                val l = k + 1
                arrayOfByte[j] = (0xFF and Integer.decode(
                    "0x" + paramString.substring(j * 2, k)
                            + paramString.substring(k, l)
                )).toByte()
                ++j
            }
        }
    }

}


