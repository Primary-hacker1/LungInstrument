package com.just.machine.ui.fragment.cardiopulmonary.dynamic

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.data.Entry
import com.just.machine.model.staticlung.DynamicBean
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

    init {
        initView()
    }

    private fun initView() {
        // 创建折线图的样本数据
        val entries = arrayListOf<Entry>()
        for (index in 0..30) {
            entries.add(Entry(index.toFloat(), index.toFloat() / 6 - 3))
        }

        binding.chart.setLineDataSetData(
            binding.chart.flowDataSetList()
        )//设置数据

        binding.chart.setLineChartFlow(
            yAxisMinimum = -5f,
            yAxisMaximum = 5f,
            countMaxX = 30f,
            granularityY = 1f,
            granularityX = 1f,
            titleCentent = "动态肺常规"
        )

        binding.chart1.setLineDataSetData(
            binding.chart.flowDataSetList()
        )//设置数据

        binding.chart1.setLineChartFlow(
            yAxisMinimum = -5f,
            yAxisMaximum = 5f,
            countMaxX = 30f,
            granularityY = 1f,
            granularityX = 1f,
            titleCentent = "动态肺常规"
        )

        binding.chart.setDynamicDragLine()

        binding.chart1.setDynamicDragLine()

        val color = ContextCompat.getColor(context, R.color.colorPrimary) // 获取颜色资源

        binding.imgTranquility.setColorFilter(color)
        binding.imgWarm.setColorFilter(color)
        binding.imgMotion.setColorFilter(color)
        binding.imgRecover.setColorFilter(color)


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

        adapterTime.updateSpinnerText(DynamicBean.spinnerItemDatas())//测试一下
        adapterVo.updateSpinnerText(DynamicBean.spinnerItemDatas())//测试一下
        adapterHr.updateSpinnerText(DynamicBean.spinnerItemDatas())//测试一下
    }

}