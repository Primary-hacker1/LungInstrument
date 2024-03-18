package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.just.machine.model.Constants
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.ActivityCardiopulmonaryBinding
import com.just.news.databinding.ActivityCardiopulmonarySettingBinding
import com.just.news.databinding.ActivityPatientBinding
import com.justsafe.libview.nav.FragmentNavigatorHideShow
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
    }

    private fun initNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.cardiopulmonary_layout) as NavHostFragment
        val navControllerNavigation = Navigation.findNavController(this, R.id.cardiopulmonary_layout)
        val navigator =
            FragmentNavigatorHideShow(this, navHostFragment.childFragmentManager, R.id.cardiopulmonary_layout)
        navControllerNavigation.navigatorProvider.addNavigator(navigator)
        navControllerNavigation.setGraph(R.navigation.nav_cardiopulmonary)
    }

    override fun getViewBinding() = ActivityCardiopulmonaryBinding.inflate(layoutInflater)
}