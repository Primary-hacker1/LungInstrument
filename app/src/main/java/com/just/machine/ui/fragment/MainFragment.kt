package com.just.machine.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.TimerEcg
import com.just.news.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
@AndroidEntryPoint
class MainFragment : CommonBaseFragment<FragmentMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    override fun loadData() {//懒加载
        viewModel.getDates("")//插入或者请求网络数据
    }

    companion object {
        val ecg = ArrayList<Float>()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        TimerEcg().startTimer(ecg)
            .ecgTimerListener(object : TimerEcg.EcgTimerListener {
                override fun ecgTimerView(cooY: Float) {
                    binding.ecgView.showLine(cooY)
                }
            })//链式心电图view

    }

    override fun initListener() {
        binding.ecgButton.setNoRepeatListener {
            for (i in 0..9){
                ecg.add(Random.nextInt(10).toFloat())//随机数
            }
        }

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

}