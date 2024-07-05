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
import com.just.machine.model.lungdata.DynamicBean
import com.just.machine.model.lungdata.LungFormula
import com.just.machine.model.lungdata.RoutineLungBean
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentRoutineBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 静态肺的用力呼吸
 *@author zt
 */
@AndroidEntryPoint
class RoutineLungFragment : CommonBaseFragment<FragmentRoutineBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private var routineLungList: MutableList<RoutineLungBean> = ArrayList()

    private var settingFVC: MutableList<CPETParameter> = ArrayList()

    override fun loadData() {//懒加载

    }

    override fun initView() {
        val layout = binding.fragmentLayout
        layout.setInitView("FVC")

        settingFVC = viewModel.fvcBeans.toMutableList()

        viewModel.getStaticSettings()

        initData()

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                STATICSETTINGSSUCCESS -> {
                    if (it.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = it.any as MutableList<*>

                    if (settings.isEmpty()) {
                        return@observe
                    }

                    settingFVC.clear()

                    for (settingBean in settings) {
                        if (settingBean !is StaticSettingBean) {
                            return@observe
                        }
                        LogUtils.e(tag + settingBean.settingFVC)
                        settingFVC = settingBean.settingFVC
                    }
                    initData()
                }
            }
        }
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


        for (index in settingFVC) {
            if (index.isShow == true) {
                LogUtils.e(tag + LungFormula.main(index.parameterName.toString()))
                routineLungList.add(
                    createRoutineLungBean(
                        index.parameterName.toString(),
                        LungFormula.main(
                            index.parameterName.toString(),
                            age,
                            height,
                            weight,
                            isMale
                        )
                    )
                )
            }
        }

//        LogUtils.e(tag + routineLungList.toString())

        binding.fragmentLayout.setLungData(routineLungList)


    }

    override fun initListener() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRoutineBinding.inflate(inflater, container, false)

}
