package com.just.machine.ui.fragment.calibration

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.common.base.CommonBaseFragment
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.setting.AllSettingBean
import com.just.machine.model.Constants
import com.just.machine.model.calibrate.Definition
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.fragment.calibration.onekeycalibration.OneKeyCalibrationFragment
import com.just.machine.ui.fragment.serial.ModbusProtocol
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentCalibrationBinding
import com.justsafe.libview.util.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 *create by 2024/6/19
 * 定标界面
 *@author zt
 */
@AndroidEntryPoint
class CalibrationFragment : CommonBaseFragment<FragmentCalibrationBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private val usbTransferUtil: USBTransferUtil by lazy {
        USBTransferUtil.getInstance()
    }

    override fun initView() {
        binding.toolbar.title = Constants.cardiopulmonary//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()

        binding.toolbarCardi.title = Constants.cardiopulmonary//标题
        binding.toolbarCardi.tvRight.setTime(
            System.currentTimeMillis(),
            DateUtils.nowTimeDataString
        )

        if(ModbusProtocol.isDeviceConnect){
            binding.toolbarCardi.tvDeviceConnectStatus.text = "已连接"
            binding.toolbarCardi.ivDeviceConnectStatus.setImageResource(R.drawable.wifi_on)
        }else{
            binding.toolbarCardi.tvDeviceConnectStatus.text = "未连接"
            binding.toolbarCardi.ivDeviceConnectStatus.setImageResource(R.drawable.warn_yellow)
        }

        if(ModbusProtocol.warmLeaveSec >= 1200){
            binding.toolbarCardi.tvPreheatStatus.text = "已预热"
            binding.toolbarCardi.ivPreheatStatus.setImageResource(R.drawable.preheat)
        }else{
            binding.toolbarCardi.tvPreheatStatus.text = "未预热"
            binding.toolbarCardi.ivPreheatStatus.setImageResource(R.drawable.warn_yellow)
        }

        viewModel.getAllSettingBeans()
        viewModel.getFlowCaliResult()

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.ALL_SETTING_SUCCESS -> {
                    if (it.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = it.any as MutableList<*>

                    for (settingBean in settings) {
                        if (settingBean !is AllSettingBean) {
                            return@observe
                        }
                        binding.toolbarCardi.tvHospitalName.text = settingBean.hospitalName
                    }
                }

                LiveDataEvent.FLOWS_SUCCESS -> {
                    if (it.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = it.any as MutableList<*>

                }
            }
        }

        val adapter = FragmentChildAdapter(this)

        adapter.addFragment(EnvironmentalFragment())
        adapter.addFragment(FlowFragment())
        adapter.addFragment(IngredientFragment())
        adapter.addFragment(OneKeyCalibrationFragment())
        adapter.addFragment(CalibrationResultFragment())


        onButtonClick(binding.btnEnvironment, 0)

        binding.vpCalibration.adapter = adapter

        binding.vpCalibration.isUserInputEnabled = false

        binding.vpCalibration.orientation = ViewPager2.ORIENTATION_VERTICAL // 设置垂直方向滑动
        binding.vpCalibration.offscreenPageLimit = 3

        Definition.Cur_ADC_IN_LDES = 0.22F
        Definition.Cur_ADC_OUT_LDES = 0.22F
        Definition.Cur_ADC_IN_HDIM = 0.22F
        Definition.Cur_ADC_OUT_HDIM = 0.22F

        Definition.Cur_ADCSMALL_IN_LDES = 0.017F
        Definition.Cur_ADCSMALL_OUT_LDES = 0.017F
    }

    private fun onButtonClick(button: AppCompatButton, position: Int) {
        binding.vpCalibration.setCurrentItem(position,true)// 切换ViewPager页面，如果设置成true会出现直接点击一键定标，但是显示的是定标结果Fragment
        resetButtonColors()// 切换按钮颜色
        button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.cF5FCFF))
    }

    private fun resetButtonColors() {
        binding.btnEnvironment.setBackgroundColor(Color.WHITE)
        binding.btnFlow.setBackgroundColor(Color.WHITE)
        binding.btnIngredient.setBackgroundColor(Color.WHITE)
        binding.btnOneKey.setBackgroundColor(Color.WHITE)
        binding.btnResult.setBackgroundColor(Color.WHITE)
        binding.btnCalibrationClose.setBackgroundColor(Color.WHITE)
    }

    override fun initListener() {
//        binding.toolbar.ivTitleBack.setNoRepeatListener {
//            popBackStack()
//        }
        binding.toolbarCardi.ibBack.setNoRepeatListener {
            usbTransferUtil.write(ModbusProtocol.banOneSensor)
            usbTransferUtil.write(ModbusProtocol.banTwoSensor)
            popBackStack()
        }

        binding.btnEnvironment.setNoRepeatListener {
            onButtonClick(binding.btnEnvironment, 0)
        }

        binding.btnFlow.setNoRepeatListener {
            onButtonClick(binding.btnFlow, 1)
        }

        binding.btnIngredient.setNoRepeatListener {
            onButtonClick(binding.btnIngredient, 2)
        }

        binding.btnOneKey.setNoRepeatListener {
            onButtonClick(binding.btnOneKey, 3)
        }

        binding.btnResult.setNoRepeatListener {
            onButtonClick(binding.btnResult, 4)
        }

        binding.btnCalibrationClose.setOnClickListener {
            usbTransferUtil.write(ModbusProtocol.banOneSensor)
            usbTransferUtil.write(ModbusProtocol.banTwoSensor)
            popBackStack()
        }
    }

    private fun popBackStack(){
        val navController = findNavController()//fragment返回数据处理
        navController.previousBackStackEntry?.savedStateHandle?.set("key", "返回")
        navController.popBackStack()
    }

    /**
     * 懒加载
     */
    override fun loadData() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(100)
            binding.toolbarCardi.ivBatteryStatus.setPower(ModbusProtocol.batteryLevel)
        }
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCalibrationBinding.inflate(inflater, container, false)
}