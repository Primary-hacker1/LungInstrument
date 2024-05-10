package com.just.machine.ui.fragment.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.viewModels
import com.common.base.*
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent.Companion.STATICSETTINGSSUCCESS
import com.just.machine.dao.setting.DynamicSettingBean
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentDynamicSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 运动肺设置
 *@author zt
 */
@AndroidEntryPoint
class DynamicSettingFragment : CommonBaseFragment<FragmentDynamicSettingBinding>() {

    private var dynamicSettingBean: DynamicSettingBean = DynamicSettingBean()

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        viewModel.getDynamicSettings()

        initData()

        val fragment = parentFragment

        if (fragment is CardiopulmonarySettingFragment) {
            fragment.setButtonClickListener(object :
                CardiopulmonarySettingFragment.ButtonClickListener {
                override fun onButtonClick() {
                    LogUtils.d(tag + "onClick")

                    dynamicSettingBean.isSingularity = binding.cbSingularity.isChecked

                    dynamicSettingBean.isAutomatiFlow = binding.cbAutomaticFlow.isChecked

                    dynamicSettingBean.isExtremum = binding.cbExtremum.isChecked

                    dynamicSettingBean.isRpe = binding.cbRpe.isChecked

                    dynamicSettingBean.isOxygen = binding.cbOxygen.isChecked

                    dynamicSettingBean.isDynamicTrafficAnalysis =
                        binding.cbDynamicTrafficAnalysis.isChecked

                    dynamicSettingBean.isExercisePrescriptionOptions =
                        binding.cbExercisePrescriptionOptions.isChecked

                    dynamicSettingBean.respiratoryOrderMean =
                        binding.editRespiratoryOrderMean.text.toString()

                    dynamicSettingBean.nineDiagramEstimates =
                        binding.editNineDiagramEstimates.text.toString()

                    dynamicSettingBean.lungWidth = binding.editLungWidth.text.toString()

                    viewModel.setDynamicSettingBean(dynamicSettingBean)
                }
            })
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                STATICSETTINGSSUCCESS -> {
                    if (it.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = it.any as MutableList<*>

                    for (settingBean in settings) {
                        if (settingBean !is DynamicSettingBean) {
                            return@observe
                        }
                        dynamicSettingBean = settingBean
                    }
                    initData()
                }
            }
        }
    }

    private fun initData() {
        binding.cbSingularity.isChecked = dynamicSettingBean.isSingularity == true
        binding.cbAutomaticFlow.isChecked = dynamicSettingBean.isAutomatiFlow == true
        binding.cbExtremum.isChecked = dynamicSettingBean.isExtremum == true
        binding.cbRpe.isChecked = dynamicSettingBean.isRpe == true
        binding.cbOxygen.isChecked = dynamicSettingBean.isOxygen == true
        binding.cbDynamicTrafficAnalysis.isChecked =
            dynamicSettingBean.isDynamicTrafficAnalysis == true
        binding.cbExercisePrescriptionOptions.isChecked =
            dynamicSettingBean.isExercisePrescriptionOptions == true

        setCheckBoxListener(binding.cbSingularity) { isChecked ->
            dynamicSettingBean.isSingularity = isChecked
        }

        setCheckBoxListener(binding.cbAutomaticFlow) { isChecked ->
            dynamicSettingBean.isAutomatiFlow = isChecked
        }

        setCheckBoxListener(binding.cbExtremum) { isChecked ->
            dynamicSettingBean.isExtremum = isChecked
        }

        setCheckBoxListener(binding.cbRpe) { isChecked ->
            dynamicSettingBean.isRpe = isChecked
        }

        setCheckBoxListener(binding.cbOxygen) { isChecked ->
            dynamicSettingBean.isOxygen = isChecked
        }

        setCheckBoxListener(binding.cbDynamicTrafficAnalysis) { isChecked ->
            dynamicSettingBean.isDynamicTrafficAnalysis = isChecked
        }

        setCheckBoxListener(binding.cbExercisePrescriptionOptions) { isChecked ->
            dynamicSettingBean.isExercisePrescriptionOptions = isChecked
        }

        binding.editRespiratoryOrderMean.setText(dynamicSettingBean.respiratoryOrderMean)

        binding.editNineDiagramEstimates.setText(dynamicSettingBean.nineDiagramEstimates)

        binding.editLungWidth.setText(dynamicSettingBean.lungWidth)
    }

    private fun setCheckBoxListener(checkBoxId: CheckBox, listener: (Boolean) -> Unit) {
        checkBoxId.setOnCheckedChangeListener { _, isChecked -> listener(isChecked) }
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicSettingBinding.inflate(inflater, container, false)
}