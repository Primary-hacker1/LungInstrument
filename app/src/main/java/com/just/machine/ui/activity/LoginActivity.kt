package com.just.machine.ui.activity

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.common.base.CommonBaseActivity
import com.just.news.databinding.ActivityLoginBinding
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.justsafe.libview.nav.FragmentNavigatorHideShow
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/6/6
 * 登陆界面
 *@author zt
 */
@AndroidEntryPoint
class LoginActivity : CommonBaseActivity<ActivityLoginBinding>() {//布局ID

    private val viewModel by viewModels<MainViewModel>()//委托

    companion object {
        /**
         * @param context -
         */
        fun startJUSTLoginActivity(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun initView() {
        initNavigationView()
    }

    override fun getViewBinding() = ActivityLoginBinding.inflate(layoutInflater)

    private fun initNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.login_layout) as NavHostFragment //自定义导航器，隐藏显示fragment
        val navControllerNavigation = Navigation.findNavController(this, R.id.login_layout)
        val navigator =
            FragmentNavigatorHideShow(this, navHostFragment.childFragmentManager, R.id.login_layout)
        navControllerNavigation.navigatorProvider.addNavigator(navigator)
        navControllerNavigation.setGraph(R.navigation.nav_login)
    }


}