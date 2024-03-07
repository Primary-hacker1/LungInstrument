package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.common.base.visible
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.PatientBean
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.machine.model.Constants
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.ui.adapter.CardiopulAdapter
import com.just.machine.ui.adapter.PatientsAdapter
import com.just.machine.ui.adapter.SixMinAdapter
import com.just.machine.ui.dialog.PatientDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.ActivityPatientBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_patient.rv_six_test

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

    private var sixMinAdapter: SixMinAdapter? = null

    private var cardiopulAdapter: CardiopulAdapter? = null

    private var bean: PatientBean? = null

    val beans: ObservableList<PatientBean> = ObservableArrayList()


    private fun initToolbar() {
        binding.toolbar.title = Constants.patientInformation//标题
        binding.toolbar.tvRight.gone()
        binding.toolbar.ivTitleBack.visible()
        binding.toolbar.ivTitleBack.setNoRepeatListener {
            finish()
        }
    }

    override fun initView() {
        initToolbar()

        viewModel.getPatients()//查询数据库

        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvSixTest.layoutManager = LinearLayoutManager(this)
        binding.rvCardiopulmonaryTest.layoutManager = LinearLayoutManager(this)

        initOnClick()

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

                        val sixBeans: ObservableList<SixMinRecordsBean> = ObservableArrayList()

                        bean.sixMinRecordsBean?.let { data -> sixBeans.addAll(data) }

                        sixMinAdapter = SixMinAdapter(sixBeans, R.layout.item_layout_six_test, 10)

                        binding.rvSixTest.adapter = sixMinAdapter


                        val cardiopulmonaryBeans: ObservableList<CardiopulmonaryRecordsBean> =
                            ObservableArrayList()

                        bean.testRecordsBean?.let { data ->
                            {
                                cardiopulmonaryBeans.addAll(data)
                            }
                        }

                        cardiopulAdapter = CardiopulAdapter(
                            cardiopulmonaryBeans,
                            R.layout.item_layout_cardiopul_test,
                            10
                        )

                        binding.rvCardiopulmonaryTest.adapter = adapter
                    }
                }

                in LiveDataEvent.QueryNameId downTo LiveDataEvent.QuerySuccess -> {//查询所有患者

                    if (it.any is List<*>) {

                        val datas = it.any as MutableList<*>


                        for (num in 0 until datas.size) {
                            val bean = datas[num] as PatientBean
                            beans.add(bean)
                        }

                        LogUtils.d(tag + beans.toString())

                        adapter = PatientsAdapter(beans, R.layout.item_layout_patient, 10)

                        binding.rvList.adapter = adapter

                    }
                }
            }
        }
    }

    private fun initOnClick() {
        binding.editSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (beans.size > 0) {
                    beans.clear()
                }

                val select = binding.editSearch.text.toString().trim()

                if (select.isEmpty()) {
                    toast("请输入搜索内容", Toast.LENGTH_LONG)
                    false
                }
                viewModel.getNameOrId(select)//模糊查询数据库
            }
            false
        }

        adapter?.setItemOnClickListener(object : PatientsAdapter.PatientListener {
            //点击item返回点击患者的数据
            override fun onClickItem(bean: PatientBean) {
                viewModel.getPatient(bean.patientId)//查询数据库
            }
        })


        cardiopulAdapter?.setItemOnClickListener(object : CardiopulAdapter.PatientListener {
            //点击item返回心肺测试数据
            override fun onClickItem(bean: CardiopulmonaryRecordsBean) {

            }
        })

        sixMinAdapter?.setItemOnClickListener(object : SixMinAdapter.PatientListener {
            //点击item返回六分钟测试数据
            override fun onClickItem(bean: SixMinRecordsBean) {

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

        binding.btnSixMin.setNoRepeatListener {
            setButtonStyle(
                binding.btnCardiopulmonary,
                binding.btnSixMin
            )
            toggleRecyclerView(binding.rvSixTest, binding.rvCardiopulmonaryTest)
        }

        binding.btnCardiopulmonary.setNoRepeatListener {
            setButtonStyle(
                binding.btnSixMin,
                binding.btnCardiopulmonary
            )

            toggleRecyclerView(
                binding.rvCardiopulmonaryTest,
                binding.rvSixTest
            )
        }

    }

    private fun toggleRecyclerView(showRecyclerView: RecyclerView, hideRecyclerView: RecyclerView) {
        if (showRecyclerView.visibility == View.VISIBLE) {
            showRecyclerView.gone()
            hideRecyclerView.visible()
        } else {
            showRecyclerView.visible()
            hideRecyclerView.gone()
        }
    }

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(this, R.color.cD9D9D9))
        textView2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        textView1.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        textView2.background =
            ContextCompat.getDrawable(this, R.drawable.super_edittext_bg)
    }

    override fun getViewBinding() = ActivityPatientBinding.inflate(layoutInflater)
}