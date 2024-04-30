package com.just.machine.ui.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.common.base.CommonBaseActivity
import com.common.base.gone
import com.common.base.setNoRepeatListener
import com.common.base.visible
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent
import com.just.machine.dao.PatientBean
import com.just.machine.model.Constants
import com.just.machine.model.PatientInfoBean
import com.just.machine.model.sixminreport.SixMinReportInfo
import com.just.machine.ui.adapter.CardiopulAdapter
import com.just.machine.ui.adapter.PatientsAdapter
import com.just.machine.ui.adapter.SixMinAdapter
import com.just.machine.ui.dialog.CommonDialogFragment
import com.just.machine.ui.dialog.DeleteWarnDialogFragment
import com.just.machine.ui.dialog.DeleteWarnDialogFragment.Companion.startDeleteWarnDialogFragment
import com.just.machine.ui.dialog.PatientDialogFragment
import com.just.machine.ui.dialog.SelectActionDialogFragment
import com.just.machine.ui.dialog.SixMinReportSelfCheckBeforeTestFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.USBTransferUtil
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
    private lateinit var usbTransferUtil: USBTransferUtil

    companion object {
        /**
         * @param context context
         */
        fun startPatientActivity(context: Context?, jumpFlag: String?) {
            val intent = Intent(context, PatientActivity::class.java)
            intent.putExtra(Constants.finishSixMinTest, jumpFlag)
            context?.startActivity(intent)
        }
    }

    private var sixMinAdapter: SixMinAdapter = SixMinAdapter(this)

    private var cardiopulmonaryAdapter: CardiopulAdapter = CardiopulAdapter(this)

    private var bean: PatientBean? = null

    val beans: MutableList<PatientBean> = ArrayList()
    private val patientInfoBeans: MutableList<PatientInfoBean> = ArrayList()

    private val adapter: PatientsAdapter = PatientsAdapter(this)

    private var jumpFlag: String? = null


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
        usbTransferUtil = USBTransferUtil.getInstance()

        viewModel.getPatients()//查询数据库

        binding.rvList.layoutManager = LinearLayoutManager(this)

        binding.rvSixTest.layoutManager = LinearLayoutManager(this)

        binding.rvCardiopulmonaryTest.layoutManager = LinearLayoutManager(this)

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.SUCCESS -> {
                    if (it.any is Int) LogUtils.e(tag + it.any)
                }

                LiveDataEvent.QueryPatient, LiveDataEvent.QueryPatientNull -> {//查询患者
                    it.any?.let { it1 -> queryPatient(it1) }
                }

                LiveDataEvent.QueryNameId, LiveDataEvent.QuerySuccess -> {
                    it.any?.let { it1 -> beanQuery(it1) }
                }
            }
        }

        initOnClick()
        jumpFlag = intent.getStringExtra(Constants.finishSixMinTest)
        if (jumpFlag != null && jumpFlag == "finishSixMinTest") {
            setButtonStyle(
                binding.btnCardiopulmonary,
                binding.btnSixMin,
                binding.rvCardiopulmonaryTest,
                binding.rvSixTest
            )
            binding.llSixMin.visible()
            binding.llCardiopulmonary.gone()
        }
    }

    private fun queryPatient(any: Any) {

        if (any is PatientBean) {
            this.bean = any
        } else {
            bean = PatientBean()
        }

        LogUtils.d(tag + bean.toString())

        if (patientInfoBeans.isNotEmpty()) {
            patientInfoBeans.forEach { it ->
                if (bean?.patientId == it.infoBean.patientId) {
                    it.sixMinReportInfo.sortByDescending { it.addTime }
                    val filter = it.sixMinReportInfo.filter { it.delFlag == "0" }
                    sixMinAdapter.setItemsBean(filter as MutableList<SixMinReportInfo>)
                }
            }
        }

//        sixMinAdapter = SixMinAdapter(this)

//        bean?.sixMinRecordsBean?.let { it1 -> sixMinAdapter.setItemsBean(it1) }

//        binding.rvSixTest.adapter = sixMinAdapter


        cardiopulmonaryAdapter = CardiopulAdapter(this)

        bean?.testRecordsBean?.let { it1 -> cardiopulmonaryAdapter.setItemsBean(it1) }

        binding.rvCardiopulmonaryTest.adapter = cardiopulmonaryAdapter

    }

    private fun beanQuery(any: Any) {
        if (any is List<*>) {
            beans.clear()
            patientInfoBeans.clear()

            val datas = any as MutableList<*>

            for (num in 0 until datas.size) {
                val bean = datas[num] as PatientInfoBean
                beans.add(bean.infoBean)
                patientInfoBeans.add(bean)
            }

            adapter.setItemsBean(beans)

            LogUtils.d(tag + beans.toString())

            binding.rvList.adapter = adapter

            if (patientInfoBeans.isNotEmpty()) {

                binding.rvSixTest.layoutManager = LinearLayoutManager(this)

                sixMinAdapter = SixMinAdapter(this)

                patientInfoBeans[0].sixMinReportInfo.sortByDescending { it.addTime }

                val filter = patientInfoBeans[0].sixMinReportInfo.filter { it.delFlag == "0" }

                filter.let { it1 -> sixMinAdapter.setItemsBean(it1 as MutableList<SixMinReportInfo>) }

                binding.rvSixTest.adapter = sixMinAdapter

                sixMinAdapter.setItemOnClickListener(object : SixMinAdapter.SixMinReportListener {
                    override fun onDeleteItem(bean: SixMinReportInfo) {
                        val startDeleteWarnDialogFragment = startDeleteWarnDialogFragment(
                            supportFragmentManager, "确认删除该试验记录吗?"
                        )
                        startDeleteWarnDialogFragment.setDeleteWarnDialogListener(object :
                            DeleteWarnDialogFragment.DeleteWarnDialogListener {
                            override fun onClickConfirm() {
                                viewModel.deleteSixMinReportInfo(bean.reportNo)
                                viewModel.getPatients()//查询数据库
                            }
                        })
                    }

                    override fun onUpdateItem(bean: SixMinReportInfo) {
                        val intent = Intent(
                            this@PatientActivity, SixMinPreReportActivity::class.java
                        )
                        val bundle = Bundle()
                        bundle.putString(Constants.sixMinPatientInfo, bean.patientId.toString())
                        bundle.putString(Constants.sixMinReportNo, bean.reportNo)
                        bundle.putString(Constants.sixMinReportType, "2")
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }

                    override fun onCheckItem(bean: SixMinReportInfo) {

                    }
                })
            }
        }
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
                val startDeleteWarnDialogFragment =
                    startDeleteWarnDialogFragment(supportFragmentManager, "确认删除该患者吗?")
                startDeleteWarnDialogFragment.setDeleteWarnDialogListener(object :
                    DeleteWarnDialogFragment.DeleteWarnDialogListener {
                    override fun onClickConfirm() {
                        viewModel.deletePatient(bean.patientId)
                    }
                })
            }

            override fun onUpdateItem(bean: PatientBean) {
                PatientDialogFragment.startPatientDialogFragment(
                    supportFragmentManager, bean
                )//修改患者信息
            }

            override fun onActionItem(bean: PatientBean) {
                val startSelectActionDialogFragment =
                    SelectActionDialogFragment.startSelectActionDialogFragment(
                        supportFragmentManager
                    )
                startSelectActionDialogFragment.setSelectActionDialogListener(object :
                    SelectActionDialogFragment.SelectActionDialogListener {
                    override fun onClickConfirm(actionType: Int) {
                        //0 心肺测试  1 6分钟测试
                        if (actionType == 0) {

                        } else {
                            if (usbTransferUtil.isConnectUSB && usbTransferUtil.bloodOxygenConnection && usbTransferUtil.ecgConnection && usbTransferUtil.bloodPressureConnection) {
                                val selfCheckBeforeTestDialogFragment =
                                    SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
                                        supportFragmentManager, "1", "1"
                                    )
                                selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(
                                    object :
                                        SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener {
                                        override fun onClickConfirm(
                                            befoFatigueLevel: Int,
                                            befoBreathingLevel: Int,
                                            befoFatigueLevelStr: String,
                                            befoBreathingLevelStr: String
                                        ) {
                                            val intent = Intent(
                                                this@PatientActivity, SixMinActivity::class.java
                                            )
                                            val bundle = Bundle()
                                            bundle.putString(
                                                Constants.sixMinSelfCheckViewSelection,
                                                "$befoFatigueLevelStr&$befoBreathingLevelStr"
                                            )
                                            bundle.putString(
                                                Constants.sixMinPatientInfo,
                                                bean.patientId.toString()
                                            )
                                            intent.putExtras(bundle)
                                            startActivity(intent)
                                        }

                                        override fun onClickClose() {

                                        }
                                    })
                            } else {
                                val startCommonDialogFragment =
                                    CommonDialogFragment.startCommonDialogFragment(
                                        supportFragmentManager,
                                        getString(R.string.sixmin_test_enter_test_without_device_connection)
                                    )
                                startCommonDialogFragment.setCommonDialogOnClickListener(object :
                                    CommonDialogFragment.CommonDialogClickListener {
                                    override fun onPositiveClick() {
                                        val selfCheckBeforeTestDialogFragment =
                                            SixMinReportSelfCheckBeforeTestFragment.startPatientSelfCheckDialogFragment(
                                                supportFragmentManager, "1", "1"
                                            )
                                        selfCheckBeforeTestDialogFragment.setSelfCheckBeforeTestDialogOnClickListener(
                                            object :
                                                SixMinReportSelfCheckBeforeTestFragment.SixMinReportSelfCheckBeforeTestDialogListener {
                                                override fun onClickConfirm(
                                                    befoFatigueLevel: Int,
                                                    befoBreathingLevel: Int,
                                                    befoFatigueLevelStr: String,
                                                    befoBreathingLevelStr: String
                                                ) {
                                                    val intent = Intent(
                                                        this@PatientActivity,
                                                        SixMinActivity::class.java
                                                    )
                                                    val bundle = Bundle()
                                                    bundle.putString(
                                                        Constants.sixMinSelfCheckViewSelection,
                                                        "$befoFatigueLevelStr&$befoBreathingLevelStr"
                                                    )
                                                    bundle.putString(
                                                        Constants.sixMinPatientInfo,
                                                        bean.patientId.toString()
                                                    )
                                                    intent.putExtras(bundle)
                                                    startActivity(intent, bundle)
                                                }

                                                override fun onClickClose() {

                                                }
                                            })
                                    }

                                    override fun onNegativeClick() {

                                    }

                                    override fun onStopNegativeClick(stopReason: String) {

                                    }
                                })
                            }
                        }
                    }
                })
            }
        })

        adapter.setItemClickListener { item, position ->
            viewModel.getPatient(item.patientId)//查询数据库
            adapter.toggleItemBackground(position)
        }

        cardiopulmonaryAdapter.setItemClickListener { item, position ->
            cardiopulmonaryAdapter.toggleItemBackground(position)
            LogUtils.d(tag + item.toString())
        }

        sixMinAdapter.setItemClickListener { item, position ->
            sixMinAdapter.toggleItemBackground(position)
        }

        binding.btnAdd.setNoRepeatListener {
            val patientDialogFragment =
                PatientDialogFragment.startPatientDialogFragment(supportFragmentManager)//添加患者修改患者信息
            patientDialogFragment.setDialogOnClickListener(object :
                PatientDialogFragment.PatientDialogListener {
                override fun onClickConfirmBtn(patientId:String) {//确认

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
        textView2.background = ContextCompat.getDrawable(this, R.drawable.super_edittext_bg)
        showRecyclerView.gone()
        hideRecyclerView.visible()
    }

    override fun getViewBinding() = ActivityPatientBinding.inflate(layoutInflater)
}