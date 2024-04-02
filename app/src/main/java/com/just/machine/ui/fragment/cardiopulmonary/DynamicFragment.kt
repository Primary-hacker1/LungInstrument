package com.just.machine.ui.fragment.cardiopulmonary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.onUI
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.ui.adapter.CustomSpinnerAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentDynamicBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_calibration.btn_ingredient


/**
 *create by 2020/6/19
 * 动态肺测试
 *@author zt
 */
@AndroidEntryPoint
class DynamicFragment : CommonBaseFragment<FragmentDynamicBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    private fun initToolbar() {

    }

    override fun initView() {
        initToolbar()

        val adapterTime = CustomSpinnerAdapter(requireContext())
        val adapterVo = CustomSpinnerAdapter(requireContext())
        val adapterHr = CustomSpinnerAdapter(requireContext())
        val adapterRer = CustomSpinnerAdapter(requireContext())
        val adapterO2Hr = CustomSpinnerAdapter(requireContext())
        val adapterFio2 = CustomSpinnerAdapter(requireContext())
        val adapterVdVt = CustomSpinnerAdapter(requireContext())
        val adapterTex = CustomSpinnerAdapter(requireContext())
        val adapterFeo2 = CustomSpinnerAdapter(requireContext())

        val adapterTph = CustomSpinnerAdapter(requireContext())
        val adapterVCO2 = CustomSpinnerAdapter(requireContext())
        val adapterHrr = CustomSpinnerAdapter(requireContext())
        val adapterVe = CustomSpinnerAdapter(requireContext())
        val adapterSpo2 = CustomSpinnerAdapter(requireContext())
        val adapterFico2 = CustomSpinnerAdapter(requireContext())
        val adapterBf = CustomSpinnerAdapter(requireContext())
        val adapterVtex = CustomSpinnerAdapter(requireContext())
        val adapterFeco2 = CustomSpinnerAdapter(requireContext())

        binding.timeSpinner.adapter = adapterTime
        binding.voSpinner.adapter = adapterVo
        binding.hrSpinner.adapter = adapterHr
        binding.rerSpinner.adapter = adapterRer
        binding.o2hrSpinner.adapter = adapterO2Hr
        binding.fioSpinner.adapter = adapterFio2
        binding.vdSpinner.adapter = adapterVdVt
        binding.texSpinner.adapter = adapterTex
        binding.feoSpinner.adapter = adapterFeo2

        binding.phSpinner.adapter = adapterTph
        binding.vcoSpinner.adapter = adapterVCO2
        binding.hrrSpinner.adapter = adapterHrr
        binding.veSpinner.adapter = adapterVe
        binding.spoSpinner.adapter = adapterSpo2
        binding.ficoSpinner.adapter = adapterFico2
        binding.bfSpinner.adapter = adapterBf
        binding.vtexSpinner.adapter = adapterVtex
        binding.fecoSpinner.adapter = adapterFeco2

        LiveDataBus.get().with("msg").observe(this) {//串口消息
            if (it is ArrayList<*>) {
                val dataList = it as ArrayList<Pair<String, String>>
                // 在这里对数据进行解析和处理
                for (pair in dataList) {
                    val key = pair.first
                    val value = pair.second
                    // 对每个 Pair 中的数据进行相应的操作，例如更新 UI 或执行其他逻辑
                    LogUtils.d(tag + value)
                }
                // 你可以在这里根据需要更新 Adapter 或其他 UI 元素
                adapterTime.updateSpinnerText(dataList)
            }
        }

        var title = 111//模拟串口数据
        var title1 = 222//模拟串口数据

        binding.llStart.setNoRepeatListener {

            title++

            title1++

            LiveDataBus.get().with("msg").value = mutableListOf(
                Pair("111", title.toString()),
                Pair("222", title1.toString()),
                Pair("333", "item")
            )
        }
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicBinding.inflate(inflater, container, false)

}
