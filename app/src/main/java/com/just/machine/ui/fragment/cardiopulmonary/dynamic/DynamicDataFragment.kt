package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.ui.adapter.lung.DynamicDataAdapter
import com.just.machine.ui.adapter.lung.DynamicDataTitleAdapter
import com.just.machine.ui.fragment.serial.MudbusProtocol
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.BaseUtil
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentDynamicDataBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/4/2
 * 动态肺的数据界面
 *@author zt
 */
@AndroidEntryPoint
class DynamicDataFragment : CommonBaseFragment<FragmentDynamicDataBinding>() {

    private var cpxBreathInOutData: CPXBreathInOutData = CPXBreathInOutData()

    private var mutableListCPX: MutableList<CPXBreathInOutData> = mutableListOf()

    private var titles: MutableList<String> = mutableListOf()

    private var adapterTitle: DynamicDataTitleAdapter? = null

    private var adapterData: DynamicDataAdapter? = null

    private val viewModel by viewModels<MainViewModel>()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        viewModel.getCPXBreathInOutData()

        cpxBreathInOutData.toMutableList().forEach { (key, value) ->
            titles.add(key)
//            LogUtils.d(TAG + key + value)
        }

        adapterTitle = DynamicDataTitleAdapter(requireContext())

        adapterTitle?.setItemsBean(titles)

        val layoutManager = LinearLayoutManager(requireContext())

        layoutManager.orientation = RecyclerView.HORIZONTAL

        binding.rvDynamicTitle.layoutManager = layoutManager

        binding.rvDynamicTitle.adapter = adapterTitle

        adapterData = DynamicDataAdapter(requireContext())

        binding.rvDynamicData.layoutManager = LinearLayoutManager(requireContext())

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.CPXDYNAMICBEAN -> {
                    if (it.any !is List<*>) {
                        return@observe
                    }
                    LogUtils.e(TAG + it.any)
                    mutableListCPX.clear()
                    val listBean = it.any as List<*>
                    for (bean in listBean) {
                        if (bean !is CPXBreathInOutData) {
                            return@observe
                        }
                        mutableListCPX.add(bean)
                    }
                    LogUtils.e(TAG + "$mutableListCPX")
                    adapterData?.setItemsBean(mutableListCPX)
                    binding.rvDynamicData.adapter = adapterData
                }
            }
        }

        viewModel.insertCPXBreathInOutData(
            CPXBreathInOutData(
                createTime = "2024:5:15",
                VTin = 3.3
            )
        )

//        LiveDataBus.get().with("动态心肺测试").observe(this) {//解析串口消息
//            LogUtils.e("$tag======收到了动态肺数据")
//            viewModel.insertCPXBreathInOutData(
//                CPXBreathInOutData(
//                    createTime = "2024:5:15",
//                    VTin = 3.3
//                )
//            )
//        }
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicDataBinding.inflate(inflater, container, false)

}
