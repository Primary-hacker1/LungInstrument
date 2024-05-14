package com.just.machine.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    private val REQUEST_BLUETOOTH_PERMISSION = 1

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            val allGranted = permissions.all { it.value }
            if (allGranted){//

            }
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {

                } else {
                    toast("${permissionName}被拒绝了，请在应用设置里打开权限")
                }
            }
        }

    override fun initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                    REQUEST_BLUETOOTH_PERMISSION
                )
            }
        }

        activityResultLauncher.launch(
            arrayListOf(Manifest.permission.BLUETOOTH).apply {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    // 在 Android R 之前的版本中，需要请求 BLUETOOTH_ADMIN 和 BLUETOOTH_CONNECT 权限
                    add(Manifest.permission.BLUETOOTH_ADMIN)
                    add(Manifest.permission.BLUETOOTH_CONNECT)
                }
            }.toTypedArray()
        )

        if (!SharedPreferencesUtils.instance.user.equals("")) {
            MainActivity.startMainActivity(context)
            activity?.finish()
        }

        if (SharedPreferencesUtils.instance.user.equals("")) {
            SharedPreferencesUtils.instance.user = "admin"
        }

        if (SharedPreferencesUtils.instance.pass.equals("")) {
            SharedPreferencesUtils.instance.pass = "123456"
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

            if (binding.atvUser.text.toString() !=  SharedPreferencesUtils.instance.user) {
                toast("没有该用户！请检查用户名")
                return@setNoRepeatListener
            }

            if (binding.atvPass.text.toString() !=  SharedPreferencesUtils.instance.pass) {
                toast("请输入正确的密码！")
                return@setNoRepeatListener
            }

            SharedPreferencesUtils.instance.user = binding.atvUser.text.toString()

            SharedPreferencesUtils.instance.pass = binding.atvPass.text.toString()

            MainActivity.startMainActivity(context)

            activity?.finish()
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