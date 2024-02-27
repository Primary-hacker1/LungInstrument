package com.just.machine.ui.activity


import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseActivity
import com.just.machine.ui.adapter.PatientAdapter
import com.just.news.databinding.ActivityPatientBinding
import dagger.hilt.android.AndroidEntryPoint
/**
 *create by 2024/2/27
 * 患者信息
 *@author zt
 */
@AndroidEntryPoint
class PatientActivity : CommonBaseActivity<ActivityPatientBinding>(){

    lateinit var adapter: PatientAdapter
    override fun initView() {

        binding.rvList.layoutManager = LinearLayoutManager(this)

        adapter = PatientAdapter(getDataList())

        binding.rvList.adapter = adapter

        // 设置左滑删除功能
        val itemTouchHelper = ItemTouchHelper(PatientAdapter.SwipeToDeleteCallback(this))

        itemTouchHelper.attachToRecyclerView(binding.rvList)
    }

    private fun getDataList(): ArrayList<String> {
        return arrayListOf(
            "Item 1",
            "Item 2",
            "Item 3",
            "Item 4",
            "Item 5"
        )
    }

    override fun getViewBinding() = ActivityPatientBinding.inflate(layoutInflater)
}