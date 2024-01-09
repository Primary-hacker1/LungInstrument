package com.just.machine.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.toast
import com.just.machine.model.Constants
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.activity.MainActivity
import com.just.news.databinding.FragmentLoginBinding
import com.just.machine.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_new.view.title

/**
 *create by 2020/6/19
 * 登录界面
 *@author zt
 */
@AndroidEntryPoint
class LoginFragment : CommonBaseFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private fun initToolbar() {
        binding.toolbar.title = Constants.login//标题
        binding.toolbar.tvRight.gone()
//        binding.toolbar.ivTitleBack.visible()
    }

    override fun initView() {
        initToolbar()

        if (!SharedPreferencesUtils.instance.phone.equals("")) {
            MainActivity.startMainActivity(context)
        }

        binding.btnLogin.setOnClickListener {

            if(Constants.isDebug){
                MainActivity.startMainActivity(context)
                activity?.finish()
                return@setOnClickListener
            }

            hideKeyboard(it.windowToken)
            if (binding.atvPhone.text?.isEmpty() == true) {
                toast("手机号不能为空！")
                return@setOnClickListener
            }
            if (binding.atvPass.text?.isEmpty() == true) {
                toast("密码不能为空！")
                return@setOnClickListener
            }

            SharedPreferencesUtils.instance.phone = binding.atvPhone.text.toString()

            MainActivity.startMainActivity(context)

            activity?.finish()
        }

        binding.toolbar.ivTitleBack.setOnClickListener{
            activity?.finish()
            SharedPreferencesUtils.instance.logout()
        }
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