package com.just.machine.ui.fragment.calibration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.just.machine.dao.calibration.IngredientBean
import com.just.machine.ui.adapter.calibration.IngredientAdapter
import com.just.machine.ui.viewmodel.MainViewModel
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
                IngredientBean("容积1", "3", "3.003"),
                IngredientBean("容积1", "3", "3.003"),
                IngredientBean("容积1", "3", "3.003"),
            )
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