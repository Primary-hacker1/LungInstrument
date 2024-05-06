package com.just.machine.ui.fragment.cardiopulmonary.staticfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.model.staticlung.LungFormula
import com.just.machine.model.staticlung.RoutineLungBean
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.model.staticlung.DynamicBean
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


    override fun initView() {

        val layout = binding.fragmentLayout

        layout.setInitView("常规")

//        viewModel.getPatientsMax()
//
//        viewModel.mEventHub.observe(this) {
//            when (it.action) {
//                MaxPatient -> {//最新的患者
//                    if(it.any is PatientBean){
//                        val bean = it.any as PatientBean
//                        bean.age
//                        bean.sex
//                        bean.height
//                        bean.weight
//                    }
//                    LogUtils.e(tag + it.any)
//                }
//            }
//        }

        initData()

    }

    override fun initListener() {

    }

    private fun initData() {
        val bean = SharedPreferencesUtils.instance.patientBean
        LogUtils.e(tag + bean.toString())
        val age = bean?.age?.toDoubleOrNull()
        val sex = bean?.sex
        val isMale = sex == "男"
        val height = bean?.height?.toDoubleOrNull()
        val weight = bean?.weight?.toDoubleOrNull()

        val beanSVC = DynamicBean.spinnerItemData("SVC")
        val lungSVC = LungFormula.main(
            parameter = "SVC", age = age, heightCm = height, weightKg = weight, isMale = isMale
        )

        val beanVCEX = DynamicBean.spinnerItemData("VC_ex")

        val beanERV = DynamicBean.spinnerItemData("ERV")
        val lungERV = LungFormula.main(
            parameter = "ERV", age = age, heightCm = height, weightKg = weight, isMale = isMale
        )

        val beanVT = DynamicBean.spinnerItemData("VT")

        val beanIC = DynamicBean.spinnerItemData("IC")
        val lungIC = LungFormula.main(
            parameter = "IC", age = age, heightCm = height, weightKg = weight, isMale = isMale
        )

        routineLungList = mutableListOf(
            RoutineLungBean(
                beanSVC?.parameterNameCH + "(" + beanSVC?.parameterName + ")",
                beanSVC?.unit,
                lungSVC.toString(),
                "1",
                "111",
                "1",
                "2",
                "3",
                "4",
                "5"
            ),
            RoutineLungBean(
                beanVCEX?.parameterNameCH + "(" + beanVCEX?.parameterName + ")",
                beanVCEX?.unit,
                "-",
                "1",
                "111",
                "1",
                "2",
                "3",
                "4",
                "5"
            ),
            RoutineLungBean(
                beanERV?.parameterNameCH + "(" + beanERV?.parameterName + ")",
                beanERV?.unit,
                lungERV.toString(),
                "1",
                "111",
                "1",
                "2",
                "3",
                "4",
                "5"
            ),
            RoutineLungBean(
                beanVT?.parameterNameCH + "(" + beanVT?.parameterName + ")",
                beanVT?.unit,
                "-",
                "1",
                "111",
                "1",
                "2",
                "3",
                "4",
                "5"
            ),
            RoutineLungBean(
                beanIC?.parameterNameCH + "(" + beanIC?.parameterName + ")",
                beanIC?.unit,
                lungIC.toString(),
                "1",
                "111",
                "1",
                "2",
                "3",
                "4",
                "5"
            ),
        )

        binding.fragmentLayout.setLungData(routineLungList)
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentBreatheBinding.inflate(inflater, container, false)
}
