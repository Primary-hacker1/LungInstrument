package com.just.machine.ui.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.common.base.toast
import com.common.network.LogUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.just.machine.dao.PatientBean
import com.just.machine.model.CardiopulmonaryRecordsBean
import com.just.machine.model.Constants
import com.just.machine.model.SixMinRecordsBean
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.CommonUtil
import com.just.news.R
import com.just.news.databinding.FragmentDialogPatientBinding
import com.justsafe.libview.util.DateUtils
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
         * @param bean 修改传过来的bean数据
         */
        fun startPatientDialogFragment(

            fragmentManager: FragmentManager,

            bean: PatientBean? = PatientBean()

        ): PatientDialogFragment {

            val dialogFragment = PatientDialogFragment()

            dialogFragment.show(fragmentManager, PatientDialogFragment::javaClass.toString())

            val bundle = Bundle()

            bundle.putParcelable(Constants.patientBean, bean)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    private var patient = PatientBean()

    private var listener: PatientDialogListener? = null

    private var isUpdate: Boolean = false

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
        binding.atvHeight.addTextChangedListener(textWatcher)
        binding.atvWeight.addTextChangedListener(textWatcher)
    }

    override fun initData() {
        val bean = arguments?.getParcelable<PatientBean>(Constants.patientBean)
        if (bean != null) {
            patient = bean
            if (patient.name.toString().isNotEmpty()) {
                isUpdate = true

                binding.atvName.setText(patient.name)

                binding.atvHeight.setText(patient.height)

                binding.atvWeight.setText(patient.weight)

                binding.editIdentityCard.setText(patient.identityCard)

                binding.atvBirthday.text = patient.birthday

                binding.editBmi.setText(patient.BMI)

                binding.editAge.setText(patient.age)

                binding.atvPaientNumber.setText(patient.medicalRecordNumber)

                binding.editPredictDistances.setText(patient.predictDistances)

                binding.editDiseaseHistory.setText(patient.diseaseHistory)

                binding.editCurrentMedications.setText(patient.currentMedications)

                binding.editClinicalDiagnosis.setText(patient.clinicalDiagnosis)

                binding.editRemark.setText(patient.remark)
            }
        }
    }

    var index = 0

    override fun initListener() {

//        patient.sex = "男"

        binding.rgSex.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_man -> {
                    patient.sex = "男"
                }

                R.id.rb_woman -> {
                    patient.sex = "女"
                }

                else -> {
                    patient.sex = "男"
                }
            }
        }


        binding.btnYes.setNoRepeatListener {

            if (!Constants.isDebug) {

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

                return@setNoRepeatListener
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

            patient.addTime = DateUtils.nowTimeString

            patient.name = binding.atvName.text.toString()

            patient.height = binding.atvHeight.text.toString()

            patient.weight = binding.atvWeight.text.toString()

            patient.identityCard = binding.editIdentityCard.text.toString()

            patient.birthday = binding.atvBirthday.text.toString()

            patient.BMI = binding.editBmi.text.toString()

            patient.age = binding.editAge.text.toString()

            patient.sex = if(binding.rbMan.isChecked) "男" else "女"

            patient.medicalRecordNumber = binding.atvPaientNumber.text.toString()

            patient.predictDistances = binding.editPredictDistances.text.toString()

            patient.diseaseHistory = binding.editDiseaseHistory.text.toString()

            patient.currentMedications = binding.editCurrentMedications.text.toString()

            patient.clinicalDiagnosis = binding.editClinicalDiagnosis.text.toString()

            patient.remark = binding.editRemark.text.toString()

            if (isUpdate) {

                viewModel.updatePatients(patient)//修改数据

            } else {

                viewModel.setDates(patient)//新增患者

            }

            listener?.onClickConfirmBtn()

            hideKeyboard(it.windowToken)

            LogUtils.d(tag + patient.toString())

            dismiss()
        }

        binding.btnNo.setNoRepeatListener {
            listener?.onClickCleanBtn()
            dismiss()
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

                    binding.atvBirthday.text = time
                    binding.editAge.setText(CommonUtil.getAge(time).toString())
                }
                .setOnCancel("关闭") {
                }.build()
            dialog.show()
            (dialog as BottomSheetDialog).behavior.isHideable = false
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }
        override fun afterTextChanged(s: Editable) {
            // 这里处理两个EditText的文本变化
            val height = binding.atvHeight.text.toString()
            val weight = binding.atvWeight.text.toString()
            if(height.isNotEmpty() && weight.isNotEmpty()){
                binding.editBmi.setText( String.format("%.1f", CommonUtil.calculateBmi(height.toDouble()/100, weight.toDouble())))
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_patient
    }
}