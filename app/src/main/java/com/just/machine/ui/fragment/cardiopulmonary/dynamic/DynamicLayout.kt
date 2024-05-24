package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.model.lungdata.CPXSerializeData
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

    fun setDynamicData(cpxData: CPXBreathInOutData? = CPXBreathInOutData()) {

        val cpxSerializeData = CPXSerializeData()

        val spinnerItems = listOfNotNull(
            DynamicBean.spinnerItemData("BF").apply { lowRange = cpxData?.BF },
            DynamicBean.spinnerItemData("BR").apply { lowRange = cpxData?.VTex },
            DynamicBean.spinnerItemData("Cho").apply { lowRange = cpxData?.CHO },
            DynamicBean.spinnerItemData("EE").apply { lowRange = cpxData?.EE },
            DynamicBean.spinnerItemData("FAT").apply { lowRange = cpxData?.FAT },
            DynamicBean.spinnerItemData("FeCO2").apply { lowRange = cpxData?.FeCO2 },
            DynamicBean.spinnerItemData("FeO2").apply { lowRange = cpxData?.FeCO2 },
            DynamicBean.spinnerItemData("FeiTCO2").apply { lowRange = cpxData?.FeCO2 },
            DynamicBean.spinnerItemData("FeTO2").apply { lowRange = cpxData?.FeCO2 },
            DynamicBean.spinnerItemData("FiCO2").apply { lowRange = cpxData?.FeCO2 },
            DynamicBean.spinnerItemData("FiO2").apply { lowRange = cpxData?.FeCO2 },
            DynamicBean.spinnerItemData("Grade")
                .apply { lowRange = cpxSerializeData.Grade },
            DynamicBean.spinnerItemData("HR").apply { lowRange = cpxSerializeData.HR },
            DynamicBean.spinnerItemData("HRR").apply { lowRange = cpxSerializeData.hrr },
            DynamicBean.spinnerItemData("Load")
                .apply { lowRange = cpxSerializeData.Load },
            DynamicBean.spinnerItemData("METS").apply { lowRange = cpxData?.METS },
            DynamicBean.spinnerItemData("PaCO2").apply { lowRange = cpxData?.PaCO2 },
            DynamicBean.spinnerItemData("PaO2").apply { lowRange = cpxData?.PaO2 },
//            DynamicBean.spinnerItemData("Pdia").apply { lowRange = cpxData?.Pdia },
            DynamicBean.spinnerItemData("PeTO2").apply { lowRange = cpxData?.PeTO2 },
            DynamicBean.spinnerItemData("PiCO2").apply { lowRange = cpxData?.PiCO2 },
            DynamicBean.spinnerItemData("PiO2").apply { lowRange = cpxData?.PiO2 },
            DynamicBean.spinnerItemData("PROT").apply { lowRange = cpxData?.PROT },
            DynamicBean.spinnerItemData("Psys").apply { lowRange = cpxSerializeData?.Psys },
            DynamicBean.spinnerItemData("Qtc").apply { lowRange = cpxData?.Qtc },
            DynamicBean.spinnerItemData("RER").apply { lowRange = cpxData?.RER },
//            DynamicBean.spinnerItemData("RPE").apply { lowRange = cpxData?.cpxSerializeData.RPE },
//            DynamicBean.spinnerItemData("RPM").apply { lowRange = cpxData?.RPM },
            DynamicBean.spinnerItemData("Speed").apply { lowRange = cpxSerializeData.Speed },
            DynamicBean.spinnerItemData("SPO2").apply { lowRange =
                cpxSerializeData.SPO2.toDouble()
            },
            DynamicBean.spinnerItemData("SVc").apply { lowRange = cpxData?.SVc },
//            DynamicBean.spinnerItemData("Tin").apply { lowRange = cpxData?.Tin },
//            DynamicBean.spinnerItemData("Tex").apply { lowRange = cpxData?.Tex },
            DynamicBean.spinnerItemData("VTex").apply { lowRange = cpxData?.VTex },
            DynamicBean.spinnerItemData("VTin").apply { lowRange = cpxData?.VTin },
            DynamicBean.spinnerItemData("T TOT").apply { lowRange = cpxData?.Tin_div_Ttot },
            DynamicBean.spinnerItemData("VCO2").apply { lowRange = cpxData?.VCO2 },
            DynamicBean.spinnerItemData("VD").apply { lowRange = cpxData?.VD },
            DynamicBean.spinnerItemData("VD/VT").apply { lowRange = cpxData?.VD_div_VT },
            DynamicBean.spinnerItemData("VdCO2").apply { lowRange = cpxData?.VdCO2 },
            DynamicBean.spinnerItemData("VdO2").apply { lowRange = cpxData?.VdO2 },
            DynamicBean.spinnerItemData("VE").apply { lowRange = cpxData?.VE },
            DynamicBean.spinnerItemData("VE/VCO2").apply { lowRange = cpxData?.VE_div_VCO2 },
            DynamicBean.spinnerItemData("VE/VO2").apply { lowRange = cpxData?.VE_div_VO2 },
            DynamicBean.spinnerItemData("VI").apply { lowRange = cpxData?.VI },
            DynamicBean.spinnerItemData("VO2").apply { lowRange = cpxData?.VO2 },
            DynamicBean.spinnerItemData("VO2/HR").apply { lowRange = cpxData?.VO2_div_HR },
            DynamicBean.spinnerItemData("VO2/KG").apply { lowRange = cpxData?.VO2_div_KG },
        ).filter {
            it.lowRange != null
        }

        // 确保 HR 是 adapterVo 的第一个数据
        val hrItem = DynamicBean.spinnerItemData("HR").apply { lowRange = cpxSerializeData.HR }
        val spinnerItemsForVo = listOfNotNull(hrItem) + spinnerItems.filter { it.parameterName != "HR" }

        adapterTime.updateSpinnerText(spinnerItems.toMutableList())

        adapterVo.updateSpinnerText(spinnerItems.toMutableList())//测试一下

    }

}