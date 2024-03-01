package com.just.machine.ui.dialog

import android.app.Dialog
import com.common.base.BaseDialogFragment
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.FragmentDialogPatientBinding

/**
 *create by 2024/3/1
 * 患者信息add
 *@author zt
 */
class PatientDialogFragment : BaseDialogFragment<FragmentDialogPatientBinding>() {
    override fun start(dialog: Dialog?) {

    }

    override fun initView() {
        binding.toolbar.title = Constants.addPatient//标题
    }

    override fun initListener() {

    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return  R.layout.fragment_dialog_patient
    }
}