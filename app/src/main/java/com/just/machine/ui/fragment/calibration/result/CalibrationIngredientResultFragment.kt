package com.just.machine.ui.fragment.calibration.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.calibration.IngredientCalibrationResultBean
import com.just.machine.ui.adapter.calibration.ResultIngredientAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentCalibrationIngredientResultBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 成分定标结果
 */
@AndroidEntryPoint
class CalibrationIngredientResultFragment :
    CommonBaseFragment<FragmentCalibrationIngredientResultBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private val adapterIngredient by lazy { ResultIngredientAdapter(requireContext()) }
    override fun loadData() {

    }

    override fun initView() {
        binding.rvResultIngredient.layoutManager = LinearLayoutManager(requireContext())
        binding.rvResultIngredient.adapter = adapterIngredient
        viewModel.getIngredientCaliResult()
    }

    override fun initListener() {
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.INGREDIENTS_SUCCESS -> {
                    val ingredientBean: MutableList<IngredientCalibrationResultBean> = ArrayList()
                    if (it.any is List<*>) {
                        val list = it.any as List<*>
                        for (index in list) {
                            if (index is IngredientCalibrationResultBean) {
                                ingredientBean.add(index)
                            }
                        }
                    }
                    adapterIngredient.setItemsBean(ingredientBean)
                }
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCalibrationIngredientResultBinding.inflate(inflater, container, false)

}