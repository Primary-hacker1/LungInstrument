package com.justsafe.libview.chart;

import static android.os.Build.VERSION_CODES.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.xclcharts.chart.AreaChart;
import org.xclcharts.chart.AreaData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.chart.PieChart;
import org.xclcharts.chart.PieData;
import org.xclcharts.chart.PointD;
import org.xclcharts.chart.SplineChart;
import org.xclcharts.chart.SplineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotGrid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

public class MultiAxisChart03View extends DemoView {
    private final String tag = "MultiAxisChart03View";

    private Context mContext;

    //用来显示面积图，左边及底部的轴
    private final AreaChart chart = new AreaChart();
    //标签集合
    private final LinkedList<String> mLabels = new LinkedList<>();
    //数据集合
    private final LinkedList<AreaData> mDataset = new LinkedList<>();

    //用来显示折线,右边及顶部的轴
    private final LineChart chartLn = new LineChart();
    private final LinkedList<LineData> chartData = new LinkedList<>();

    //曲线图，用来显示最两边的两条竖轴
    private final SplineChart chartLnAxes = new SplineChart();
    private final LinkedList<SplineData> chartDataAxes = new LinkedList<>();
    private final LinkedList<String> mLabelsAxes = new LinkedList<>();

    //饼图
    private final PieChart chartPie = new PieChart();
    private final LinkedList<PieData> chartDataPie = new LinkedList<>();

    private final Paint mPaintTooltips = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float touchX = -1;

    public MultiAxisChart03View(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public MultiAxisChart03View(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public MultiAxisChart03View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    private void initView() {
        chartLabels();
        chartLabelsAxes();
        chartDataSetPie();

        chartDataSet();
        chartDataSetLn();
        chartDataSetAxes();

        chartRender();
        chartRenderLn();
        chartRenderLnAxes();
//		chartRenderPie();//饼图

//		this.bindTouch(this,chart);//綁定手势滑动事件

//		this.bindTouch(this,chartLn);//綁定手势滑动事件

//		 this.bindTouch(this,chartLnAxes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w, h);
        chartLn.setChartRange(w, h);
        chartLnAxes.setChartRange(w, h);

        float left = DensityUtil.dip2px(getContext(), 42);
        float top = DensityUtil.dip2px(getContext(), 62);

        float piewidth = (float) Math.min(w, h) / 4;//1.5f;

        chartPie.setChartRange(left, top, piewidth, piewidth);
    }

    private void chartRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();
            //chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            float left = DensityUtil.dip2px(getContext(), 40); //left 40
            float right = DensityUtil.dip2px(getContext(), 40); //right	20
            chart.setPadding(left, ltrb[1], right, ltrb[3]);    //ltrb[2]

            //轴数据源
            //标签轴
            chart.setCategories(mLabels);
            //数据轴
            chart.setDataSource(mDataset);
            //仅横向平移
            chart.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

            //数据轴最大值
            chart.getDataAxis().setAxisMax(300);
            chart.getDataAxis().setAxisMin(0);
            //数据轴刻度间隔
            chart.getDataAxis().setAxisSteps(50);

            //网格
//            chart.getPlotGrid().showHorizontalLines();
//            chart.getPlotGrid().showVerticalLines();

            // 网格
            chart.getPlotGrid().hideVerticalLines();  // 隐藏竖直方向的网格线
            chart.getPlotGrid().showHorizontalLines();  // 显示水平方向的网格线


            //把轴线和刻度线给隐藏起来
            //chart.getDataAxis().hideAxisLine();
            chart.getDataAxis().hideTickMarks();
            //	chart.getCategoryAxis().hideAxisLine();
            chart.getCategoryAxis().hideTickMarks();

            chart.getDataAxis().setTickLabelRotateAngle(-45);
            chart.getDataAxis().getTickLabelPaint().setColor(Color.RED);
            chart.getCategoryAxis().getTickLabelPaint().setColor(Color.RED);

            //标题
//				chart.setTitle("混合图(区域、折线、饼图)");
//				chart.addSubtitle("(XCL-Charts Demo)");
            //轴标题
            //chart.getAxisTitle().setLowerAxisTitle("(时间点)");

            //透明度
            chart.setAreaAlpha(200);
            //显示图例
            chart.getPlotLegend().hide();

            //把轴线设成和横向网络线一样和大小和颜色,演示下定制性，这块问得人较多
            PlotGrid plot = chart.getPlotGrid();
            chart.getDataAxis().getAxisPaint().setStrokeWidth(
                    plot.getHorizontalLinePaint().getStrokeWidth());
            chart.getCategoryAxis().getAxisPaint().setStrokeWidth(
                    plot.getHorizontalLinePaint().getStrokeWidth());

            chart.getDataAxis().getAxisPaint().setColor(
                    plot.getHorizontalLinePaint().getColor());
            chart.getCategoryAxis().getAxisPaint().setColor(
                    plot.getHorizontalLinePaint().getColor());

            chart.getDataAxis().getTickMarksPaint().setColor(
                    plot.getHorizontalLinePaint().getColor());
            chart.getCategoryAxis().getTickMarksPaint().setColor(
                    plot.getHorizontalLinePaint().getColor());

            plot.hideHorizontalLines();


            //激活点击监听
            chart.ActiveListenItemClick();
            //为了让触发更灵敏，可以扩大5px的点击监听范围
            chart.extPointClickRange(10);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(tag, e.toString());
        }
    }

    private void chartDataSet() {

        List<Double> dataSeries2 = new LinkedList<>();
        dataSeries2.add((double) 140);  //40
        dataSeries2.add((double) 122);
        dataSeries2.add((double) 130);
        dataSeries2.add((double) 135);
        dataSeries2.add((double) 115); //15

        AreaData line2 = new AreaData("小小熊", dataSeries2,
                Color.RED,
                Color.YELLOW //(int)Color.rgb(254, 170, 50)
        );
        //设置线上每点对应标签的颜色
        line2.getDotLabelPaint().setColor(Color.rgb(83, 148, 235));
        //设置点标签
        line2.setLabelVisible(true);
        line2.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.CAPRECT);
        line2.getLabelOptions().setOffsetY(20.f);

        line2.setApplayGradient(true);
        line2.setGradientMode(Shader.TileMode.MIRROR);
        line2.setAreaBeginColor(Color.WHITE); //Color.rgb(254, 170, 50));
        line2.setAreaEndColor(Color.rgb(224, 65, 10));


        line2.setDotStyle(XEnum.DotStyle.RING);
        line2.getPlotLine().getDotPaint().setColor(Color.WHITE);
        line2.getPlotLine().getPlotDot().setRingInnerColor(Color.RED);

        mDataset.add(line2);
    }

    private void chartRenderLn() {
        try {

            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();

            float left = DensityUtil.dip2px(getContext(), 40); //left 40
            float right = DensityUtil.dip2px(getContext(), 40); //right	20
            chartLn.setPadding(left, ltrb[1], right, ltrb[3]);    //ltrb[2]


            //设定数据源
            chartLn.setCategories(mLabels);
            chartLn.setDataSource(chartData);

            //数据轴最大值
            chartLn.getDataAxis().setAxisMax(70);
            //数据轴刻度间隔
            chartLn.getDataAxis().setAxisSteps(10);

            //仅横向平移
            chartLn.setPlotPanMode(XEnum.PanMode.HORIZONTAL);

            //背景网格
            chartLn.getPlotGrid().hideEvenRowBgColor();
            chartLn.getPlotGrid().hideHorizontalLines();
            chartLn.getPlotGrid().hideOddRowBgColor();
            chartLn.getPlotGrid().hideVerticalLines();

            //chartLn.getPlotGrid().showHorizontalLines();

            //chartLn.getDataAxis().hideAxisLine();
            chartLn.getDataAxis().hideTickMarks();
            //chartLn.getCategoryAxis().hideAxisLine();
            chartLn.getCategoryAxis().hideTickMarks();

            chartLn.getDataAxis().setTickLabelRotateAngle(-45);
            chartLn.getDataAxis().getTickLabelPaint().setColor(Color.rgb(106, 218, 92));
            chartLn.getCategoryAxis().getTickLabelPaint().setColor(Color.rgb(106, 218, 92));
            chartLn.getDataAxis().setHorizontalTickAlign(Align.RIGHT);
            chartLn.getDataAxis().getTickLabelPaint().setTextAlign(Align.LEFT);

            //调整轴显示位置
            chartLn.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
            chartLn.setCategoryAxisLocation(XEnum.AxisLocation.TOP);


            //把轴线设成和横向网络线一样和大小和颜色,演示下定制性，这块问得人较多
            PlotGrid plot = chart.getPlotGrid();
            chartLn.getDataAxis().getAxisPaint().setStrokeWidth(
                    plot.getHorizontalLinePaint().getStrokeWidth());
            chartLn.getCategoryAxis().getAxisPaint().setStrokeWidth(
                    plot.getHorizontalLinePaint().getStrokeWidth());

            chartLn.getDataAxis().getAxisPaint().setColor(
                    plot.getHorizontalLinePaint().getColor());
            chartLn.getCategoryAxis().getAxisPaint().setColor(
                    plot.getHorizontalLinePaint().getColor());

            chartLn.getDataAxis().getTickMarksPaint().setColor(
                    plot.getHorizontalLinePaint().getColor());
            chartLn.getCategoryAxis().getTickMarksPaint().setColor(
                    plot.getHorizontalLinePaint().getColor());

            //图例显示在正下方
            chartLn.getPlotLegend().setVerticalAlign(XEnum.VerticalAlign.BOTTOM);
            chartLn.getPlotLegend().setHorizontalAlign(XEnum.HorizontalAlign.CENTER);

        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
    }


    private void chartDataSetLn() {

        LinkedList<Double> dataSeries0 = new LinkedList<Double>();
        dataSeries0.add(0d);
        dataSeries0.add(1d);
        dataSeries0.add(2d);
        dataSeries0.add(3d);
        dataSeries0.add(4d);
        dataSeries0.add(5d);
        dataSeries0.add(6d);
        LineData line2 = new LineData("Area圆环", dataSeries0,
                ContextCompat.getColor(mContext, com.justsafe.libview.R.color.white)); //(int)Color.rgb(48, 145, 255));
        line2.setDotStyle(XEnum.DotStyle.RING);
        line2.getPlotLine().getDotPaint().setColor(Color.WHITE);
        line2.getPlotLine().getPlotDot().setRingInnerColor(Color.RED);
        line2.getLabelOptions().hideBorder();//不显示标签边框


        //Line 1
        LinkedList<Double> dataSeries1 = new LinkedList<Double>();
        dataSeries1.add(40d);
        dataSeries1.add(35d);
        dataSeries1.add(50d);
        dataSeries1.add(60d);
        dataSeries1.add(55d);
        dataSeries1.add(55d);
        dataSeries1.add(55d);
        LineData lineData1 = new LineData("棱形", dataSeries1,
                ContextCompat.getColor(mContext, com.justsafe.libview.R.color.white));
        lineData1.setLabelVisible(false);
        lineData1.setDotStyle(XEnum.DotStyle.PRISMATIC);

        lineData1.getDotLabelPaint().setColor(Color.BLUE);
        lineData1.getDotLabelPaint().setTextSize(22);
        lineData1.getDotLabelPaint().setTextAlign(Align.LEFT);
        lineData1.setItemLabelRotateAngle(45.f);
        lineData1.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.RECT);

        //Line 2
        LinkedList<Double> dataSeries2 = new LinkedList<Double>();
        dataSeries2.add((double) 50);
        dataSeries2.add((double) 42);
        dataSeries2.add((double) 55);
        dataSeries2.add((double) 65);
        dataSeries2.add((double) 58);
        dataSeries2.add((double) 58);
        dataSeries2.add((double) 58);
        LineData lineData2 = new LineData("圆环", dataSeries2,
                ContextCompat.getColor(mContext, com.justsafe.libview.R.color.white));
        lineData2.setDotStyle(XEnum.DotStyle.RING);
        lineData2.getPlotLine().getDotPaint().setColor(Color.RED);
        lineData2.setLabelVisible(false);
        lineData2.getPlotLine().getPlotDot().setRingInnerColor(Color.GREEN);
        lineData2.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.CAPRECT);
        //lineData2.setLineStyle(XEnum.LineStyle.DASH);


        LinkedList<Double> dataSeries3 = new LinkedList<Double>();
        dataSeries3.add((double) 55);
        dataSeries3.add((double) 42);
        dataSeries3.add((double) 65);
        dataSeries3.add((double) 45);
        dataSeries3.add((double) 45);
        dataSeries3.add((double) 45);
        dataSeries3.add((double) 45);
        LineData lineData3 = new LineData("角", dataSeries3,
                ContextCompat.getColor(mContext, com.justsafe.libview.R.color.white));
        lineData3.setDotStyle(XEnum.DotStyle.TRIANGLE);
        lineData3.getLabelOptions().setLabelBoxStyle(XEnum.LabelBoxStyle.TEXT);

        chartData.add(line2);
        chartData.add(lineData1);
        chartData.add(lineData2);
        chartData.add(lineData3);
    }


    private void chartRenderLnAxes() {
        try {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            int[] ltrb = getBarLnDefaultSpadding();

            float left = DensityUtil.dip2px(getContext(), 20); // left 40
            float right = DensityUtil.dip2px(getContext(), 20); // right 20
            chartLnAxes.setPadding(left, ltrb[1], right, ltrb[3]); // ltrb[2]

            // 设定数据源
            chartLnAxes.setCategories(mLabelsAxes);
            chartLnAxes.setDataSource(chartDataAxes);

            // 数据轴最大值
            chartLnAxes.getDataAxis().setAxisMax(100);
            // 数据轴刻度间隔
            chartLnAxes.getDataAxis().setAxisSteps(10);

            // 标签轴最大值
            chartLnAxes.setCategoryAxisMax(70);
            // 标签轴最小值
            chartLnAxes.setCategoryAxisMin(0);

            chartLnAxes.getPlotLegend().hide();

            // 背景网格
            chartLnAxes.getPlotGrid().hideEvenRowBgColor();
            chartLnAxes.getPlotGrid().hideHorizontalLines();
            chartLnAxes.getPlotGrid().hideOddRowBgColor();
            chartLnAxes.getPlotGrid().hideVerticalLines();

            chartLnAxes.getDataAxis().hideAxisLine();
            chartLnAxes.getDataAxis().hideTickMarks();

            chartLnAxes.getCategoryAxis().hideAxisLine();
            chartLnAxes.getCategoryAxis().hideTickMarks();
            chartLnAxes.getCategoryAxis().setTickLabelRotateAngle(-45);

            chartLnAxes.getDataAxis().setTickLabelRotateAngle(-45);
            chartLnAxes.getDataAxis().getTickLabelPaint().setColor(Color.rgb(48, 145, 255));

            chartLnAxes.getCategoryAxis().getTickLabelPaint().setColor(Color.rgb(199, 64, 219));
            chartLnAxes.getDataAxis().setHorizontalTickAlign(Paint.Align.RIGHT);
            chartLnAxes.getDataAxis().getTickLabelPaint().setTextAlign(Paint.Align.LEFT);

            // 调整轴显示位置
            chartLnAxes.setDataAxisLocation(XEnum.AxisLocation.RIGHT);
            chartLnAxes.setCategoryAxisLocation(XEnum.AxisLocation.LEFT);

        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
    }

    private void chartLabelsAxes() {
        mLabelsAxes.add("0");
        mLabelsAxes.add("10");
        mLabelsAxes.add("20");
        mLabelsAxes.add("30");
        mLabelsAxes.add("40");
        mLabelsAxes.add("50");
        mLabelsAxes.add("60");
        mLabelsAxes.add("70");
    }

    private void chartDataSetAxes() {
        // 线1的数据集
        List<PointD> linePoint1 = new ArrayList<>();
        linePoint1.add(new PointD(0d, 0d));

        SplineData dataSeries1 = new SplineData("", linePoint1, Color.rgb(54, 141, 238));
        dataSeries1.setDotStyle(XEnum.DotStyle.RING); // 设置点的样式为圆环

        // 设定数据源
        chartDataAxes.add(dataSeries1);
    }


    private void chartLabels() {//X轴数据
        for (int i = 1; i <= 10; i++) {
            mLabels.add(i + "'");
        }
    }

    private void chartRenderPie() {
        chartPie.setPadding(0, 0, 0, 0);

        //标签显示(隐藏，显示在中间，显示在扇区外面)
        chartPie.setLabelStyle(XEnum.SliceLabelStyle.INSIDE);
        chartPie.getLabelPaint().setColor(Color.WHITE);

        chartPie.setDataSource(chartDataPie);

        //显示图例
        chartPie.getPlotLegend().hide();
    }

    private void chartDataSetPie() {
        chartDataPie.add(new PieData("closed", "25%", 25, Color.rgb(155, 187, 90)));
        chartDataPie.add(new PieData("inspect", "45%", 45, Color.rgb(191, 79, 75)));
        chartDataPie.add(new PieData("workdone", "15%", 15, Color.rgb(60, 173, 213)));
        chartDataPie.add(new PieData("dispute", "15%", 15, Color.rgb(90, 79, 88)));
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
            chartLn.render(canvas);
            chartLnAxes.render(canvas);
            chartPie.render(canvas);

            drawVerticalLineAndYValues(canvas); // 绘制垂直线和Y值
        } catch (Exception e) {
            Log.e(tag, e.toString());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                touchX = event.getX();
                invalidate(); // 请求重绘视图
                return true;
//            case MotionEvent.ACTION_UP:
//                touchX = -1;
//                triggerClick(event.getX(),event.getY());
//                invalidate(); // 请求重绘视图
//                return true;
        }
        return super.onTouchEvent(event);
    }

    //触发监听
    private void triggerClick(float x, float y) {
        PointPosition record = chart.getPositionRecord(x, y);
        if (null == record) return;

        AreaData lData = mDataset.get(record.getDataID());
        Double lValue = lData.getLinePoint().get(record.getDataChildID());

        //在点击处显示tooltip
        mPaintTooltips.setColor(Color.rgb(240, 73, 119));
        chart.getToolTip().getBackgroundPaint().setColor(Color.GREEN);
        chart.getToolTip().setCurrentXY(x, y);
        chart.getToolTip().addToolTip(" Key:" + lData.getLineKey(), mPaintTooltips);
        chart.getToolTip().addToolTip(" Label:" + lData.getLabel(), mPaintTooltips);
        chart.getToolTip().addToolTip(" Current Value:" + lValue, mPaintTooltips);
        chart.getToolTip().setAlign(Align.LEFT);
        this.invalidate();
    }

    private void drawVerticalLineAndYValues(Canvas canvas) {
        if (touchX != -1) {
            Paint linePaint = new Paint();
            linePaint.setColor(Color.RED);
            linePaint.setStrokeWidth(2);
            canvas.drawLine(touchX, 0, touchX, getHeight(), linePaint);

            // 获取所有折线图数据集合
            for (LineData lineData : chartData) {
                List<Double> dataPoints = lineData.getLinePoint();
                int dataCount = dataPoints.size();
                for (int i = 0; i < dataCount; i++) {
                    // 计算每个数据点的X坐标
                    float x = chartLn.getPlotArea().getLeft() + (i * chartLn.getPlotArea().getWidth() / (dataCount - 1));
                    // 计算每个数据点的Y坐标
                    double value = dataPoints.get(i);
                    float y = chartLn.getPlotArea().getBottom() - (float) (value * chartLn.getPlotArea().getHeight() / chartLn.getDataAxis().getAxisMax());

                    // 如果触摸点接近某个数据点的X坐标，就显示其Y值，并标识数据线的名称
                    if (Math.abs(touchX - x) < 10) {
                        Paint textPaint = new Paint();
                        textPaint.setColor(Color.BLACK);
                        textPaint.setTextSize(20);
                        canvas.drawText(String.valueOf(value), touchX, y, textPaint);
                        Log.d(tag, "Draw Y value for " + lineData.getLineKey() + ": " + value + " at X: " + touchX + ", Y: " + y);
                    }
                }
            }
        }
    }

}