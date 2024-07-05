package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.common.base.CommonBaseActivity
import com.just.machine.dao.PatientBean
import com.just.machine.model.Constants.Companion.patientBean
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.ActivityCardiopulmonaryBinding
import com.justsafe.libview.nav.FragmentNavigatorHideShow
import com.justsafe.libview.util.SystemUtil
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/3/4
 * 心肺测试界面
 *@author zt
 */
@AndroidEntryPoint
class CardiopulmonaryActivity : CommonBaseActivity<ActivityCardiopulmonaryBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    companion object {
        /**
         * @param context context
         * @param bean 患者管理，没有就新建，有就是修改
         */
        fun startCardiopulmonaryActivity(context: Context?,bean: PatientBean) {
            val intent = Intent(context, CardiopulmonaryActivity::class.java)
            // 将患者信息作为额外数据放入Intent
            intent.putExtra(patientBean, bean)
            context?.startActivity(intent)
        }
    }


    private fun initToolbar() {

    }

    override fun initView() {
        val patientBean = intent.getParcelableExtra(patientBean) as? PatientBean

        if (patientBean != null) {//从患者管理跳转进来的
            SharedPreferencesUtils.instance.patientBean = patientBean
        }
        initToolbar()
        initNavigationView()
//        SerialPortManager.initialize(this)
//        SerialPortManager.sendMessage(MudbusProtocol.HANDSHAKE_COMMAND)//握手
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