package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.common.base.CommonBaseActivity
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.ActivityCardiopulmonaryBinding
import com.justsafe.libview.nav.FragmentNavigatorHideShow
import com.justsafe.libview.util.SystemUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *create by 2024/3/4
 * 心肺测试界面
 *@author zt
 */
@AndroidEntryPoint
class CardiopulmonaryActivity : CommonBaseActivity<ActivityCardiopulmonaryBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val usbTransferUtil: USBTransferUtil by lazy {
        USBTransferUtil.getInstance()
    }

    companion object {
        /**
         * @param context context
         */
        fun startCardiopulmonaryActivity(context: Context?) {
            val intent = Intent(context, CardiopulmonaryActivity::class.java)
            context?.startActivity(intent)
        }
    }


    private fun initToolbar() {

    }

    override fun initView() {
        initToolbar()
        initNavigationView()
//        SerialPortManager.initialize(this)
//        SerialPortManager.sendMessage(MudbusProtocol.HANDSHAKE_COMMAND)//握手
//        usbTransferUtil.write(MudbusProtocol.cmdSend("01"))
        lifecycleScope.launch {
            delay(200)
            usbTransferUtil.write(ModbusProtocol.cmdSend("02"))
        }

        //串口数据
        LiveDataBus.get().with("GetVersionInfo").observe(this) {
            if(it is String){
                val hardWareVersion = ModbusProtocol.formatVersion(it.substring(8,12))
                val softWareVersion = ModbusProtocol.formatVersion(it.substring(12,16))
            }
        }

        LiveDataBus.get().with("GetDeviceInfo").observe(this) {
            if(it is String){
                val batteryHex = it.substring(10, 12)
                val battery = batteryHex.toInt(16)
                val heatSecHex = it.substring(12, 16)
                val heatSec = heatSecHex.toInt(16)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SerialPortManager.cleanSerial()
    }


    private fun initNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.cardiopulmonary_layout) as NavHostFragment
        val navControllerNavigation =
            Navigation.findNavController(this, R.id.cardiopulmonary_layout)
        val navigator =
            FragmentNavigatorHideShow(
                this,
                navHostFragment.childFragmentManager,
                R.id.cardiopulmonary_layout
            )
        navControllerNavigation.navigatorProvider.addNavigator(navigator)
        navControllerNavigation.setGraph(R.navigation.nav_cardiopulmonary)
    }

    override fun getViewBinding() = ActivityCardiopulmonaryBinding.inflate(layoutInflater)

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
}