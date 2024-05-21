package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.model.lungdata.DynamicBean
import com.just.machine.ui.adapter.CustomSpinnerAdapter
import com.just.news.R
import com.just.news.databinding.DynamicLayoutBinding


class DynamicLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: DynamicLayoutBinding = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        R.layout.dynamic_layout,
        this,
        true
    )

    val adapterTime = CustomSpinnerAdapter(context)
    val adapterVo = CustomSpinnerAdapter(context)
    val adapterHr = CustomSpinnerAdapter(context)
    val adapterRer = CustomSpinnerAdapter(context)
    val adapterO2Hr = CustomSpinnerAdapter(context)
    val adapterFio2 = CustomSpinnerAdapter(context)
    val adapterVdVt = CustomSpinnerAdapter(context)
    val adapterTex = CustomSpinnerAdapter(context)
    val adapterFeo2 = CustomSpinnerAdapter(context)

    val adapterTph = CustomSpinnerAdapter(context)
    val adapterVCO2 = CustomSpinnerAdapter(context)
    val adapterHrr = CustomSpinnerAdapter(context)
    val adapterVe = CustomSpinnerAdapter(context)
    val adapterSpo2 = CustomSpinnerAdapter(context)
    val adapterFico2 = CustomSpinnerAdapter(context)
    val adapterBf = CustomSpinnerAdapter(context)
    val adapterVtex = CustomSpinnerAdapter(context)
    val adapterFeco2 = CustomSpinnerAdapter(context)

    init {
        initView()
    }

    private fun initView() {


        val color = ContextCompat.getColor(context, R.color.colorPrimary) // 获取颜色资源

        binding.imgTranquility.setColorFilter(color)
        binding.imgWarm.setColorFilter(color)
        binding.imgMotion.setColorFilter(color)
        binding.imgRecover.setColorFilter(color)

        binding.timeSpinner.adapter = adapterTime
        binding.voSpinner.adapter = adapterVo
        binding.hrSpinner.adapter = adapterHr
        binding.rerSpinner.adapter = adapterRer
        binding.o2hrSpinner.adapter = adapterO2Hr
        binding.fioSpinner.adapter = adapterFio2
        binding.vdSpinner.adapter = adapterVdVt
        binding.texSpinner.adapter = adapterTex
        binding.feoSpinner.adapter = adapterFeo2

        binding.phSpinner.adapter = adapterTph
        binding.vcoSpinner.adapter = adapterVCO2
        binding.hrrSpinner.adapter = adapterHrr
        binding.veSpinner.adapter = adapterVe
        binding.spoSpinner.adapter = adapterSpo2
        binding.ficoSpinner.adapter = adapterFico2
        binding.bfSpinner.adapter = adapterBf
        binding.vtexSpinner.adapter = adapterVtex
        binding.fecoSpinner.adapter = adapterFeco2

    }

    fun setDynamicData(cpxData: CPXBreathInOutData) {

        val svc= DynamicBean.spinnerItemData("SVC")
        svc.lowRange = cpxData.SVc

        val vTex= DynamicBean.spinnerItemData("VTex")
        vTex.lowRange = cpxData.VTex

        adapterTime.updateSpinnerText(mutableListOf(svc,vTex))

        adapterVo.updateSpinnerText(DynamicBean.spinnerItemDatas())//测试一下

    }

}