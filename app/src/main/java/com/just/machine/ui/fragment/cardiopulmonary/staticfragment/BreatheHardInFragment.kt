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

    private var routineLungList: MutableList<RoutineLungBean>? = ArrayList()


    override fun initView() {
        initData()

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

        val bean = SharedPreferencesUtils.instance.patientBean
        LogUtils.e(tag + bean.toString())
        val age = bean?.age?.toDouble()
        val sex = bean?.sex
        val isMale = sex == "男"
        val height = bean?.height?.toDouble()
        val weight = bean?.weight?.toDouble()

        LungFormula.main(age = age, heightCm = height, weightKg = weight, isMale = isMale)
    }

    override fun initListener() {

    }

    private fun initData() {
        routineLungList = mutableListOf(
            RoutineLungBean(
                "用力肺活量(ERV)", "20", "9", "1", "111", "1", "2", "3", "4", "5"
            ), RoutineLungBean(
                "一秒量(ERV)", "20", "9", "1", "111", "1", "2", "3", "4", "5"
            ), RoutineLungBean(
                "一秒率(ERV)", "20", "9", "1", "111", "1", "2", "3", "4", "5"
            ), RoutineLungBean(
                "用力呼气峰流速(ERV)", "20", "9", "1", "111", "1", "2", "3", "4", "5"
            ), RoutineLungBean(
                "25%时呼气流逝(ERV)", "20", "9", "1", "111", "1", "2", "3", "4", "5"
            ), RoutineLungBean(
                "50%时呼气流逝(ERV)", "20", "9", "1", "111", "1", "2", "3", "4", "5"
            )
        )
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentBreatheBinding.inflate(inflater, container, false)
}
