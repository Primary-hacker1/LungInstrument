package com.just.machine.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.common.base.toast
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
        binding.tvTitle.text = Constants.addPatient//标题
    }

    override fun initData() {
        viewModel.setDates(PatientBean())//新增患者
    }

    override fun initListener() {
        binding.btnYes.setNoRepeatListener {

            listener?.onClickConfirmBtn()

            hideKeyboard(it.windowToken)

            if (binding.atvName.text?.isEmpty() == true) {
                toast("姓名不能为空！")
                return@setNoRepeatListener
            }
            if (binding.atvPaientNumber.text?.isEmpty() == true) {
                toast("病历号不能为空！")
                return@setNoRepeatListener
            }
            if (binding.atvHeight.text?.isEmpty() == true) {
                toast("身高不能为空！")
                return@setNoRepeatListener
            }
            if (binding.atvWeight.text?.isEmpty() == true) {
                toast("体重不能为空！")
                return@setNoRepeatListener
            }
            if (binding.atvBirthday.text?.isEmpty() == true) {
                toast("生日不能为空！")
                return@setNoRepeatListener
            }



        }

        binding.btnNo.setNoRepeatListener {
            listener?.onClickCleanBtn()
        }
    }



    override fun getLayout(): Int {
        return R.layout.fragment_dialog_patient
    }
}