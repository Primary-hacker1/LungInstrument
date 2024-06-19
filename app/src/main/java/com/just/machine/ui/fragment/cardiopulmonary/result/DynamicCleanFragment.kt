package com.just.machine.ui.fragment.cardiopulmonary.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.network.LogUtils
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.result.SavedDataAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.databinding.FragmentDynamicCleanBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 运动终止fragment
 *@author zt
 */
@AndroidEntryPoint
class DynamicCleanFragment : CommonBaseFragment<FragmentDynamicCleanBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var adapter: SavedDataAdapter

    private val savedData = mutableListOf<String>()

    override fun loadData() {//懒加载

    }

    override fun initView() {


        adapter = SavedDataAdapter(requireContext(), savedData)

        adapter.setItemOnClickListener(object : SavedDataAdapter.SavedListener {
            override fun onClick(item: String, position: Int) {
                binding.editInput.setText("${binding.editInput.text}\n$item")
                adapter.toggleItemBackground(position)
            }
        })

        binding.llSave.setNoRepeatListener {
            val inputText = binding.editInput.text.toString()
            if (inputText.isNotBlank()) {
                savedData.clear()
                val lines = inputText.split("\n")
                savedData.addAll(lines)
                adapter.notifyDataSetChanged()
//                    binding.editInput.text.clear()
            }
        }

        binding.llReset.setNoRepeatListener {
            adapter.dataClean()
        }

        binding.rvRecord.layoutManager = LinearLayoutManager(requireContext())

        binding.rvRecord.adapter = adapter
    }

    override fun initListener() {

    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDynamicCleanBinding.inflate(inflater, container, false)

}
