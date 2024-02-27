package com.just.machine.ui.fragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.just.machine.ui.activity.PatientActivity
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

    override fun loadData() {//懒加载

    }

    @SuppressLint("SetTextI18n")
    override fun initView() {

        binding.btnPatient.setNoRepeatListener {
            PatientActivity.startPatientActivity(context)
        }

    }

    override fun initListener() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

}