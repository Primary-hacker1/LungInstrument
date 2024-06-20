package com.justsafe.libview.chart

import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import android.content.Context
import android.widget.TextView
import com.justsafe.libview.R

/*
* 我的标记视图
* */
class MyMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val tvContent: TextView = findViewById(R.id.tvContent)

    // 当 MarkerView 需要刷新内容时调用
    override fun refreshContent(e: Entry, highlight: Highlight) {
        tvContent.text = "X: ${e.x}, Y: ${e.y}"
        super.refreshContent(e, highlight)
    }

    // 控制 MarkerView 的位置，使其在数据点正上方显示而不覆盖数据点
    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}


