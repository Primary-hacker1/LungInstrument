package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.model.Constants
import com.just.news.R
import com.just.news.databinding.FragmentDialogCommonBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonDialogFragment : BaseDialogFragment<FragmentDialogCommonBinding>() {

    private var listener: CommonDialogClickListener? = null
    private var dialogContent = ""//dialog内容
    private var stopReasonList = mutableListOf<String>()//停止或者提前终止试验的原因
    private var dialogType = ""// ""为普通dialog 其它为停止或者提前终止试验弹窗
    private var stopType = "" // "0"为正常停止试验  "1"为提前停止试验

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startCommonDialogFragment(

            fragmentManager: FragmentManager,
            dialogContent: String,
            dialogType:String = "",
            stopType:String = "0",
        ): CommonDialogFragment {

            val dialogFragment = CommonDialogFragment()

            dialogFragment.show(fragmentManager, CommonDialogFragment::javaClass.toString())

            val bundle = Bundle()

            bundle.putString(Constants.commonDialogContent, dialogContent)
            bundle.putString(Constants.commonDialogType, dialogType)
            bundle.putString(Constants.commonDialogStopType, stopType)

            dialogFragment.arguments = bundle

            return dialogFragment
        }
    }

    interface CommonDialogClickListener {
        fun onPositiveClick()
        fun onNegativeClick()

        fun onStopNegativeClick(stopReason:String)
    }

    fun setCommonDialogOnClickListener(listener: CommonDialogClickListener) {
        this.listener = listener
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        if(dialogType == ""){
            binding.llCommonDialog.visibility = View.VISIBLE
            binding.llCommonDialogStop.visibility = View.GONE
        }else{
            binding.llCommonDialog.visibility = View.GONE
            binding.llCommonDialogStop.visibility = View.VISIBLE
        }
        if(dialogContent != ""){
            binding.tvCommonDialogContent.text = dialogContent
        }
        if(stopType == "0"){
            binding.tvCommonDialogStopTitle.text = "是否有不良症状"
            binding.tvCommonDialogStopOtherReason.text = "其他症状"
        }else{
            binding.tvCommonDialogStopTitle.text = "请选择停止原因"
            binding.tvCommonDialogStopOtherReason.text = "其他原因"
        }
    }

    override fun initListener() {
        binding.ivCommonDialogClose.setNoRepeatListener {
            dismiss()
        }
        binding.tvCommonDialogPositive.setNoRepeatListener {
            dismiss()
            listener?.onPositiveClick()
        }
        binding.tvCommonDialogNegative.setNoRepeatListener {
            dismiss()
            listener?.onNegativeClick()
        }
        binding.rbCommonDialogStopEcgProblem.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                stopReasonList.add("心电异常")
            }else{
                if(stopReasonList.isNotEmpty()){
                    stopReasonList.remove("心电异常")
                }
            }
        }
        binding.rbCommonDialogStopBodySpasm.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                stopReasonList.add("下肢痉挛")
            }else{
                if(stopReasonList.isNotEmpty()){
                    stopReasonList.remove("下肢痉挛")
                }
            }
        }
        binding.rbCommonDialogStopWalkHard.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                stopReasonList.add("步履蹒跚")
            }else{
                if(stopReasonList.isNotEmpty()){
                    stopReasonList.remove("步履蹒跚")
                }
            }
        }
        binding.rbCommonDialogStopExhaustionSweat.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                stopReasonList.add("虚脱出汗")
            }else{
                if(stopReasonList.isNotEmpty()){
                    stopReasonList.remove("虚脱出汗")
                }
            }
        }
        binding.rbCommonDialogStopFaceWhite.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                stopReasonList.add("面色苍白")
            }else{
                if(stopReasonList.isNotEmpty()){
                    stopReasonList.remove("面色苍白")
                }
            }
        }
        binding.rbCommonDialogStopBreathHard.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                stopReasonList.add("呼吸困难")
            }else{
                if(stopReasonList.isNotEmpty()){
                    stopReasonList.remove("呼吸困难")
                }
            }
        }
        binding.rbCommonDialogStopChestPain.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                stopReasonList.add("胸痛")
            }else{
                if(stopReasonList.isNotEmpty()){
                    stopReasonList.remove("胸痛")
                }
            }
        }
        binding.tvCommonDialogStopNegative.setNoRepeatListener {
            val otherStopReason = binding.etCommonDialogStopOtherReason.text.toString()
            if(stopReasonList.isNotEmpty()){
                if(stopReasonList.size > 3){
                    Toast.makeText(requireContext(),if(stopType == "0") "最多选择三个症状" else "最多选择三个原因",Toast.LENGTH_SHORT).show()
                    return@setNoRepeatListener
                }
            }else{
                if(otherStopReason != "" && otherStopReason.length > 9){
                    Toast.makeText(requireContext(),if(stopType == "0") "其他症状长度不能大于9" else "其他原因长度不能大于9",Toast.LENGTH_SHORT).show()
                    return@setNoRepeatListener
                }
            }
            var stopReason = ""
            for (i in 0 until stopReasonList.size) {
                stopReason += if (i == stopReasonList.size - 1) {
                    stopReasonList[i]
                } else {
                    stopReasonList[i] + ","
                }
            }
            if(otherStopReason.isNotEmpty()){
                if(stopReason.isNotEmpty()){
                    stopReason += ",$otherStopReason"
                }else{
                    stopReason = otherStopReason
                }
            }
            listener?.onStopNegativeClick(stopReason)
            dismiss()
        }
    }

    override fun initData() {
        dialogContent = arguments?.getString(Constants.commonDialogContent,"").toString()
        dialogType = arguments?.getString(Constants.commonDialogType,"").toString()
        stopType = arguments?.getString(Constants.commonDialogStopType,"").toString()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_common
    }

}