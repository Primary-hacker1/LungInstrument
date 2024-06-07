package com.just.machine.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.common.base.CommonBaseActivity
import com.just.machine.util.ConnectThread
import com.just.machine.util.ListenerThread
import com.just.news.R
import com.just.news.databinding.ActivityMainBinding
import com.justsafe.libview.nav.FragmentNavigatorHideShow
import dagger.hilt.android.AndroidEntryPoint


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
        // 设置 FLAG_KEEP_SCREEN_ON 标志以防止屏幕熄灭
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initSocket()
        initNavigationView()
        requestStoragePermission()

        //生成word所必须的设置
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
    }

    private fun initSocket() {
        listenerThread = ListenerThread(12345, handler)
        listenerThread.start()
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
