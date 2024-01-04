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
import kotlinx.android.synthetic.main.view_toolbar.view.iv_title_back
import kotlinx.android.synthetic.main.view_toolbar.view.tv_right
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

    private fun initToolbar() {
        binding.toolbar.title = Constants.succeedName//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.gone()
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        initToolbar()

        binding.btnMe.setOnClickListener {
            navigate(it, R.id.newFragment)
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.LOGIN_FAIL -> {
                    LogUtils.e(TAG + it.any as Plant)
                }
            }
        }

        val stringBuilder = StringBuilder()

        val serialPort =
            SerialPortBuilder
                .setReceivedDataCallback {
                    MainScope().launch {
                        stringBuilder.append(it)
                        binding.textViewReceiced.text = stringBuilder.toString()
                    }
                }
                .isDebug(Constants.isDebug)
                .setConnectionStatusCallback { status, bluetoothDevice ->
                    MainScope().launch {
                        if (status) {
                            if (ActivityCompat.checkSelfPermission(
                                    activity!!,
                                    Manifest.permission.BLUETOOTH_CONNECT
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return@launch
                            }

                            binding.textViewConnectInfo.text =
                                "设备名称:\t${bluetoothDevice?.name}\n" +
                                        "设备地址:\t${bluetoothDevice?.address}\n" +
                                        "设备类型:\t${bluetoothDevice?.type}"

                        }else {
                            binding.textViewConnectInfo.text = ""
                        }
                    }
                }
                .build(activity!!)

        binding.buttonConnect.setOnClickListener {
            serialPort.openDiscoveryActivity()
        }

        binding.buttonDisconnect.setOnClickListener {
            serialPort.disconnect()
        }

        binding.buttonSend.setOnClickListener {
            serialPort.sendData(binding.editTextTextSend.text.toString())
        }
    }




    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

}