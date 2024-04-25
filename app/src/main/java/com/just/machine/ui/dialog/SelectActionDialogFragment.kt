package com.just.machine.ui.dialog

import android.app.Dialog
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.dao.PatientBean
import com.just.news.R
import com.just.news.databinding.FragmentDialogSelectActionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectActionDialogFragment : BaseDialogFragment<FragmentDialogSelectActionBinding>() {


    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startSelectActionDialogFragment(

            fragmentManager: FragmentManager,

            bean: PatientBean? = PatientBean()

        ): SelectActionDialogFragment {

            val dialogFragment = SelectActionDialogFragment()

            dialogFragment.show(fragmentManager, SelectActionDialogFragment::javaClass.toString())

            return dialogFragment
        }
    }

    override fun start(dialog: Dialog?) {

    }

    override fun initView() {

    }

    override fun initListener() {
        binding.ivBtnSixmin.setNoRepeatListener {

        }
        binding.ivBtnXf.setNoRepeatListener {

        }
    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_select_action
    }
}