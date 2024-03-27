package com.just.machine.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import com.common.base.BaseDialogFragment
import com.common.base.setNoRepeatListener
import com.just.machine.dao.PatientBean
import com.just.machine.model.Constants
import com.just.news.databinding.FragmentDialogSixminGuideBinding
import com.just.news.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class SixMinGuideDialogFragment : BaseDialogFragment<FragmentDialogSixminGuideBinding>(), TextToSpeech.OnInitListener  {

    private var listener: SixMinGuideDialogListener? = null
    private lateinit var textToSpeech: TextToSpeech

    companion object {
        /**
         * @param fragmentManager FragmentManager
         * @param bean 修改传过来的bean数据
         */
        fun startGuideDialogFragment(

            fragmentManager: FragmentManager,

        ): SixMinGuideDialogFragment {

            val dialogFragment = SixMinGuideDialogFragment()

            dialogFragment.show(fragmentManager, SixMinGuideDialogFragment::javaClass.toString())

            return dialogFragment
        }
    }

    interface SixMinGuideDialogListener {
        fun onSelectNotSeeAnyMore(checked:Boolean)
        fun onClickStartTest()
    }

    fun setDialogOnClickListener(listener: SixMinGuideDialogListener) {
        this.listener = listener
    }

    override fun start(dialog: Dialog?) {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
    }

    override fun initView() {
        textToSpeech = TextToSpeech(this.activity?.applicationContext, this)
    }

    override fun initListener() {
        binding.sixminBtnStartTest.setNoRepeatListener {
            listener?.onClickStartTest()
        }
        binding.sixminCbNotSeeAnymore.setOnCheckedChangeListener { _, isChecked ->
            listener?.onSelectNotSeeAnyMore(isChecked)
        }
    }

    override fun initData() {

    }

    override fun getLayout(): Int {
        return R.layout.fragment_dialog_sixmin_guide
    }

    override fun onInit(status: Int) {
        //判断是否转化成功
        if (status == TextToSpeech.SUCCESS) {
            //设置语言为中文
            textToSpeech.language = Locale.CHINA
            //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech.setPitch(1f)
            //设置语速
            textToSpeech.setSpeechRate(1f)
            //在onInIt方法里直接调用tts的播报功能
            textToSpeech.setOnUtteranceProgressListener(object:UtteranceProgressListener(){
                override fun onStart(utteranceId: String?) {
                    Log.d(TAG, "onStart: $utteranceId")
                }

                override fun onDone(utteranceId: String?) {
                    activity?.runOnUiThread {
                        Log.d(TAG, "onDone: $utteranceId")
                        binding.sixminBtnStartTest.visibility = View.VISIBLE
                    }
                }

                override fun onError(utteranceId: String?) {
                    activity?.runOnUiThread {
                        Log.d(TAG, "onError: $utteranceId")
                        binding.sixminBtnStartTest.visibility = View.VISIBLE
                    }
                }
            })

            textToSpeech.speak(getString(R.string.sixmin_test_guide_text),TextToSpeech.QUEUE_ADD,null,System.currentTimeMillis().toString())
        }
    }

}