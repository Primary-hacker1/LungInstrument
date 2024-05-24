package com.just.machine.ui.fragment.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.common.base.*
import com.common.network.LogUtils
import com.common.viewmodel.LiveDataEvent.Companion.STATICSETTINGSSUCCESS
import com.just.machine.dao.setting.StaticSettingBean
import com.just.machine.model.CPETParameter
import com.just.machine.model.Constants.Companion.settingsAreSaved
import com.just.machine.model.lungdata.DynamicBean
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

    private var staticSettingBean: StaticSettingBean = StaticSettingBean()

    private val adapterSvc by lazy { SVCSettingAdapter(requireContext()) }

    private val adapterFvc by lazy { SVCSettingAdapter(requireContext()) }

    private val adapterMvv by lazy { SVCSettingAdapter(requireContext()) }
    override fun loadData() {//懒加载

    }

    override fun initView() {

        viewModel.getStaticSettings()

        initData()

        viewModel.mEventHub.observe(this) {
            when (it.action) {
                STATICSETTINGSSUCCESS -> {
                    if (it.any !is MutableList<*>) {
                        return@observe
                    }

                    val settings = it.any as MutableList<*>

                    for (settingBean in settings) {
                        if (settingBean !is StaticSettingBean) {
                            return@observe
                        }

                        staticSettingBean = settingBean
                    }
                    initSetting()
                }
            }
        }
    }

    private fun initSetting(){
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

    private fun initData() {

        initSetting()

        binding.rvSvc.layoutManager = LinearLayoutManager(context)
        binding.rvFvc.layoutManager = LinearLayoutManager(context)
        binding.rvMvv.layoutManager = LinearLayoutManager(context)

        val isSvcBean = staticSettingBean.settingSVC.isEmpty()
        val isFvcBean = staticSettingBean.settingFVC.isEmpty()
        val isMvvBean = staticSettingBean.settingMVV.isEmpty()

        if (isSvcBean) {
            // 创建一个存储 DynamicBean 对象的数组
            val dynamicBeans = arrayOf(
                DynamicBean.spinnerItemData("SVC"),
                DynamicBean.spinnerItemData("VC_ex"),
                DynamicBean.spinnerItemData("ERV"),
                DynamicBean.spinnerItemData("IRV"),
                DynamicBean.spinnerItemData("VT"),
                DynamicBean.spinnerItemData("IC")
            )

            // 创建一个空的 beans 列表
            val beansSVC: MutableList<CPETParameter> = mutableListOf()

            // 遍历 dynamicBeans 数组，将每个 DynamicBean 对象添加到 beans 列表中
            for (bean in dynamicBeans) {
                bean.let { beansSVC.add(it) }
            }

            // 设置 adapterSvc 的数据
            adapterSvc.setItemsBean(beansSVC)
        } else {
            adapterSvc.setItemsBean(staticSettingBean.settingSVC)
        }

        binding.rvSvc.adapter = adapterSvc

        if (isFvcBean) {

            // 创建一个存储 DynamicBean 对象的数组
            val fvcBeans = arrayOf(
                DynamicBean.spinnerItemData("FVC"),
                DynamicBean.spinnerItemData("FEV1"),
                DynamicBean.spinnerItemData("FEV2"),
                DynamicBean.spinnerItemData("FEV3"),
                DynamicBean.spinnerItemData("FEV6"),
                DynamicBean.spinnerItemData("FEV1/FVC"),
                DynamicBean.spinnerItemData("FEV2/FVC"),
                DynamicBean.spinnerItemData("FEV3/FVC"),
                DynamicBean.spinnerItemData("FEV6/FVC"),
                DynamicBean.spinnerItemData("PEF"),
                DynamicBean.spinnerItemData("MEF"),
                DynamicBean.spinnerItemData("FEF25"),
                DynamicBean.spinnerItemData("FEF75"),
                DynamicBean.spinnerItemData("MMEF"),
                DynamicBean.spinnerItemData("FET"),
                DynamicBean.spinnerItemData("FEF200-1200"),
                DynamicBean.spinnerItemData("PIF"),
                DynamicBean.spinnerItemData("FIF50"),
                DynamicBean.spinnerItemData("FIV1"),
                DynamicBean.spinnerItemData("FIV1%FVC"),
                DynamicBean.spinnerItemData("FEF50%FIF50"),
                DynamicBean.spinnerItemData("FEV1%FIV1"),
                DynamicBean.spinnerItemData("FEF75/85"),
                DynamicBean.spinnerItemData("TIN/ TTOT"),
                DynamicBean.spinnerItemData("TEX/ TTOT"),
                DynamicBean.spinnerItemData("TIN/TEX"),
                DynamicBean.spinnerItemData("T TOT"),
                DynamicBean.spinnerItemData("MIF"),
                DynamicBean.spinnerItemData("Vol extrap"),
                DynamicBean.spinnerItemData("MMEF"),
                DynamicBean.spinnerItemData("FVC IN"),
                DynamicBean.spinnerItemData("Time(S)"),
                DynamicBean.spinnerItemData("FVC IN"),
//                DynamicBean.spinnerItemData("P0.1"),
                DynamicBean.spinnerItemData("FVC IN"),
            )

            // 创建一个空的 beans 列表
            val beansFVC: MutableList<CPETParameter> = mutableListOf()

            // 遍历 dynamicBeans 数组，将每个 DynamicBean 对象添加到 beans 列表中
            for (bean in fvcBeans) {
                bean.let { beansFVC.add(it) }
            }

            adapterFvc.setItemsBean(beansFVC)

        } else {
            adapterFvc.setItemsBean(staticSettingBean.settingFVC)
        }
        binding.rvFvc.adapter = adapterFvc

        if (isMvvBean) {
            // 创建一个存储 DynamicBean 对象的数组
            val mvvBeans = arrayOf(
                DynamicBean.spinnerItemData("MVV"),
                DynamicBean.spinnerItemData("TIME_MVV"),
                DynamicBean.spinnerItemData("BF"),
                DynamicBean.spinnerItemData("Time"),
            )

            // 创建一个空的 beans 列表
            val beansMVV: MutableList<CPETParameter> = mutableListOf()

            // 遍历 dynamicBeans 数组，将每个 DynamicBean 对象添加到 beans 列表中
            for (bean in mvvBeans) {
                bean.let { beansMVV.add(it) }
            }
            adapterMvv.setItemsBean(beansMVV)
        } else {
            adapterMvv.setItemsBean(staticSettingBean.settingMVV)
        }
        binding.rvMvv.adapter = adapterMvv
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

        LiveDataBus.get().with(settingsAreSaved).observe(this) {

            staticSettingBean.settingSVC.clear()
            staticSettingBean.settingFVC.clear()
            staticSettingBean.settingMVV.clear()

            staticSettingBean.xTimeSvc = binding.editXTimeSvc.text.toString()
            staticSettingBean.yTimeUpSvc = binding.editYTimeUpSvc.text.toString()
            staticSettingBean.yTimeDownSvc = binding.editYTimeDownSvc.text.toString()

            staticSettingBean.xTimeFvc = binding.editXTimeFvc.text.toString()
            staticSettingBean.yTimeUpFvc = binding.editYTimeUpFvc.text.toString()
            staticSettingBean.yTimeDownFvc = binding.editYTimeDownFvc.text.toString()

            staticSettingBean.xTimeMvv = binding.editXTimeMvv.text.toString()
            staticSettingBean.yTimeUpMvv = binding.editYTimeUpMvv.text.toString()
            staticSettingBean.yTimeDownMvv = binding.editYTimeDownMvv.text.toString()

            val svcItems = adapterSvc.retrieveItems()
            LogUtils.e(tag+adapterSvc.retrieveItems())
            staticSettingBean.settingSVC = svcItems.toMutableList()
            staticSettingBean.settingFVC.addAll(adapterFvc.items)
            staticSettingBean.settingMVV.addAll(adapterMvv.items)

            viewModel.setStaticSettingBean(staticSettingBean)
            toast("保存成功！")
        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FrgamentStaticSettingBinding.inflate(inflater, container, false)
}