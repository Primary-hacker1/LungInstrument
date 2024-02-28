package com.just.machine.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.common.base.*
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.PatientBean
import com.just.machine.model.Constants
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentNewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import world.shanya.serialport.SerialPortBuilder

/**
 *create by 2020/6/19
 *@author zt
 */
@AndroidEntryPoint
class NewFragment : CommonBaseFragment<FragmentNewBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    override fun loadData() {//懒加载
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            Navigation.findNavController(it).popBackStack()
        }
    }

    private fun initToolbar() {
        binding.toolbar.title = Constants.news//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
    }

    @SuppressLint("UseRequireInsteadOfGet", "SetTextI18n")
    override fun initView() {
        initToolbar()

        binding.btnMe.setOnClickListener {
            navigate(it, R.id.newFragment)
        }

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.FAIL -> {
                    LogUtils.e(TAG + it.any as PatientBean)
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

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentNewBinding.inflate(inflater, container, false)
}