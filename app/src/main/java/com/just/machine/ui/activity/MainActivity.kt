package com.just.machine.ui.activity

import android.content.Context
import android.content.Intent
import android.view.WindowManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.common.base.CommonBaseActivity
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivityMainBinding
import com.justsafe.libview.nav.FragmentNavigatorHideShow
import com.justsafe.libview.util.SystemUtil
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/6/6
 * 主界面（开启串口之类的操作，权限开启）
 *@author zt
 */
@AndroidEntryPoint
class MainActivity : CommonBaseActivity<ActivityMainBinding>() {

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
        initNavigationView()
        requestStoragePermission()

        //生成word所必须的设置
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");
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

    override fun onResume() {
        SystemUtil.immersive(this, true)
        super.onResume()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            SystemUtil.immersive(this, true)
        }
    }

    override fun onDestroy() {
        USBTransferUtil.getInstance().unRegisterUsbReceiver()
        USBTransferUtil.getInstance().disconnect()
        super.onDestroy()
    }
}
