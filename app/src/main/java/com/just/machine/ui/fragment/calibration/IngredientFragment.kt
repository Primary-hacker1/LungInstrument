package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.calibration.IngredientAdapter
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.fragment.serial.SerialPortManager
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.BaseUtil
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentIngredientBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2020/6/19
 * 成分定标
 *@author zt
 */
@AndroidEntryPoint
class IngredientFragment : CommonBaseFragment<FragmentIngredientBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val ingredientAdapter by lazy {
        IngredientAdapter(requireContext())
    }

    override fun initView() {

        binding.rvIngredient.layoutManager = LinearLayoutManager(requireContext())

        ingredientAdapter.setItemClickListener { item, position ->
            ingredientAdapter.toggleItemBackground(position)
        }

        ingredientAdapter.setItemsBean(
            mutableListOf
                (
                IngredientBean(0,1,"容积1", "3", "3.003"),
                IngredientBean(0,1,"容积1", "3", "3.003"),
                IngredientBean(0,1,"容积1", "3", "3.003"),
            )
        )

        binding.chartView.setLineDataSetData(binding.chartView.flowDataSetList())

        binding.chartView.setLineChartFlow(
            yAxisMinimum = 0f,
            yAxisMaximum = 30f,
            countMaxX = 60f,
            granularityY = 1.5f,
            granularityX = 1f,
            titleCentent = "成分定标"
        )

        binding.rvIngredient.adapter = ingredientAdapter

        binding.materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // SwitchMaterial 打开
            } else {
                // SwitchMaterial 关闭
            }
        }

        binding.materialSwitch2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // SwitchMaterial 打开
            } else {
                // SwitchMaterial 关闭
            }
        }

        binding.llStart.setNoRepeatListener {
            if (Constants.isDebug) {
                // 调用生成主控板返回数据方法并打印生成的数据
                val controlBoardResponse = MudbusProtocol.ControlBoardData(
                    0x12.toByte(), // 返回命令
                    1000, // 大量程流量传感器数据
                    500, // 小量程流量传感器数据
                    800, // CO2传感器数据
                    200, // O2传感器数据
                    1500, // 分析气体流速传感器数据
                    1000, // 分析气体压力传感器数据
                    300, // 温度数据
                    80 // 电量数据
                )

                val data = MudbusProtocol.generateControlBoardResponse(
                    controlBoardResponse
                )

                LogUtils.e(tag + BaseUtil.bytes2HexStr(data) + "发送的数据")

                LiveDataBus.get().with("测试3").value = data

                return@setNoRepeatListener
            }

            SerialPortManager.sendMessage(MudbusProtocol.FLOW_CALIBRATION_COMMAND)//发送流量定标
        }

        LiveDataBus.get().with("测试3").observe(this) {//解析串口消息
            if (it is ByteArray) {
                LogUtils.e(tag + BaseUtil.bytes2HexStr(it) + "字节长度" + BaseUtil.bytes2HexStr(it).length)
                val data = MudbusProtocol.parseControlBoardResponse(it)
                if (data != null) {
                    val bean = data
                    LogUtils.e(tag+data.toString())
                }
            }
        }
    }

    override fun initListener() {

    }

    /**
     * 懒加载
     */
    override fun loadData() {

    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentIngredientBinding.inflate(inflater, container, false)
}