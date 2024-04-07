package com.just.machine.ui.fragment.cardiopulmonary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.ui.adapter.CustomSpinnerAdapter
import com.just.machine.ui.adapter.FragmentPagerAdapter
import com.just.machine.ui.fragment.calibration.EnvironmentalFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.DynamicDataFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.RoutineFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.WassermanFragment
import com.just.machine.ui.fragment.setting.CardiopulmonarySettingFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FragmentDynamicBinding
import dagger.hilt.android.AndroidEntryPoint


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
        initViewPager()

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

    private fun initViewPager() {

        val adapter = FragmentPagerAdapter(requireActivity())

        adapter.addFragment(RoutineFragment())
        adapter.addFragment(WassermanFragment())
        adapter.addFragment(DynamicDataFragment())

        binding.vpTitle.setCurrentItem(0, true)

        binding.vpTitle.adapter = adapter

        binding.llStart.setNoRepeatListener {

        }

        binding.llClean.setNoRepeatListener { //点击取消

        }

    }

    override fun initListener() {

        binding.btnRoutine.setNoRepeatListener {
            binding.vpTitle.currentItem = 0
            setButtonPosition(0)
        }

        binding.btnWasserman.setNoRepeatListener {
            binding.vpTitle.currentItem = 1
            setButtonPosition(1)
        }

        binding.btnData.setNoRepeatListener {
            binding.vpTitle.currentItem = 2
            setButtonPosition(2)
        }

        //这个必须写，不然会产生Fata
        binding.vpTitle.isSaveEnabled = false

        binding.vpTitle.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 当页面被选中时执行你的操作
//                LogUtils.d(tag + "ViewPager2Selected page: $position")

                setButtonPosition(position)

            }
        })
    }

    private fun setButtonPosition(position: Int) {
        when (position) {
            0->{
                setButtonStyle(
                    binding.btnRoutine,
                    binding.btnWasserman, binding.btnData
                )
            }
            1->{
                setButtonStyle(
                    binding.btnWasserman,
                    binding.btnRoutine, binding.btnData
                )
            }
            2->{
                setButtonStyle(
                    binding.btnData,
                    binding.btnWasserman, binding.btnRoutine
                )
            }

        }
    }

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
        textView3: TextView,
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        textView1.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.super_edittext_bg)

        textView2.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        textView3.setTextColor(ContextCompat.getColor(requireContext(), R.color.cD9D9D9))
        textView3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicBinding.inflate(inflater, container, false)

}
