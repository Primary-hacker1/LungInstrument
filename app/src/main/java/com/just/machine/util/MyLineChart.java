package com.just.machine.util;

import android.content.Context;
import android.util.AttributeSet;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

public class MyLineChart extends LineChart {

    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void init() {
        super.init();
        //mRenderer 渲染器 设置为自己定义的渲染器
        mRenderer = new MyLineChartRenderer(this, mAnimator, mViewPortHandler);
    }
    @Override
    public LineData getLineData() {
        return mData;
    }

    @Override
    protected void onDetachedFromWindow() {
        // releases the bitmap in the renderer to avoid oom error
        if (mRenderer != null && mRenderer instanceof MyLineChartRenderer) {
            ((MyLineChartRenderer) mRenderer).releaseBitmap();
        }
        super.onDetachedFromWindow();
    }
}
