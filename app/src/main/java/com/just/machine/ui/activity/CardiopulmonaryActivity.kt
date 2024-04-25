package com.just.machine.ui.activity


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.common.base.CommonBaseActivity
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.ActivityCardiopulmonaryBinding
import com.justsafe.libview.nav.FragmentNavigatorHideShow
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/3/4
 * 心肺测试界面
 *@author zt
 */
@AndroidEntryPoint
class CardiopulmonaryActivity : CommonBaseActivity<ActivityCardiopulmonaryBinding>() {

    private val REQUEST_BLUETOOTH_PERMISSION = 1

    private val viewModel by viewModels<MainViewModel>()

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
        SerialPortManager.initialize(this)
        SerialPortManager.sendMessage(MudbusProtocol.HANDSHAKE_COMMAND)//握手

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
}