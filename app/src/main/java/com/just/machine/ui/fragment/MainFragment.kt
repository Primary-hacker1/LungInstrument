package com.just.machine.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.news.R
import com.just.machine.dao.Plant
import com.just.news.databinding.FragmentMainBinding
import com.just.machine.model.Constants
import com.just.machine.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_new.view.title
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import world.shanya.serialport.SerialPortBuilder


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

    @SuppressLint("SetTextI18n")
    override fun initView() {

    }

    override fun initListener() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

}