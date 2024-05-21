package com.just.machine.ui.fragment.cardiopulmonary.staticfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
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

        // Helper function to create RoutineLungBean
        fun createRoutineLungBean(parameter: String, lungValue: Double?): RoutineLungBean {
            val dynamicBean = DynamicBean.spinnerItemData(parameter)
            return RoutineLungBean(
                dynamicBean?.parameterNameCH + "(${dynamicBean?.parameterName})",
                dynamicBean?.unit,
                lungValue?.toString() ?: "-",
                "1",
                "111",
                "1",
                "2",
                "3",
                "4",
                "5"
            )
        }

        // Calculate lung values
        val lungSVC = LungFormula.main("SVC", age, height, weight, isMale)
        val lungERV = LungFormula.main("ERV", age, height, weight, isMale)
        val lungIC = LungFormula.main("IC", age, height, weight, isMale)

        // Create list of RoutineLungBeans
        routineLungList = mutableListOf(
            createRoutineLungBean("SVC", lungSVC),
            createRoutineLungBean("VC_ex", null),
            createRoutineLungBean("ERV", lungERV),
            createRoutineLungBean("VT", null),
            createRoutineLungBean("IC", lungIC)
        )

        // Bind data to layout
        binding.fragmentLayout.setLungData(routineLungList)
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentBreatheBinding.inflate(inflater, container, false)
}
