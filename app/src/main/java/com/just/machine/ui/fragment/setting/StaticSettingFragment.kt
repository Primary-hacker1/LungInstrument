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
import com.just.machine.ui.adapter.setting.StaticSettingAdapter
import com.just.machine.ui.viewmodel.MainViewModel
import com.just.machine.util.LiveDataBus
import com.just.news.R
import com.just.news.databinding.FrgamentStaticSettingBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 *create by 2024/6/19
 * 静态肺设置
 *@author zt
 */
@AndroidEntryPoint
class StaticSettingFragment : CommonBaseFragment<FrgamentStaticSettingBinding>() {

    private val viewModel by viewModels<MainViewModel>()

    private var staticSettingBean: StaticSettingBean = StaticSettingBean()

    private val adapterSvc by lazy { StaticSettingAdapter(requireContext()) }

    private val adapterFvc by lazy { StaticSettingAdapter(requireContext()) }

    private val adapterMvv by lazy { StaticSettingAdapter(requireContext()) }
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

            // 创建一个空的 beans 列表
            val beansSVC: MutableList<CPETParameter> = mutableListOf()

            // 遍历 dynamicBeans 数组，将每个 DynamicBean 对象添加到 beans 列表中
            for (bean in viewModel.dynamicBeans) {
                bean.let { beansSVC.add(it) }
            }

            // 设置 adapterSvc 的数据
            adapterSvc.setItemsBean(beansSVC)
        } else {
            adapterSvc.setItemsBean(staticSettingBean.settingSVC)
        }

        binding.rvSvc.adapter = adapterSvc

        if (isFvcBean) {

            // 创建一个空的 beans 列表
            val beansFVC: MutableList<CPETParameter> = mutableListOf()

            // 遍历 dynamicBeans 数组，将每个 DynamicBean 对象添加到 beans 列表中
            for (bean in viewModel.fvcBeans) {
                bean.let { beansFVC.add(it) }
            }

            adapterFvc.setItemsBean(beansFVC)

        } else {
            adapterFvc.setItemsBean(staticSettingBean.settingFVC)
        }
        binding.rvFvc.adapter = adapterFvc

        if (isMvvBean) {

            // 创建一个空的 beans 列表
            val beansMVV: MutableList<CPETParameter> = mutableListOf()

            // 遍历 dynamicBeans 数组，将每个 DynamicBean 对象添加到 beans 列表中
            for (bean in viewModel.mvvBeans) {
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