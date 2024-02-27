package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseActivity
import com.just.machine.dao.PatientBean
import com.just.machine.ui.adapter.PatientAdapter
import com.just.news.databinding.ActivityPatientBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/2/27
 * 患者信息
 *@author zt
 */
@AndroidEntryPoint
class PatientActivity : CommonBaseActivity<ActivityPatientBinding>() {

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

        binding.rvList.layoutManager = LinearLayoutManager(this)

        adapter = PatientAdapter(getDataList())

        binding.rvList.adapter = adapter
    }

    private fun getDataList(): ArrayList<PatientBean> {
        val list = ArrayList<PatientBean>()
        list.add(PatientBean("2024", "测试1"))
        list.add(PatientBean("2024", "测试2"))
        list.add(PatientBean("2024", "测试3"))
        list.add(PatientBean("2024", "测试4"))
        list.add(PatientBean("2024", "测试5"))
        return list
    }

    override fun getViewBinding() = ActivityPatientBinding.inflate(layoutInflater)
}