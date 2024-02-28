package com.just.machine.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.just.machine.model.Constants
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.activity.MainActivity
import com.just.news.databinding.FragmentLoginBinding
import com.just.machine.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 登录界面
 *@author zt
 */
@AndroidEntryPoint
class LoginFragment : CommonBaseFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun initView() {

        if (!SharedPreferencesUtils.instance.user.equals("")) {
            MainActivity.startMainActivity(context)
        }

        binding.btnLogin.setNoRepeatListener {

            if (Constants.isDebug) {
                MainActivity.startMainActivity(context)
                activity?.finish()
                return@setNoRepeatListener
            }

            hideKeyboard(it.windowToken)
            if (binding.atvUser.text?.isEmpty() == true) {
                toast("用户名不能为空！")
                return@setNoRepeatListener
            }
            if (binding.atvPass.text?.isEmpty() == true) {
                toast("密码不能为空！")
                return@setNoRepeatListener
            }

            SharedPreferencesUtils.instance.user = binding.atvUser.text.toString()

            SharedPreferencesUtils.instance.pass = binding.atvPass.text.toString()

            MainActivity.startMainActivity(context)

            activity?.finish()
        }

//        binding.toolbar.ivTitleBack.setOnClickListener {
//            activity?.finish()
//            SharedPreferencesUtils.instance.logout()
//        }
    }

    override fun initListener() {

    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentLoginBinding.inflate(inflater, container, false)
}