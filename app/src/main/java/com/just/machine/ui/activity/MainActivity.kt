package com.just.machine.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Message
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.common.base.CommonBaseActivity
import com.common.network.LogUtils
import com.just.machine.util.ConnectThread
import com.just.machine.util.ListenerThread
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivityMainBinding
import com.justsafe.libview.nav.FragmentNavigatorHideShow
import com.justsafe.libview.util.MySystemParams
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.Socket
import java.net.SocketException


@AndroidEntryPoint
class MainActivity : CommonBaseActivity<ActivityMainBinding>() {

    private lateinit var listenerThread: ListenerThread
    private lateinit var connectThread: ConnectThread

    private val handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {

        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    connectThread = ConnectThread(
                        this@MainActivity, listenerThread.socket,
                        this
                    )
                    connectThread.start()
                }

                2 -> Toast.makeText(
                    this@MainActivity,
                    "设备连接成功",
                    Toast.LENGTH_SHORT
                ).show()

                3 -> Toast.makeText(
                    this@MainActivity,
                    "发送消息成功:" + msg.data.getString("MSG"),
                    Toast.LENGTH_SHORT
                ).show()

                4 -> Toast.makeText(
                    this@MainActivity,
                    "发送消息失败:" + msg.data.getString("MSG"),
                    Toast.LENGTH_SHORT
                ).show()

                6 -> Toast.makeText(
                    this@MainActivity,
                    "收到消息:" + msg.data.getString(
                        "MSG"
                    ),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        /**
         * @param context -跳转主界面
         */
        fun startMainActivity(context: Context?) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context?.startActivity(intent)
        }
    }

    override fun initView() {
        LogUtils.d(MySystemParams.getInstance(this).toString())
        initSocket()
        initNavigationView()
    }

    private fun initSocket() {
        listenerThread = ListenerThread(12345, handler)
        listenerThread.start()

//        Thread {
//            try {
//                val wifiManager =
//                    this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//                val dhcpInfo = wifiManager.dhcpInfo
//                //本地路由开启通信
//                var routeIp = Formatter.formatIpAddress(dhcpInfo.gateway)
//                val socket = Socket(routeIp, 12345)
//                connectThread = ConnectThread(this@MainActivity, socket, handler)
//                connectThread.start()
//            } catch (e: IOException) {
//                e.printStackTrace()
//                runOnUiThread {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "创建通信失败",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }.start()
    }

    private fun initNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_layout) as NavHostFragment
        val navControllerNavigation = Navigation.findNavController(this, R.id.main_layout)
        val navigator =
            FragmentNavigatorHideShow(this, navHostFragment.childFragmentManager, R.id.main_layout)
        navControllerNavigation.navigatorProvider.addNavigator(navigator)
        navControllerNavigation.setGraph(R.navigation.nav_main)
    }


    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
}
