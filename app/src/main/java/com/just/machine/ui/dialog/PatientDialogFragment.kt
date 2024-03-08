package com.just.machine.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.just.machine.dao.PatientBean
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.machine.model.Constants
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.news.R
import com.just.news.databinding.FragmentDialogPatientBinding
import com.justsafe.libview.util.StringUtils
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
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
        fun startPatientDialogFragment(

            fragmentManager: FragmentManager,

            bean: PatientBean? = PatientBean()

        ): PatientDialogFragment {

            val dialogFragment = PatientDialogFragment()

            dialogFragment.show(fragmentManager, PatientDialogFragment::javaClass.toString())

            val bundle = Bundle()

            bundle.putParcelable("patientBean", bean)

            dialogFragment.arguments = bundle

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
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        val attributes = dialog?.window?.attributes
        attributes?.gravity = Gravity.CENTER
        attributes?.width = dm.widthPixels / 7 * 4
        attributes?.height = dm.heightPixels / 7 * 5
        dialog?.window?.attributes = attributes
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        binding.tvTitle.text = Constants.addPatient//标题


    }

    override fun initData() {
        viewModel.updatePatients(PatientBean())//修改数据
    }

    var index = 0

    override fun initListener() {


        binding.rgSex.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_man -> {

                }

                R.id.rb_woman -> {

                }

                else -> {

                }
            }
        }


        binding.btnYes.setNoRepeatListener {

            listener?.onClickConfirmBtn()

            hideKeyboard(it.windowToken)

            val patient = PatientBean()

            if (Constants.isDebug) {

                patient.name = "张三$index"

                patient.age = (1 + index).toString()

                patient.sex = "男$index"

                patient.height = "18$index"

                patient.addTime = "2024-3-6 13:00"

                val testRecordsBeans: MutableList<CardiopulmonaryRecordsBean> = ArrayList()//心肺测试记录

                val sixMinRecordsBeans: MutableList<SixMinRecordsBean> = ArrayList()//六分钟测试记录

                val sixMinRecordsBean = SixMinRecordsBean("", "123456", "2024-3-7 13:00")

                val cardiopulmonaryRecordsBean = CardiopulmonaryRecordsBean(
                    "测试1",
                    "测试2", "测试3", "测试4", "测试5",
                )

                sixMinRecordsBeans.add(sixMinRecordsBean)

                testRecordsBeans.add(cardiopulmonaryRecordsBean)

                patient.testRecordsBean = testRecordsBeans

                patient.sixMinRecordsBean = sixMinRecordsBeans

                viewModel.setDates(patient)//新增患者
            }

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
            viewModel.setDates(patient)//新增患者
        }

        binding.btnNo.setNoRepeatListener {
            listener?.onClickCleanBtn()
        }

        binding.atvBirthday.setNoRepeatListener {
            hideKeyboard(it.windowToken)
            val displayList: MutableList<Int> = mutableListOf()
            displayList.add(DateTimeConfig.YEAR)
            displayList.add(DateTimeConfig.MONTH)
            displayList.add(DateTimeConfig.DAY)

//        val model = R.drawable.shape_bg_dialog_custom

            val pickerLayout = 0

            val dialog = CardDatePickerDialog.builder(requireContext())
                .setTitle("DATE&TIME PICKER")
                .setDisplayType(displayList)
//            .setBackGroundModel(model)
                .showBackNow(true)
                .setMaxTime(0)
                .setPickerLayout(pickerLayout)
                .setMinTime(0)
                .setDefaultTime(System.currentTimeMillis())
                .setTouchHideable(true)
                .setWrapSelectorWheel(false)
//            .setThemeColor(if (model == R.drawable.shape_bg_dialog_custom) Color.parseColor("#FF8000") else 0)
                .showDateLabel(true)
                .showFocusDateInfo(true)
                .setOnChoose("选择") {

                    val time = StringUtils.conversionTime(it)

                    binding.atvBirthday.setText(time)
                }
                .setOnCancel("关闭") {
                }.build()
            dialog.show()
            (dialog as BottomSheetDialog).behavior.isHideable = false
        }
    }


    override fun getLayout(): Int {
        return R.layout.fragment_dialog_patient
    }
}