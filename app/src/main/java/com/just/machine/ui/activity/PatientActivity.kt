package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseActivity
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
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

    private var adapter: PatientAdapter? = null

    override fun initView() {

        viewModel.getPatient()//查询数据库

        binding.rvList.layoutManager = LinearLayoutManager(this)

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.SUCCESS -> {
                    if (it.any is Int)
                        LogUtils.e(tag + it.any)
                }

                LiveDataEvent.QuerySuccess -> {
                    if (it.any is List<*>) {

                        val datas = it.any as MutableList<*>

                        val beans: MutableList<PatientBean> = ArrayList()

                        for (num in 0 until datas.size) {
                            val bean = datas[num] as PatientBean
                            beans.add(bean)
                        }

                        LogUtils.e(tag + beans.toString())

                        adapter = PatientAdapter(beans)

                        binding.rvList.adapter = adapter

                    }
                }
            }
        }

        adapter?.setItemOnClickListener(object : PatientAdapter.PatientListener {
            //点击item返回点击患者的数据
            override fun onClickItem(bean: PatientBean) {


            }
        })
    }

    override fun getViewBinding() = ActivityPatientBinding.inflate(layoutInflater)
}