package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseActivity
import com.just.machine.dao.PatientBean
import com.just.machine.ui.adapter.PatientAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.ActivityPatientBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/2/27
 * 患者信息
 *@author zt
 */
@AndroidEntryPoint
class PatientActivity : CommonBaseActivity<ActivityPatientBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    companion object {
        /**
         * @param context context
         */
        fun startPatientActivity(context: Context?) {
            val intent = Intent(context, PatientActivity::class.java)
            context?.startActivity(intent)
        }
    }

    private lateinit var adapter: PatientAdapter
    override fun initView() {

        viewModel.getDates("")//插入患者数据

        binding.rvList.layoutManager = LinearLayoutManager(this)

        adapter = PatientAdapter(getDataList())

        binding.rvList.adapter = adapter
    }

    private fun getDataList(): ArrayList<PatientBean> {
        val list = ArrayList<PatientBean>()
        list.add(PatientBean("1", "测试1"))
        list.add(PatientBean("2", "测试2"))
        list.add(PatientBean("3", "测试3"))
        list.add(PatientBean("4", "测试4"))
        list.add(PatientBean("5", "测试5"))
        return list
    }

    override fun getViewBinding() = ActivityPatientBinding.inflate(layoutInflater)
}