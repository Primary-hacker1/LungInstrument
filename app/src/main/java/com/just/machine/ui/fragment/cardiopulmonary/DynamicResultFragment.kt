package com.just.machine.ui.fragment.cardiopulmonary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.common.viewmodel.LiveDataEvent.Companion.DYNAMICSUCCESS
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.dao.setting.DynamicSettingBean
import com.just.machine.model.lungdata.AnlyCpxTableModel
import com.just.machine.model.lungdata.CPXSerializeData
import com.just.machine.model.result.DynamicResultButtonBean
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.adapter.result.ResultBtnAdapter
import com.just.machine.ui.fragment.cardiopulmonary.result.CompensatoryPointFragment
import com.just.machine.ui.fragment.cardiopulmonary.result.DynamicCleanFragment
import com.just.machine.ui.fragment.cardiopulmonary.result.ExtremumAnalysisFragment
import com.just.machine.ui.fragment.cardiopulmonary.result.FlowRateLoopsFragment
import com.just.machine.ui.fragment.cardiopulmonary.result.OxygenDomainFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentDynamicResultBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2024/6/19
 * 运动评估
 *@author zt
 */
@AndroidEntryPoint
class DynamicResultFragment : CommonBaseFragment<FragmentDynamicResultBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val resultBtnAdapter by lazy { ResultBtnAdapter(requireContext()) }

    private var bean: MutableList<DynamicResultButtonBean> = ArrayList()

    private var mutableListCPX: MutableList<AnlyCpxTableModel> = mutableListOf()

    override fun loadData() {//懒加载

    }

    override fun initView() {

        val adapter = FragmentChildAdapter(this)

        adapter.addFragment(DynamicCleanFragment())
        adapter.addFragment(ExtremumAnalysisFragment())
        adapter.addFragment(OxygenDomainFragment())
        adapter.addFragment(CompensatoryPointFragment())
//        adapter.addFragment(SlopeFragment())
        adapter.addFragment(FlowRateLoopsFragment())

        binding.viewpager.setCurrentItem(1, true)

        binding.viewpager.adapter = adapter

        binding.viewpager.isUserInputEnabled = false


        // 设置 RecyclerView 的布局和适配器
        binding.rvButton.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvButton.adapter = resultBtnAdapter

        // 初始化按钮列表
        updateButtonList("终止原因")

        // 设置按钮点击监听器
        resultBtnAdapter.setItemClickListener { item, _ ->
            updateButtonList(item.resultBtnName)
        }

        viewModel.getDynamicSettings()

        viewModel.getCPXBreathInOutData()

        viewModel.mEventHub.observe(this) { event ->
            when (event.action) {
                DYNAMICSUCCESS -> {
                    if (event.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = event.any as MutableList<*>

                    for (settingBean in settings) {
                        if (settingBean !is DynamicSettingBean) {
                            return@observe
                        }

                        LogUtils.e(tag + settingBean)

                        // 根据条件决定是否添加或移除对应的片段
                        for (index in bean) {
                            if (index.resultBtnName.toString() == "运动极值分析") {
                                index.isVisible = settingBean.isExtremum
                            }

                            if (index.resultBtnName.toString() == "无氧域分析") {
                                index.isVisible = settingBean.isOxygen
                            }

                            if (index.resultBtnName.toString() == "呼吸代偿点分析") {
                                index.isVisible = settingBean.isRpe
                            }

                            if (index.resultBtnName.toString() == "动态流速环分析") {
                                index.isVisible = settingBean.isDynamicTrafficAnalysis
                            }
                        }

                        resultBtnAdapter.setItemsBean(bean)
                    }
                }

                LiveDataEvent.CPXDYNAMICBEAN -> {
                    if (event.any !is List<*>) {
                        return@observe
                    }
                    mutableListCPX.clear()
                    val listBean = event.any as List<*>
                    for (bean in listBean) {
                        if (bean !is CPXBreathInOutData) {
                            return@observe
                        }
                        val cpxBean = CPXSerializeData().createAnlyCpxTableModels(bean)
                        mutableListCPX.add(cpxBean)
                    }
                }
            }
        }
    }

    private fun createButtonList(activeButtonName: String? = null): MutableList<DynamicResultButtonBean> {
        val buttonData = listOf(
            Pair("终止原因", R.drawable.ic_clean_result),
            Pair("运动极值分析", R.drawable.ic_extremum),
            Pair("无氧域分析", R.drawable.ic_oxygen),
            Pair("呼吸代偿点分析", R.drawable.ic_compensatory),
//            Pair("斜率分析", R.drawable.ic_slope),
            Pair("动态流速环分析", R.drawable.ic_rate_loops)
        )

        val buttons = buttonData.map { (name, drawableResId) ->
            val drawable = ContextCompat.getDrawable(requireContext(), drawableResId)
            var isVis = true
            for (index in bean) {
                if (index.resultBtnName.toString() == name) {
                    isVis = index.isVisible == true
                }
            }

            DynamicResultButtonBean(drawable, name, name == activeButtonName, isVis)
        }.toMutableList()

        activeButtonName?.let { name ->
            val index = buttonData.indexOfFirst { it.first == name }
            if (index != -1) {
                binding.viewpager.setCurrentItem(index, true)
            }
        }

        return buttons
    }


    // 更新按钮列表的函数
    private fun updateButtonList(activeButtonName: String?) {
        bean = createButtonList(activeButtonName)
        resultBtnAdapter.setItemsBean(bean)
    }

    override fun initListener() {


    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicResultBinding.inflate(inflater, container, false)

}
