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
import com.common.viewmodel.LiveDataEvent.Companion.ALL_SETTING_SUCCESS
import com.just.machine.dao.setting.AllSettingBean
import com.just.machine.model.Constants.Companion.settingsAreSaved
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.machine.util.SpinnerHelper
import com.just.news.R
import com.just.news.databinding.FragmentAllSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/5/19
 *@author zt
 */
@AndroidEntryPoint
class AllSettingFragment : CommonBaseFragment<FragmentAllSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private var allSettingBean: AllSettingBean = AllSettingBean()

    private val spScenarios by lazy {
        SpinnerHelper(
            requireContext(),
            binding.spScenarios,
            R.array.spScenarios_items
        )
    }

    private val spBreathing: SpinnerHelper by lazy {
        SpinnerHelper(requireContext(), binding.spBreathing, R.array.spBreathing_items)
    }

    private val spEcg: SpinnerHelper by lazy {
        SpinnerHelper(requireContext(), binding.spEcg, R.array.spinner_items)
    }

    override fun loadData() {//懒加载

    }

    @SuppressLint("SetTextI18n")
    override fun initView() {

        viewModel.getAllSettingBeans()

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                ALL_SETTING_SUCCESS -> {
                    if (it.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = it.any as MutableList<*>

                    for (settingBean in settings) {
                        if (settingBean !is AllSettingBean) {
                            return@observe
                        }
                        allSettingBean = settingBean
                    }
                    initData()
                }
            }
        }

        val packageInfo: PackageInfo =
            requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        val versionName: String = packageInfo.versionName
        val versionCode: Long = PackageInfoCompat.getLongVersionCode(packageInfo)
        binding.tvSystemInfo1.text = "$versionCode 完整版本：v$versionName"
    }

    private fun initData() {
        binding.editHospitalName.setText(allSettingBean.hospitalName)
        binding.editLoginPass.setText(allSettingBean.pass)

        val arrayResourceId = R.array.spScenarios_items
        val itemsArray: Array<String> = resources.getStringArray(arrayResourceId)
        for ((index, item) in itemsArray.withIndex()) {
            if (item == allSettingBean.projectedValueScenarios) {
                // 如果找到了特定字符串，则设置 Spinner 的选中项为该位置
                spScenarios.setSelection(index)
                break // 找到后立即退出循环
            }
        }

        val spBreathingItem = R.array.spBreathing_items
        val items: Array<String> = resources.getStringArray(spBreathingItem)
        for ((index, item) in items.withIndex()) {
            if (item == allSettingBean.breathingItem) {
                spBreathing.setSelection(index)
                break
            }
        }

        val spECGItem = R.array.spinner_items
        val itemsECG: Array<String> = resources.getStringArray(spECGItem)
        for ((index, item) in itemsECG.withIndex()) {
            if (item == allSettingBean.ecg) {
                spEcg.setSelection(index)
                break
            }
        }

        binding.editTestDeadSpace.setText(allSettingBean.testDeadSpace)


    }

    override fun initListener() {
        spScenarios.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                allSettingBean.projectedValueScenarios = selectedItem
            }

            override fun onNothingSelected() {

            }
        })


        spBreathing.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                allSettingBean.breathingItem = selectedItem
            }

            override fun onNothingSelected() {

            }
        })

        spEcg.setSpinnerSelectionListener(object : SpinnerHelper.SpinnerSelectionListener {
            override fun onItemSelected(selectedItem: String, view: View?) {
                allSettingBean.ecg = selectedItem
            }

            override fun onNothingSelected() {

            }
        })

        LiveDataBus.get().with(settingsAreSaved).observe(this) {
            LogUtils.d(tag + "onClick")

            if (binding.editUpdatePass.text.toString() != binding.editConfirmPass.text.toString()) {
                toast("登陆两次密码输入不相同！")
                return@observe//两次密码输入不正确
            }

            if (binding.editUpdatePass.text.toString() != binding.editConfirmPass.text.toString()) {
                toast("登陆两次密码输入不相同！")
                return@observe//两次密码输入不正确
            }

            val editPass = binding.editUpdatePass.text.toString()

            SharedPreferencesUtils.instance.pass = editPass

            allSettingBean.pass = editPass

            allSettingBean.hospitalName = binding.editHospitalName.text.toString()

            allSettingBean.testDeadSpace = binding.editTestDeadSpace.text.toString()

            viewModel.setAllSettingBean(allSettingBean)
        }

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAllSettingBinding.inflate(inflater, container, false)
}