package com.just.machine.ui.fragment.cardiopulmonary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.just.machine.model.DynamicResultButtonBean
import com.just.machine.ui.adapter.FragmentChildAdapter
import com.just.machine.ui.adapter.ResultBtnAdapter
import com.just.machine.ui.fragment.cardiopulmonary.result.CompensatoryPointFragment
import com.just.machine.ui.fragment.cardiopulmonary.result.DynamicCleanFragment
import com.just.machine.ui.fragment.cardiopulmonary.result.ExtremumAnalysisFragment
import com.just.machine.ui.fragment.cardiopulmonary.result.FlowRateLoopsFragment
import com.just.machine.ui.fragment.cardiopulmonary.result.OxygenDomainFragment
import com.just.machine.ui.fragment.cardiopulmonary.result.SlopeFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentDynamicResultBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 运动评估
 *@author zt
 */
@AndroidEntryPoint
class DynamicResultFragment : CommonBaseFragment<FragmentDynamicResultBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val resultBtnAdapter by lazy { ResultBtnAdapter(requireContext()) }
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
            DynamicResultButtonBean(drawable, name, name == activeButtonName)
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
        val buttons = createButtonList(activeButtonName)
        resultBtnAdapter.setItemsBean(buttons)
    }

    override fun initListener() {

    }

    fun onSaveCLick(): LinearLayout {
        return binding.llSave
    }

    fun onResetCLick(): LinearLayout {
        return binding.llReset
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicResultBinding.inflate(inflater, container, false)

}
