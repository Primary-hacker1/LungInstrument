package com.just.machine.ui.fragment.cardiopulmonary.staticfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent.Companion.STATICSETTINGSSUCCESS
import com.just.machine.dao.setting.StaticSettingBean
import com.just.machine.model.CPETParameter
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.lungdata.LungFormula
import com.just.machine.model.lungdata.RoutineLungBean
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.model.lungdata.DynamicBean
import com.just.news.databinding.FragmentBreatheBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 静态肺常规
 * 前几次正常呼吸，软件提示深呼吸后折线图颜色变成其他颜色，取这段值展示在数据列表
 *@author zt
 */
@AndroidEntryPoint
class BreatheHardInFragment : CommonBaseFragment<FragmentBreatheBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    private var routineLungList: MutableList<RoutineLungBean> = ArrayList()

    private var settingSVC: MutableList<CPETParameter> = ArrayList()


    override fun initView() {

        val layout = binding.fragmentLayout

        layout.setInitView("常规")

        settingSVC = viewModel.dynamicBeans.toMutableList()

        viewModel.getStaticSettings()

        initData()

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                STATICSETTINGSSUCCESS -> {
                    if (it.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = it.any as MutableList<*>

                    if (settings.isEmpty()){
                        return@observe
                    }

                    settingSVC.clear()
                    for (settingBean in settings) {
                        if (settingBean !is StaticSettingBean) {
                            return@observe
                        }
                        LogUtils.e(tag + settingBean.settingSVC)
                        settingSVC = settingBean.settingSVC
                    }
                    initData()
                    LogUtils.e(tag + settingSVC.toString())
                }
            }
        }
    }

    override fun initListener() {

    }

    private fun initData() {
        routineLungList.clear()
        val bean = SharedPreferencesUtils.instance.patientBean
        val age = bean?.age?.toDoubleOrNull()
        val sex = bean?.sex
        val isMale = sex == "男"
        val height = bean?.height?.toDoubleOrNull()
        val weight = bean?.weight?.toDoubleOrNull()

        fun createRoutineLungBean(parameter: String, lungValue: Double?): RoutineLungBean {
            if (parameter.isEmpty()) return RoutineLungBean()
            val dynamicBean = DynamicBean.spinnerItemData(parameter)
            return RoutineLungBean(
                dynamicBean.parameterNameCH + "(${dynamicBean.parameterName})",
                dynamicBean.unit,
                lungValue?.toString() ?: "-",
                "1",//最佳值
                "111",//bp%
                "1",//test1
                "2",
                "3",
                "4",
                "5"
            )
        }


        for (index in settingSVC) {
            if (index.isShow == true) {
                LogUtils.e(tag + LungFormula.main(index.parameterName.toString()))
                routineLungList.add(
                    createRoutineLungBean(
                        index.parameterName.toString(),
                        LungFormula.main(index.parameterName.toString(), age, height, weight, isMale)
                    )
                )
            }
        }


        LogUtils.e(tag + routineLungList.toString())

        binding.fragmentLayout.setLungData(routineLungList)
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentBreatheBinding.inflate(inflater, container, false)
}
