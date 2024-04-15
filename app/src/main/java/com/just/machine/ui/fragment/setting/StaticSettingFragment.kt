package com.just.machine.ui.fragment.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.*
import com.common.network.LogUtils
import com.just.machine.model.Constants
import com.just.machine.model.setting.FvcSettingBean
import com.just.machine.model.setting.MvvSettingBean
import com.just.machine.model.setting.StaticSettingBean
import com.just.machine.model.setting.SvcSettingBean
import com.just.machine.ui.adapter.setting.FVCSettingAdapter
import com.just.machine.ui.adapter.setting.MVVSettingAdapter
import com.just.machine.ui.adapter.setting.SVCSettingAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FrgamentStaticSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2018/6/19
 * 静态肺设置
 *@author zt
 */
@AndroidEntryPoint
class StaticSettingFragment : CommonBaseFragment<FrgamentStaticSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private val staticSettingBean by lazy { StaticSettingBean() }

    private val adapterSvc by lazy { SVCSettingAdapter(requireContext()) }

    private val adapterFvc by lazy { FVCSettingAdapter(requireContext()) }

    private val adapterMvv by lazy { MVVSettingAdapter(requireContext()) }
    override fun loadData() {//懒加载

    }

    override fun initView() {

        initData()

        binding.rvSvc.layoutManager = LinearLayoutManager(context)
        binding.rvFvc.layoutManager = LinearLayoutManager(context)
        binding.rvMvv.layoutManager = LinearLayoutManager(context)

        adapterSvc.setItemsBean(
            mutableListOf(
                SvcSettingBean("1", "2", "3", true),
                SvcSettingBean("2", "2", "3", true),
                SvcSettingBean("3", "2", "3", true),
                SvcSettingBean("4", "2", "3", false),
                SvcSettingBean("5", "2", "3", false),
                SvcSettingBean("6", "2", "3", false),
                SvcSettingBean("7", "2", "3", true),
                SvcSettingBean("8", "2", "3", true),
                SvcSettingBean("9", "2", "3", false),
                SvcSettingBean("10", "2", "3", true),
                SvcSettingBean("11", "5", "6", true)
            )
        )


        binding.rvSvc.adapter = adapterSvc


        adapterFvc.setItemsBean(
            mutableListOf(
                FvcSettingBean("1", "2", "3", true),
                FvcSettingBean("4", "5", "6", false)
            )
        )

        binding.rvFvc.adapter = adapterFvc


        adapterMvv.setItemsBean(
            mutableListOf(
                MvvSettingBean("1", "2", "3", true),
                MvvSettingBean("4", "5", "6", false)
            )
        )

        binding.rvMvv.adapter = adapterMvv
    }

    private fun initData() {

        //这里获取本地缓存的设置sp

        if (staticSettingBean.radioVt == true) {
            binding.radioVtVisible.isChecked = true
        } else {
            binding.radioVtGone.isChecked = true
        }

        if (staticSettingBean.radioVideoAutoplaySvc == true) {
            binding.radioSvcVideoVisible.isChecked = true
        } else {
            binding.radioSvcVideoGone.isChecked = true
        }

        if (staticSettingBean.radioPredictionRing == true) {
            binding.radioPredictionRingVisible.isChecked = true
        } else {
            binding.radioPredictionRingGone.isChecked = true
        }

        if (staticSettingBean.radioVideoAutoplayFvc == true) {
            binding.radioFvcVideoVisible.isChecked = true
        } else {
            binding.radioFvcVideoGone.isChecked = true
        }

        if (staticSettingBean.radioStartRuler == true) {
            binding.radioStartRulerVisible.isChecked = true
        } else {
            binding.radioStartRulerGone.isChecked = true
        }

        if (staticSettingBean.radioVentilationCurve == true) {
            binding.radioVentilationCurveVisible.isChecked = true
        } else {
            binding.radioVentilationCurveGone.isChecked = true
        }

        if (staticSettingBean.radioVideoAutoplayMvv == true) {
            binding.radioVideoMvvVisible.isChecked = true
        } else {
            binding.radioVideoMvvGone.isChecked = true
        }

        binding.editXTimeSvc.setText(staticSettingBean.xTimeSvc)

        binding.editYTimeUpSvc.setText(staticSettingBean.yTimeUpSvc)

        binding.editYTimeDownSvc.setText(staticSettingBean.yTimeDownSvc)

        binding.editXTimeFvc.setText(staticSettingBean.xTimeFvc)

        binding.editYTimeUpFvc.setText(staticSettingBean.yTimeUpFvc)

        binding.editYTimeDownFvc.setText(staticSettingBean.yTimeDownFvc)

        binding.editXTimeMvv.setText(staticSettingBean.xTimeMvv)

        binding.editYTimeUpMvv.setText(staticSettingBean.yTimeUpMvv)

        binding.editYTimeDownMvv.setText(staticSettingBean.yTimeDownMvv)
    }

    override fun initListener() {
        //vt基线
        binding.radioVt.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_vt_visible -> {
                    staticSettingBean.radioVt = true
                }

                R.id.radio_vt_gone -> {
                    staticSettingBean.radioVt = false
                }
            }
        }

        //视频自动播放svc
        binding.radioVideoAutoplaySvc.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_svc_video_visible -> {
                    staticSettingBean.radioVideoAutoplaySvc = true
                }

                R.id.radio_svc_video_gone -> {
                    staticSettingBean.radioVideoAutoplaySvc = false
                }
            }
        }

        //预测值环
        binding.radioPredictionRing.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_predictionRing_visible -> {
                    staticSettingBean.radioPredictionRing = true
                }

                R.id.radio_predictionRing_gone -> {
                    staticSettingBean.radioPredictionRing = false
                }
            }
        }

        //视频自动播放fvc
        binding.radioVideoAutoplayFvc.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_fvc_video_visible -> {
                    staticSettingBean.radioVideoAutoplayFvc = true
                }

                R.id.radio_fvc_video_gone -> {
                    staticSettingBean.radioVideoAutoplayFvc = false
                }
            }
        }

        //开始结算标尺
        binding.radioStartRuler.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_startRuler_visible -> {
                    staticSettingBean.radioStartRuler = true
                }

                R.id.radio_startRuler_gone -> {
                    staticSettingBean.radioStartRuler = false
                }
            }
        }

        //通气量曲线
        binding.radioVentilationCurve.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_ventilationCurve_visible -> {
                    staticSettingBean.radioVentilationCurve = true
                }

                R.id.radio_ventilationCurve_gone -> {
                    staticSettingBean.radioVentilationCurve = false
                }
            }
        }

        //视频自动播放mvv
        binding.radioVideoAutoplayMvv.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_video_mvv_visible -> {
                    staticSettingBean.radioVideoAutoplayMvv = true
                }

                R.id.radio_video_mvv_gone -> {
                    staticSettingBean.radioVideoAutoplayMvv = false
                }
            }
        }

        val fragment = parentFragment

        if (fragment is CardiopulmonarySettingFragment){//保存
            fragment.onSaveCLick().setNoRepeatListener {
                staticSettingBean.xTimeSvc = binding.editXTimeSvc.text.toString()
                staticSettingBean.yTimeUpSvc = binding.editYTimeUpSvc.text.toString()
                staticSettingBean.yTimeDownSvc = binding.editYTimeDownSvc.text.toString()

                staticSettingBean.xTimeFvc = binding.editXTimeFvc.text.toString()
                staticSettingBean.yTimeUpFvc = binding.editYTimeUpFvc.text.toString()
                staticSettingBean.yTimeDownFvc = binding.editYTimeDownFvc.text.toString()

                staticSettingBean.xTimeMvv = binding.editXTimeMvv.text.toString()
                staticSettingBean.yTimeUpMvv = binding.editYTimeUpMvv.text.toString()
                staticSettingBean.yTimeDownMvv = binding.editYTimeDownMvv.text.toString()
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FrgamentStaticSettingBinding.inflate(inflater, container, false)
}