package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.just.machine.dao.lung.CPXBreathInOutData
import com.just.machine.model.CPETParameter
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

    val adapterLoad = CustomSpinnerAdapter(context)
    val adapterVO2HR = CustomSpinnerAdapter(context)
    val adapterVCO2 = CustomSpinnerAdapter(context)
    val adapterRER = CustomSpinnerAdapter(context)
    val adapterPdia = CustomSpinnerAdapter(context)
    val adapterSpo2 = CustomSpinnerAdapter(context)
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
        binding.hrSpinner.adapter = adapterHr
        binding.voSpinner.adapter = adapterRer
        binding.vo2kgSpinner.adapter = adapterO2Hr
        binding.psysSpinner.adapter = adapterFio2
        binding.bfSpinner.adapter = adapterVdVt
        binding.texSpinner.adapter = adapterTex
        binding.feoSpinner.adapter = adapterFeo2

        binding.loadSpinner.adapter = adapterLoad
        binding.vo2hrSpinner.adapter = adapterVO2HR
        binding.vco2Spinner.adapter = adapterVCO2
        binding.rerSpinner.adapter = adapterRER
        binding.pdiaSpinner.adapter = adapterPdia
        binding.spo2Spinner.adapter = adapterSpo2
        binding.tinSpinner.adapter = adapterBf
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
            DynamicBean.spinnerItemData("SPO2").apply {
                lowRange =
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

        val spinnerItemsForHr = createSpinnerItems("HR", cpxSerializeData, spinnerItems)
        val spinnerItemsForVo2 = createSpinnerItems("VO2", cpxSerializeData, spinnerItems)
        val spinnerItemsForLoad = createSpinnerItems("Load", cpxSerializeData, spinnerItems)
        val spinnerItemsForRer = createSpinnerItems("Rer", cpxSerializeData, spinnerItems)
        val spinnerItemsForVO2Hr = createSpinnerItems("VO2/HR", cpxSerializeData, spinnerItems)
        val spinnerItemsForFio2 = createSpinnerItems("Fio2", cpxSerializeData, spinnerItems)
        val spinnerItemsForVDVT = createSpinnerItems("VD/VT", cpxSerializeData, spinnerItems)


        val spinnerItemsForVCo2 = createSpinnerItems("VCO2", cpxSerializeData, spinnerItems)
        val spinnerItemsForRER = createSpinnerItems("RER", cpxSerializeData, spinnerItems)
        val spinnerItemsForSPO2 = createSpinnerItems("SPO2", cpxSerializeData, spinnerItems)
        val spinnerItemsForBF = createSpinnerItems("BF", cpxSerializeData, spinnerItems)
        val spinnerItemsForVTex = createSpinnerItems("VTex", cpxSerializeData, spinnerItems)

        adapterVo.updateSpinnerText(spinnerItemsForVo2.toMutableList())
        adapterHr.updateSpinnerText(spinnerItemsForHr.toMutableList())
        adapterRer.updateSpinnerText(spinnerItemsForRer.toMutableList())
        adapterO2Hr.updateSpinnerText(spinnerItemsForVO2Hr.toMutableList())
        adapterFio2.updateSpinnerText(spinnerItemsForFio2.toMutableList())
        adapterVdVt.updateSpinnerText(spinnerItemsForVDVT.toMutableList())

        adapterLoad.updateSpinnerText(spinnerItemsForLoad.toMutableList())
        adapterVCO2.updateSpinnerText(spinnerItemsForVCo2.toMutableList())
        adapterRER.updateSpinnerText(spinnerItemsForRER.toMutableList())
        adapterSpo2.updateSpinnerText(spinnerItemsForSPO2.toMutableList())
        adapterBf.updateSpinnerText(spinnerItemsForBF.toMutableList())
        adapterVtex.updateSpinnerText(spinnerItemsForVTex.toMutableList())

    }

    // 创建一个用于生成 SpinnerItemData 的函数
    private fun createSpinnerItems(
        parameter: String,
        cpxData: CPXSerializeData,
        items: List<CPETParameter>
    ): List<CPETParameter> {
        val newItem = DynamicBean.spinnerItemData(parameter).apply { lowRange = cpxData.HR }
        return listOfNotNull(newItem) + items.filter { it.parameterName != parameter }
    }

}