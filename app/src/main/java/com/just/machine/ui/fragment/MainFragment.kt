package com.just.machine.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.activity.PatientActivity
import com.just.machine.ui.activity.SixMinActivity
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


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

    override fun initView() {

    }

    override fun initListener() {
        binding.walkTestButton.setNoRepeatListener {
            val intent = Intent(activity, SixMinActivity::class.java)
            startActivity(intent)
        }

        binding.btnPatientInformation.setNoRepeatListener {
            PatientActivity.startPatientActivity(context)
        }

        binding.btnEcg.setNoRepeatListener {//心肺测试

        }
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

}