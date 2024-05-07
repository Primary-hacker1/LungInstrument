package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.machine.model.SixMinReportPatientSelfBean
import com.just.machine.model.SixMinReportPatientSelfItemBean
import com.just.machine.ui.adapter.SixMinReportPatientSelfAdapter
import com.just.machine.util.USBTransferUtil
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminReportSelfCheckBeforeTestBinding

/**
 * 6分钟选择试验前呼吸和疲劳状况等级dialog
 */
class SixMinReportSelfCheckBeforeTestFragment :
    BaseDialogFragment<FragmentDialogSixminReportSelfCheckBeforeTestBinding>() {

    private var listener: SixMinReportSelfCheckBeforeTestDialogListener? = null
    private val patientSelfList = mutableListOf<SixMinReportPatientSelfBean>()
    private var selectList = mutableListOf<Int>()
    private var selectStrList = mutableListOf<String>()
    private var selfCheck = "" //"" 右边按钮为确定 其它为进入试验
    private var selfCheckSelection = "" // ""没有选择 其它根据内容将对应的复选框选中
    private lateinit var usbTransferUtil: USBTransferUtil

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startPatientSelfCheckDialogFragment(
            fragmentManager: FragmentManager, checkable: String = "1",//是否可以选择  1可以选择 其它不可以
            selfCheck: String = "", checkStr: String = ""

        ): SixMinReportSelfCheckBeforeTestFragment {

            val dialogFragment = SixMinReportSelfCheckBeforeTestFragment()

            dialogFragment.show(
                fragmentManager, SixMinReportSelfCheckBeforeTestFragment::javaClass.toString()
            )

            val bundle = Bundle()

            bundle.putString(Constants.sixMinSelfCheckView, checkable)
            bundle.putString(Constants.sixMinSelfCheck, selfCheck)
            bundle.putString(Constants.sixMinSelfCheckViewSelection, checkStr)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    interface SixMinReportSelfCheckBeforeTestDialogListener {
        fun onClickConfirm(
            befoFatigueLevel: Int,
            befoBreathingLevel: Int,
            befoFatigueLevelStr: String,
            befoBreathingLevelStr: String
        )

        fun onClickClose()
    }

    fun setSelfCheckBeforeTestDialogOnClickListener(listener: SixMinReportSelfCheckBeforeTestDialogListener) {
        this.listener = listener
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        binding.sixminRvPatientSelfCheck.layoutManager = LinearLayoutManager(activity)
        val patientSelfItemAdapter = SixMinReportPatientSelfAdapter(this.requireContext())
        patientSelfItemAdapter.setItemsBean(patientSelfList)
        binding.sixminRvPatientSelfCheck.adapter = patientSelfItemAdapter
        if (selfCheck == "") {
            binding.sixminReportTvEditBloodPressureConfirm.text =
                getString(R.string.sixmin_test_report_confirm)
        } else {
            binding.sixminReportTvEditBloodPressureConfirm.text =
                getString(R.string.sixmin_test_report_check_report_enter_test)
        }
    }

    override fun initListener() {
        binding.sixminReportTvEditBloodPressureConfirm.setNoRepeatListener {
            selectList.clear()
            selectStrList.clear()
            patientSelfList.forEach {
                it.itemList.forEach { it1 ->
                    if (it1.itemCheck == "1") {
                        selectList.add(it.itemList.indexOf(it1))
                        val index = it1.itemName.indexOf("级")
                        selectStrList.add("${it.itemName}&${it1.itemName.substring(0, index)}")
                    }
                }
            }

            if (selectStrList.isNotEmpty()) {
                if (selectStrList.size > 1) {
                    dismiss()
                    listener?.onClickConfirm(
                        selectList[0], selectList[1], selectStrList[0].split("&")[1], selectStrList[1].split("&")[1]
                    )
                } else {
                    val split = selectStrList[0].split("&")
                    if (split[0] == "呼吸状况等级") {
                        Toast.makeText(context, "请选择试验前的疲劳状况", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "请选择试验前的呼吸状况", Toast.LENGTH_SHORT).show()
                    }
                    return@setNoRepeatListener
                }
            } else {
                Toast.makeText(context, "请选择试验前状况评级", Toast.LENGTH_SHORT).show()
                return@setNoRepeatListener
            }
        }
        binding.sixminReportTvEditBloodPressureClose.setOnClickListener {
            dismiss()
        }
    }

    override fun initData() {
        usbTransferUtil = USBTransferUtil.getInstance()
        val checkAble = arguments?.getString(Constants.sixMinSelfCheckView, "")
        selfCheck = arguments?.getString(Constants.sixMinSelfCheck, "").toString()
        selfCheckSelection =
            arguments?.getString(Constants.sixMinSelfCheckViewSelection, "").toString()
        var breathLevel = ""
        var fatigueLevel = ""
        if (selfCheckSelection.isNotEmpty() && selfCheckSelection.contains("&")) {
            val split = selfCheckSelection.split("&")
            breathLevel = split[0]
            fatigueLevel = split[1]
        }
        patientSelfList.clear()
        val patientBreathSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0级",
                "没有",
                if (breathLevel == "0") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0.5级",
                "非常非常轻",
                if (breathLevel == "0.5") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "1级",
                "非常轻",
                if (breathLevel == "1") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "2级",
                "很轻",
                if (breathLevel == "2") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "3级",
                "中度",
                if (breathLevel == "3") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "4级",
                "较严重",
                if (breathLevel == "4") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "5-6级",
                "严重",
                if (breathLevel == "5-6") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "7-9级",
                "非常严重",
                if (breathLevel == "7-9") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "10级",
                "非常非常严重",
                if (breathLevel == "10") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "呼吸状况等级",
                "1",
                patientBreathSelfItemList,
                usbTransferUtil.dealSelfCheckBreathingLevel(breathLevel),
            )
        )
        val patientTiredSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0级", "没有", if (fatigueLevel == "0") "1" else "0", if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "1级", "非常轻松", if (fatigueLevel == "1") "1" else "0", if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "2级", "轻松", if (fatigueLevel == "2") "1" else "0", if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "3级", "中度", if (fatigueLevel == "3") "1" else "0", if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "4级", "有点疲劳", if (fatigueLevel == "4") "1" else "0", if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "5-6级", "疲劳", if (fatigueLevel == "5-6") "1" else "0", if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "7-8级", "非常疲劳", if (fatigueLevel == "7-8") "1" else "0", if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "9-10级",
                "非常非常疲劳(几乎到极限)",
                if (fatigueLevel == "9-10") "1" else "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "疲劳状况等级",
                "2",
                patientTiredSelfItemList,
                usbTransferUtil.dealSelfCheckFatigueLevel(fatigueLevel),
            )
        )

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_report_self_check_before_test
    }
}