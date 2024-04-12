package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.BaseDialogFragment
import com.just.machine.model.Constants
import com.just.machine.model.SixMinReportPatientSelfBean
import com.just.machine.model.SixMinReportPatientSelfItemBean
import com.just.machine.ui.adapter.SixMinReportPatientSelfAdapter
import com.just.news.R
import com.just.news.databinding.FragmentDialogSixminReportSelfCheckBeforeTestBinding
import com.just.news.databinding.ItemSixminReportPatientSelfBinding

class SixMinReportSelfCheckBeforeTestFragment :
    BaseDialogFragment<FragmentDialogSixminReportSelfCheckBeforeTestBinding>() {

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startPatientSelfCheckDialogFragment(
            fragmentManager: FragmentManager,
            checkable: String

        ): SixMinReportSelfCheckBeforeTestFragment {

            val dialogFragment = SixMinReportSelfCheckBeforeTestFragment()

            dialogFragment.show(
                fragmentManager,
                SixMinReportSelfCheckBeforeTestFragment::javaClass.toString()
            )

            val bundle = Bundle()

            bundle.putString(Constants.viewSixMinSelfCheck, checkable)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    private val patientSelfList = mutableListOf<SixMinReportPatientSelfBean>()

    override fun start(dialog: Dialog?) {

    }

    override fun initView() {
        binding.sixminRvPatientSelfCheck.layoutManager = LinearLayoutManager(activity)
        val patientSelfItemAdapter = SixMinReportPatientSelfAdapter(this.requireContext())
        patientSelfItemAdapter.setItemsBean(patientSelfList)
        binding.sixminRvPatientSelfCheck.adapter = patientSelfItemAdapter
    }

    override fun initListener() {
        binding.sixminReportTvEditBloodPressureConfirm.setOnClickListener {
            dismiss()
        }
        binding.sixminReportTvEditBloodPressureClose.setOnClickListener {
            dismiss()
        }
    }

    override fun initData() {
        val checkAble = arguments?.getString(Constants.viewSixMinSelfCheck, "")
        patientSelfList.clear()
        val patientBreathSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0级",
                "没有",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0.5级",
                "非常非常轻",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "1级",
                "非常轻",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "2级",
                "很轻",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "3级",
                "中度",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "4级",
                "较严重",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "5-6级",
                "严重",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "7-9级",
                "非常严重",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientBreathSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "10级",
                "非常非常严重",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "呼吸状况等级",
                "1",
                patientBreathSelfItemList
            )
        )
        val patientTiredSelfItemList = mutableListOf<SixMinReportPatientSelfItemBean>()
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0级",
                "没有",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "0.5级",
                "非常轻松",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "1级",
                "轻松",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "2级",
                "很轻",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "3级",
                "中度",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "4级",
                "有点疲劳",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "5-6级",
                "疲劳",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "7-9级",
                "非常疲劳",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientTiredSelfItemList.add(
            SixMinReportPatientSelfItemBean(
                "10级",
                "非常非常疲劳(几乎到极限)",
                "0",
                if (checkAble == "" || checkAble == "0") "0" else "1"
            )
        )
        patientSelfList.add(
            SixMinReportPatientSelfBean(
                "疲劳状况等级",
                "2",
                patientTiredSelfItemList
            )
        )
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_report_self_check_before_test
    }

}