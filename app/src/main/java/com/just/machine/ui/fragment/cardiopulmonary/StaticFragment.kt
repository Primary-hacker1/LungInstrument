package com.just.machine.ui.fragment.cardiopulmonary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.adapter.FragmentPagerAdapter
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.DynamicDataFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.RoutineFragment
import com.just.machine.ui.fragment.cardiopulmonary.dynamic.WassermanFragment
import com.just.machine.ui.fragment.cardiopulmonary.static.BreatheHardInFragment
import com.just.machine.ui.fragment.cardiopulmonary.static.MaxVentilationFragment
import com.just.machine.ui.fragment.cardiopulmonary.static.RoutineLungFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentStaticBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 静态肺测试
 *@author zt
 */
@AndroidEntryPoint
class StaticFragment : CommonBaseFragment<FragmentStaticBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    private fun initToolbar() {

    }

    override fun initView() {
        initToolbar()
        initViewPager()
    }

    private fun initViewPager() {
        
        val fragments = listOf(
            BreatheHardInFragment(), RoutineLungFragment(),
            MaxVentilationFragment()
        )

        val adapter = FragmentPagerAdapter(childFragmentManager, lifecycle, fragments)

        binding.vpTitle.setCurrentItem(0, true)

        binding.vpTitle.adapter = adapter

        binding.llStart.setNoRepeatListener {

        }

        binding.llClean.setNoRepeatListener { //点击取消

        }

    }

    override fun initListener() {

        binding.btnRoutine.setNoRepeatListener {//常规
            binding.vpTitle.currentItem = 0
            setButtonPosition(0)
        }

        binding.btnForced.setNoRepeatListener {//用力
            binding.vpTitle.currentItem = 1
            setButtonPosition(1)
        }

        binding.btnMaxiVentilation.setNoRepeatListener {//最大通气量
            binding.vpTitle.currentItem = 2
            setButtonPosition(2)
        }

        //这个必须写，不然会产生Fata
        binding.vpTitle.isSaveEnabled = false

        binding.vpTitle.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setButtonPosition(position)
            }
        })
    }

    private fun setButtonPosition(position: Int) {
        when (position) {
            0->{
                setButtonStyle(
                    binding.btnRoutine,
                    binding.btnForced, binding.btnMaxiVentilation
                )
            }
            1->{
                setButtonStyle(
                    binding.btnForced,
                    binding.btnRoutine, binding.btnMaxiVentilation
                )
            }
            2->{
                setButtonStyle(
                    binding.btnMaxiVentilation,
                    binding.btnForced, binding.btnRoutine
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
        FragmentStaticBinding.inflate(inflater, container, false)

}
