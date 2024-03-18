package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.common.base.visible
import com.just.machine.model.Constants
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.activity.MainActivity
import com.just.news.databinding.FragmentLoginBinding
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentCalibrationBinding
import com.just.news.databinding.FragmentCalibrationResultBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 定标界面
 *@author zt
 */
@AndroidEntryPoint
class CalibrationResultFragment : CommonBaseFragment<FragmentCalibrationResultBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    override fun initView() {

    }

    override fun initListener() {

    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalibrationResultBinding.inflate(inflater, container, false)
}