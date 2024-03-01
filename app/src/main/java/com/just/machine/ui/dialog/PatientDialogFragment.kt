package com.just.machine.ui.dialog

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.dao.PatientBean
import com.just.machine.model.Constants
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentDialogPatientBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/3/1
 * 患者信息add update
 *@author zt
 */
@AndroidEntryPoint
class PatientDialogFragment : BaseDialogFragment<FragmentDialogPatientBinding>() {

    companion object {
        /**
         * @param fragmentManager FragmentManager
         */
        fun startPatientDialogFragment(fragmentManager: FragmentManager) : PatientDialogFragment {
            val dialogFragment = PatientDialogFragment()
            dialogFragment.show(fragmentManager, PatientDialogFragment::javaClass.toString())
            return dialogFragment
        }
    }

    private var listener: PatientDialogListener? = null

    interface PatientDialogListener {
        fun onClickConfirmBtn()
        fun onClickCleanBtn()
    }

    fun setDialogOnClickListener(listener: PatientDialogListener) {
        this.listener = listener
    }


    private val viewModel by viewModels<MainViewModel>()

    override fun start(dialog: Dialog?) {

    }

    override fun initView() {
        binding.toolbar.title = Constants.addPatient//标题
    }

    override fun initListener() {
        binding.btnYes.setNoRepeatListener {
            listener?.onClickConfirmBtn()
        }

        binding.btnNo.setNoRepeatListener {
            listener?.onClickCleanBtn()
        }
    }

    override fun initData() {
        viewModel.setDates(PatientBean())//新增患者
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_patient
    }
}