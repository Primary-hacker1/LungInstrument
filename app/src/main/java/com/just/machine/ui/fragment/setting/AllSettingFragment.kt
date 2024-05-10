package com.just.machine.ui.fragment.setting

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.pm.PackageInfoCompat
import androidx.fragment.app.viewModels
import com.common.base.*
import com.common.network.LogUtils
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.SpinnerHelper
import com.just.news.R
import com.just.news.databinding.FragmentAllSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class AllSettingFragment : CommonBaseFragment<FragmentAllSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    @SuppressLint("SetTextI18n")
    override fun initView() {

        val packageInfo: PackageInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        val versionName: String = packageInfo.versionName
        val versionCode: Long = PackageInfoCompat.getLongVersionCode(packageInfo)
        binding.tvSystemInfo1.text = "$versionCode 完整版本：v$versionName"
    }

    override fun initListener() {

        val spScenarios =
            SpinnerHelper(requireContext(), binding.spScenarios, R.array.spScenarios_items)
        spScenarios.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {

            }

            override fun onNothingSelected() {

            }
        })

        val spBreathing =
            SpinnerHelper(requireContext(), binding.spBreathing, R.array.spBreathing_items)
        spBreathing.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {

            }

            override fun onNothingSelected() {

            }
        })

        val spEcg = SpinnerHelper(requireContext(), binding.spEcg, R.array.spinner_items)
        spEcg.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {

            }

            override fun onNothingSelected() {

            }
        })


        val fragment = parentFragment

        if (fragment is CardiopulmonarySettingFragment) {
            fragment.setButtonClickListener(object : CardiopulmonarySettingFragment.ButtonClickListener{
                override fun onButtonClick() {
                    LogUtils.d(tag+"onClick")
                }
            })
        }


    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAllSettingBinding.inflate(inflater, container, false)
}