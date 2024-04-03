package com.just.machine.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.common.base.CommonBaseFragment
import com.common.base.setNoRepeatListener
import com.common.viewmodel.LiveDataEvent
import com.just.machine.model.SharedPreferencesUtils
import com.just.machine.ui.activity.CardiopulmonaryActivity
import com.just.machine.ui.activity.PatientActivity
import com.just.machine.ui.activity.SixMinActivity
import com.just.machine.ui.dialog.PatientDialogFragment
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 *create by 2020/6/19
 * 主界面
 *@author zt
 */
@AndroidEntryPoint
class MainFragment : CommonBaseFragment<FragmentMainBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private var patientListSize = 0
    override fun loadData() {//懒加载
        viewModel.getPatients()
        viewModel.mEventHub.observe(this) {
            when (it.action) {
                LiveDataEvent.QuerySuccess -> {
                    if (it.any is List<*>) {
                        val datas = it.any as MutableList<*>
                        patientListSize = datas.size
                    }
                }
            }
        }
    }

    override fun initView() {

    }

    override fun initListener() {
        binding.walkTestButton.setNoRepeatListener {
            when (patientListSize) {
                0 -> {
                    val patientDialogFragment =
                        PatientDialogFragment.startPatientDialogFragment(parentFragmentManager)//添加患者修改患者信息
                    patientDialogFragment.setDialogOnClickListener(object :
                        PatientDialogFragment.PatientDialogListener {
                        override fun onClickConfirmBtn() {//确认
                            SharedPreferencesUtils.instance.isClickBtn = "1"
                            patientDialogFragment.dismiss()
                            val intent = Intent(activity, SixMinActivity::class.java)
                            startActivity(intent)
                        }

                        override fun onClickCleanBtn() {//取消
                            patientDialogFragment.dismiss()
                        }
                    })
                }
                else -> {
                    val intent = Intent(activity, SixMinActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.btnEcg.setNoRepeatListener {//心肺测试
            val isClick = SharedPreferencesUtils.instance.isClickBtn
            when (isClick) {
                "" -> {
                    val patientDialogFragment =
                        PatientDialogFragment.startPatientDialogFragment(parentFragmentManager)//添加患者修改患者信息
                    patientDialogFragment.setDialogOnClickListener(object :
                        PatientDialogFragment.PatientDialogListener {
                        override fun onClickConfirmBtn() {//确认
                            SharedPreferencesUtils.instance.isClickBtn = "1"
                            patientDialogFragment.dismiss()
                            CardiopulmonaryActivity.startCardiopulmonaryActivity(context)
                        }

                        override fun onClickCleanBtn() {//取消
                            patientDialogFragment.dismiss()
                        }
                    })
                }

                "1" -> {
                    CardiopulmonaryActivity.startCardiopulmonaryActivity(context)
                }

                null -> TODO()
            }
        }

        binding.btnPatientInformation.setNoRepeatListener {
            PatientActivity.startPatientActivity(context,null)
        }

        binding.btnClose.setNoRepeatListener {
            activity?.finish()
        }
    }


    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

}