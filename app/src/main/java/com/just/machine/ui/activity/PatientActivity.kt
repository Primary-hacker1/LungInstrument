package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.onUI
import com.common.base.setNoRepeatListener
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
import kotlinx.coroutines.flow.collect


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

    private var sixMinAdapter: SixMinAdapter = SixMinAdapter()

    private var cardiopulmonaryAdapter: CardiopulAdapter = CardiopulAdapter()

    private var bean: PatientBean? = null

    val beans: MutableList<PatientBean> = ArrayList()

    private val adapter: PatientsAdapter = PatientsAdapter()


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

                        LogUtils.d(tag + bean.toString())

                        sixMinAdapter = SixMinAdapter()

                        bean.sixMinRecordsBean?.let { it1 -> sixMinAdapter.setItemsBean(it1) }

                        binding.rvSixTest.adapter = sixMinAdapter


                        cardiopulmonaryAdapter = CardiopulAdapter()

                        bean.testRecordsBean?.let { it1 -> cardiopulmonaryAdapter.setItemsBean(it1) }

                        binding.rvCardiopulmonaryTest.adapter = cardiopulmonaryAdapter
                    }
                }

                in LiveDataEvent.QueryNameId downTo LiveDataEvent.QuerySuccess -> {//查询所有患者

                    if (it.any is List<*>) {
                        beans.clear()

                        val datas = it.any as MutableList<*>

                        for (num in 0 until datas.size) {
                            val bean = datas[num] as PatientBean
                            beans.add(bean)
                        }

                        adapter.setItemsBean(beans)

                        LogUtils.d(tag + beans.toString())

                        binding.rvList.adapter = adapter

                    }
                }
            }
        }

        initOnClick()

    }

    private fun initOnClick() {

        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (beans.size > 0) {
                    beans.clear()
                }

                val select = binding.editSearch.text.toString().trim()

                if (select.isEmpty()) {
                    viewModel.getPatients()//查询数据库
                    return
                }
                viewModel.getNameOrId(select)//模糊查询数据库
            }

            override fun afterTextChanged(s: Editable) {}
        })

        adapter.setItemOnClickListener(object : PatientsAdapter.PatientListener {
            override fun onDeleteItem(bean: PatientBean) {
                viewModel.deletePatient(bean.patientId)
                viewModel.getPatients()//查询数据库
            }

            override fun onUpdateItem(bean: PatientBean) {
                PatientDialogFragment.startPatientDialogFragment(supportFragmentManager, bean)//修改患者信息
            }
        })

        adapter.setItemClickListener {
            viewModel.getPatient(it.patientId)//查询数据库
        }


        cardiopulmonaryAdapter.setItemOnClickListener(object : CardiopulAdapter.PatientListener {
            override fun onClickItem(bean: CardiopulmonaryRecordsBean) {//点击item返回心肺测试数据

            }
        })

        sixMinAdapter.setItemOnClickListener(object : SixMinAdapter.PatientListener {
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

                }

                override fun onClickCleanBtn() {//取消

                }
            })
        }

        binding.btnSixMin.setNoRepeatListener {
            setButtonStyle(
                binding.btnCardiopulmonary,
                binding.btnSixMin,
                binding.rvCardiopulmonaryTest,
                binding.rvSixTest
            )
            binding.llSixMin.visible()
            binding.llCardiopulmonary.gone()
        }

        binding.btnCardiopulmonary.setNoRepeatListener {
            setButtonStyle(
                binding.btnSixMin,
                binding.btnCardiopulmonary,
                binding.rvSixTest,
                binding.rvCardiopulmonaryTest
            )
            binding.llSixMin.gone()
            binding.llCardiopulmonary.visible()
        }

    }

    private fun setButtonStyle(
        textView1: TextView,
        textView2: TextView,
        showRecyclerView: RecyclerView,
        hideRecyclerView: RecyclerView
    ) {// 设置按钮的样式

        textView1.setTextColor(ContextCompat.getColor(this, R.color.cD9D9D9))
        textView2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        textView1.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        textView2.background =
            ContextCompat.getDrawable(this, R.drawable.super_edittext_bg)
        showRecyclerView.gone()
        hideRecyclerView.visible()
    }

    override fun getViewBinding() = ActivityPatientBinding.inflate(layoutInflater)
}