package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.PatientBean
import com.just.machine.model.Constants
import com.just.machine.ui.adapter.PatientsAdapter
import com.just.machine.ui.dialog.PatientDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
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

    private var adapter: PatientsAdapter? = null
    private var bean: PatientBean? = null

    private fun initToolbar() {
        binding.toolbar.title = Constants.patientInformation//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
    }

    override fun initView() {
        initToolbar()

        viewModel.getPatients()//查询数据库

        binding.rvList.layoutManager = LinearLayoutManager(this)

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.SUCCESS -> {
                    if (it.any is Int)
                        LogUtils.e(tag + it.any)
                }

                LiveDataEvent.QueryPatient -> {//查询患者单个
                    if (it.any is PatientBean) {
                        val bean = it.any as PatientBean

                        this.bean = bean

                        bean.testRecordsBean
                    }
                }

                LiveDataEvent.QuerySuccess -> {//查询所有患者
                    if (it.any is List<*>) {

                        val datas = it.any as MutableList<*>

                        val beans: ObservableList<PatientBean> = ObservableArrayList()

                        for (num in 0 until datas.size) {
                            val bean = datas[num] as PatientBean
                            beans.add(bean)
                        }

                        LogUtils.e(tag + beans.toString())

                        adapter = PatientsAdapter(beans, R.layout.item_layout_patient, 10)

                        binding.rvList.adapter = adapter

                    }
                }
            }
        }




        adapter?.setItemOnClickListener(object : PatientsAdapter.PatientListener {
            //点击item返回点击患者的数据
            override fun onClickItem(bean: PatientBean) {
                viewModel.getPatient(bean.patientId)//查询数据库
            }
        })

        binding.btnAdd.setNoRepeatListener {
            val patientDialogFragment =
                PatientDialogFragment.startPatientDialogFragment(supportFragmentManager)//添加患者修改患者信息
            patientDialogFragment.setDialogOnClickListener(object :
                PatientDialogFragment.PatientDialogListener {
                override fun onClickConfirmBtn() {//确认
                    patientDialogFragment.dismiss()

                }

                override fun onClickCleanBtn() {//取消
                    patientDialogFragment.dismiss()
                }
            })
        }
    }

    override fun getViewBinding() = ActivityPatientBinding.inflate(layoutInflater)
}