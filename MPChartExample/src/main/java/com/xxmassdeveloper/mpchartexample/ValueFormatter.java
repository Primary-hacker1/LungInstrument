package com.xxmassdeveloper.mpchartexample;//

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

public abstract class ValueFormatter implements IAxisValueFormatter, IValueFormatter {
    public ValueFormatter() {
    }

    /** @deprecated */
    @Deprecated
    public String getFormattedValue(float value, AxisBase axis) {
        return this.getFormattedValue(value);
    }

    /** @deprecated */
    @Deprecated
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return this.getFormattedValue(value);
    }

    public String getFormattedValue(float value) {
        return String.valueOf(value);
    }

    public String getAxisLabel(float value, AxisBase axis) {
        return this.getFormattedValue(value);
    }

    public String getBarLabel(BarEntry barEntry) {
        return this.getFormattedValue(barEntry.getY());
    }

    public String getBarStackedLabel(float value, BarEntry stackedEntry) {
        return this.getFormattedValue(value);
    }

    public String getPointLabel(Entry entry) {
        return this.getFormattedValue(entry.getY());
    }

    public String getPieLabel(float value, PieEntry pieEntry) {
        return this.getFormattedValue(value);
    }

    public String getRadarLabel(RadarEntry radarEntry) {
        return this.getFormattedValue(radarEntry.getY());
    }

    public String getBubbleLabel(BubbleEntry bubbleEntry) {
        return this.getFormattedValue(bubbleEntry.getSize());
    }

    public String getCandleLabel(CandleEntry candleEntry) {
        return this.getFormattedValue(candleEntry.getHigh());
    }

    public void setFormattedValue(String formattedValue) {
        // 这里是你的自定义逻辑
        // 你可以在这里处理格式化后的值

    }
}
