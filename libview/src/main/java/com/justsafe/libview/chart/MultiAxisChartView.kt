package com.justsafe.libview.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Shader
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.justsafe.libview.R
import org.xclcharts.chart.AreaChart
import org.xclcharts.chart.AreaData
import org.xclcharts.chart.LineChart
import org.xclcharts.chart.LineData
import org.xclcharts.chart.PieChart
import org.xclcharts.chart.PieData
import org.xclcharts.chart.PointD
import org.xclcharts.chart.SplineChart
import org.xclcharts.chart.SplineData
import org.xclcharts.common.DensityUtil
import org.xclcharts.renderer.XEnum
import java.util.LinkedList
import kotlin.math.abs
import kotlin.math.min

class MultiAxisChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : DemoView(context, attrs, defStyle) {

    private val tag = MultiAxisChartView::class.java.name

    //用来显示面积图，左边及底部的轴
    private val chart = AreaChart()

    //标签集合
    private val mLabels = LinkedList<String>()

    //数据集合
    private val mDataset = LinkedList<AreaData>()

    //用来显示折线,右边及顶部的轴
    private val chartLn = LineChart()
    private val chartData = LinkedList<LineData>()

    //曲线图，用来显示最两边的两条竖轴
    private val chartLnAxes = SplineChart()
    private val chartDataAxes = LinkedList<SplineData>()
    private val mLabelsAxes = LinkedList<String>()

    //饼图
    private val chartPie = PieChart()
    private val chartDataPie = LinkedList<PieData>()

    private var touchX = -1f

    init {
        initView()
    }

    private fun initView() {
        chartLabels()

        chartLabelsAxes()//左1轴

        chartDataSetPie()

        chartDataSetLn()
        chartDataSetAxes()

        chartRender()//左边2轴

        chartRenderLn()//右边1轴

        chartRenderLnAxes()//右边2轴,左1轴

//		chartRenderPie();//饼图

//		this.bindTouch(this,chart);//綁定手势滑动事件

//		this.bindTouch(this,chartLn);//綁定手势滑动事件

//		 this.bindTouch(this,chartLnAxes);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //图所占范围大小
        chart.setChartRange(w.toFloat(), h.toFloat())
        chartLn.setChartRange(w.toFloat(), h.toFloat())
        chartLnAxes.setChartRange(w.toFloat(), h.toFloat())

        val left = DensityUtil.dip2px(context, 42f).toFloat()
        val top = DensityUtil.dip2px(context, 62f).toFloat()

        val pieWidth = min(w.toDouble(), h.toDouble()).toFloat() / 4 //1.5f;

        chartPie.setChartRange(left, top, pieWidth, pieWidth)
    }

    private fun chartRender() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            val ltrb = barLnDefaultSpadding
            //chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
            val left = DensityUtil.dip2px(context, 40f).toFloat() //left 40
            val right = DensityUtil.dip2px(context, 40f).toFloat() //right	20
            chart.setPadding(left, ltrb[1].toFloat(), right, ltrb[3].toFloat()) //ltrb[2]

            //轴数据源
            //标签轴
            chart.setCategories(mLabels)
            //数据轴
            chart.dataSource = mDataset
            //仅横向平移
            chart.plotPanMode = XEnum.PanMode.HORIZONTAL

            //数据轴最大值
            chart.dataAxis.setAxisMax(300.0)
            chart.dataAxis.setAxisMin(0.0)
            //数据轴刻度间隔
            chart.dataAxis.axisSteps = 50.0

            //网格
//            chart.getPlotGrid().showHorizontalLines();
//            chart.getPlotGrid().showVerticalLines();

            // 网格
            chart.plotGrid.hideVerticalLines() // 隐藏竖直方向的网格线
            chart.plotGrid.showHorizontalLines() // 显示水平方向的网格线


            //把轴线和刻度线给隐藏起来
            //chart.getDataAxis().hideAxisLine();
            chart.dataAxis.hideTickMarks()
            //	chart.getCategoryAxis().hideAxisLine();
            chart.categoryAxis.hideTickMarks()

            chart.dataAxis.tickLabelRotateAngle = -45f
            chart.dataAxis.tickLabelPaint.color = Color.RED
            chart.categoryAxis.tickLabelPaint.color = Color.RED

            //标题
//				chart.setTitle("混合图(区域、折线、饼图)");
//				chart.addSubtitle("(XCL-Charts Demo)");
            //轴标题
            //chart.getAxisTitle().setLowerAxisTitle("(时间点)");

            //透明度
            chart.setAreaAlpha(0)
            //显示图例
            chart.plotLegend.hide()

            //把轴线设成和横向网络线一样和大小和颜色,演示下定制性，这块问得人较多
            val plot = chart.plotGrid
            chart.dataAxis.axisPaint.strokeWidth = plot.horizontalLinePaint.strokeWidth
            chart.categoryAxis.axisPaint.strokeWidth = plot.horizontalLinePaint.strokeWidth

            chart.dataAxis.axisPaint.color = plot.horizontalLinePaint.color
            chart.categoryAxis.axisPaint.color = plot.horizontalLinePaint.color

            chart.dataAxis.tickMarksPaint.color = plot.horizontalLinePaint.color
            chart.categoryAxis.tickMarksPaint.color = plot.horizontalLinePaint.color

            plot.hideHorizontalLines()


            //激活点击监听
            chart.ActiveListenItemClick()
            //为了让触发更灵敏，可以扩大5px的点击监听范围
            chart.extPointClickRange(10)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(tag, e.toString())
        }
    }

    val dataSeries2: MutableList<Double> = LinkedList()

    /**
    * @param dataLine2 左边y轴2轴数据
    * */
    fun chartDataSet(dataLine2: Double) {

        val line2 = AreaData(
            "小小熊", dataSeries2, Color.RED, Color.YELLOW //(int)Color.rgb(254, 170, 50)
        )
        //设置线上每点对应标签的颜色
        line2.dotLabelPaint.color = Color.rgb(83, 148, 235)
        //设置点标签
        line2.labelVisible = true
        line2.labelOptions.setLabelBoxStyle(XEnum.LabelBoxStyle.CAPRECT)
        line2.labelOptions.setOffsetY(20f)

        line2.applayGradient = true
        line2.gradientMode = Shader.TileMode.MIRROR
        line2.areaBeginColor = Color.WHITE //Color.rgb(254, 170, 50));
        line2.areaEndColor = Color.rgb(224, 65, 10)


        line2.dotStyle = XEnum.DotStyle.RING
        line2.plotLine.dotPaint.color = Color.WHITE
        line2.plotLine.plotDot.ringInnerColor = Color.RED

        mDataset.add(line2)
    }

    private fun chartRenderLn() {
        try {
            //设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....

            val ltrb = barLnDefaultSpadding

            val left = DensityUtil.dip2px(context, 40f).toFloat() //left 40
            val right = DensityUtil.dip2px(context, 40f).toFloat() //right	20
            chartLn.setPadding(left, ltrb[1].toFloat(), right, ltrb[3].toFloat()) //ltrb[2]


            //设定数据源
            chartLn.setCategories(mLabels)
            chartLn.dataSource = chartData

            //数据轴最大值
            chartLn.dataAxis.setAxisMax(180.0)
            //数据轴刻度间隔
            chartLn.dataAxis.axisSteps = 10.0

            //仅横向平移
            chartLn.plotPanMode = XEnum.PanMode.HORIZONTAL

            //背景网格
            chartLn.plotGrid.hideEvenRowBgColor()
            chartLn.plotGrid.hideHorizontalLines()
            chartLn.plotGrid.hideOddRowBgColor()
            chartLn.plotGrid.hideVerticalLines()

            //chartLn.getPlotGrid().showHorizontalLines();

            //chartLn.getDataAxis().hideAxisLine();
            chartLn.dataAxis.hideTickMarks()
            //chartLn.getCategoryAxis().hideAxisLine();
            chartLn.categoryAxis.hideTickMarks()

            chartLn.dataAxis.tickLabelRotateAngle = -45f
            chartLn.dataAxis.tickLabelPaint.color = Color.rgb(106, 218, 92)
            chartLn.categoryAxis.tickLabelPaint.color = Color.rgb(106, 218, 92)
            chartLn.dataAxis.horizontalTickAlign = Paint.Align.RIGHT
            chartLn.dataAxis.tickLabelPaint.textAlign = Paint.Align.LEFT

            //调整轴显示位置
            chartLn.dataAxisLocation = XEnum.AxisLocation.RIGHT
            chartLn.categoryAxisLocation = XEnum.AxisLocation.TOP


            //把轴线设成和横向网络线一样和大小和颜色,演示下定制性，这块问得人较多
            val plot = chart.plotGrid
            chartLn.dataAxis.axisPaint.strokeWidth = plot.horizontalLinePaint.strokeWidth
            chartLn.categoryAxis.axisPaint.strokeWidth = plot.horizontalLinePaint.strokeWidth

            chartLn.dataAxis.axisPaint.color = plot.horizontalLinePaint.color
            chartLn.categoryAxis.axisPaint.color = plot.horizontalLinePaint.color

            chartLn.dataAxis.tickMarksPaint.color = plot.horizontalLinePaint.color
            chartLn.categoryAxis.tickMarksPaint.color = plot.horizontalLinePaint.color

            //图例显示在正下方
            chartLn.plotLegend.verticalAlign = XEnum.VerticalAlign.BOTTOM
            chartLn.plotLegend.horizontalAlign = XEnum.HorizontalAlign.CENTER
        } catch (e: Exception) {
            Log.e(tag, e.toString())
        }
    }


    private fun chartDataSetLn() {
        val dataSeries0 = LinkedList<Double>()
        dataSeries0.add(0.0)
        dataSeries0.add(1.0)
        dataSeries0.add(2.0)
        dataSeries0.add(3.0)
        dataSeries0.add(4.0)
        dataSeries0.add(5.0)
        dataSeries0.add(6.0)
        val line2 = LineData(
            "Area圆环", dataSeries0, ContextCompat.getColor(context, R.color.white)
        )
        line2.dotStyle = XEnum.DotStyle.RING
        line2.plotLine.dotPaint.color = Color.WHITE
        line2.plotLine.plotDot.ringInnerColor = Color.RED
        line2.labelOptions.hideBorder() //不显示标签边框


        //Line 1
        val dataSeries1 = LinkedList<Double>()
        dataSeries1.add(40.0)
        dataSeries1.add(35.0)
        dataSeries1.add(50.0)
        dataSeries1.add(60.0)
        dataSeries1.add(55.0)
        dataSeries1.add(55.0)
        dataSeries1.add(55.0)
        val lineData1 = LineData(
            "棱形", dataSeries1, ContextCompat.getColor(context, R.color.blue)
        )
        lineData1.labelVisible = false
        lineData1.dotStyle = XEnum.DotStyle.PRISMATIC

        lineData1.dotLabelPaint.color = Color.BLUE
        lineData1.dotLabelPaint.textSize = 22f
        lineData1.dotLabelPaint.textAlign = Paint.Align.LEFT
        lineData1.itemLabelRotateAngle = 45f
        lineData1.labelOptions.setLabelBoxStyle(XEnum.LabelBoxStyle.RECT)

        //Line 2
        val dataSeries2 = LinkedList<Double>()
        dataSeries2.add(50.0)
        dataSeries2.add(42.0)
        dataSeries2.add(55.0)
        dataSeries2.add(65.0)
        dataSeries2.add(58.0)
        dataSeries2.add(58.0)
        dataSeries2.add(58.0)
        val lineData2 = LineData(
            "圆环", dataSeries2, ContextCompat.getColor(context, R.color.white)
        )
        lineData2.dotStyle = XEnum.DotStyle.RING
        lineData2.plotLine.dotPaint.color = Color.RED
        lineData2.labelVisible = false
        lineData2.plotLine.plotDot.ringInnerColor = Color.GREEN
        lineData2.labelOptions.setLabelBoxStyle(XEnum.LabelBoxStyle.CAPRECT)


        //lineData2.setLineStyle(XEnum.LineStyle.DASH);
        val dataSeries3 = LinkedList<Double>()
        dataSeries3.add(55.0)
        dataSeries3.add(42.0)
        dataSeries3.add(65.0)
        dataSeries3.add(45.0)
        dataSeries3.add(45.0)
        dataSeries3.add(45.0)
        dataSeries3.add(45.0)
        val lineData3 = LineData(
            "角", dataSeries3, ContextCompat.getColor(context, R.color.colorAccent)
        )
        lineData3.dotStyle = XEnum.DotStyle.TRIANGLE
        lineData3.labelOptions.setLabelBoxStyle(XEnum.LabelBoxStyle.TEXT)

        chartData.add(line2)
        chartData.add(lineData1)
        chartData.add(lineData2)
        chartData.add(lineData3)
    }


    private fun chartRenderLnAxes() {
        try {
            // 设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....
            val ltrb = barLnDefaultSpadding

            val left = DensityUtil.dip2px(context, 20f).toFloat() // left 40
            val right = DensityUtil.dip2px(context, 20f).toFloat() // right 20
            chartLnAxes.setPadding(left, ltrb[1].toFloat(), right, ltrb[3].toFloat()) // ltrb[2]

            // 设定数据源
            chartLnAxes.setCategories(mLabelsAxes)
            chartLnAxes.dataSource = chartDataAxes

            // 数据轴最大值
            chartLnAxes.dataAxis.setAxisMax(100.0)
            // 数据轴刻度间隔
            chartLnAxes.dataAxis.axisSteps = 10.0

            // 标签轴最大值
            chartLnAxes.setCategoryAxisMax(50.0)
            // 标签轴最小值
            chartLnAxes.setCategoryAxisMin(0.0)

            chartLnAxes.plotLegend.hide()

            // 背景网格
            chartLnAxes.plotGrid.hideEvenRowBgColor()
            chartLnAxes.plotGrid.hideHorizontalLines()
            chartLnAxes.plotGrid.hideOddRowBgColor()
            chartLnAxes.plotGrid.hideVerticalLines()

            chartLnAxes.dataAxis.hideAxisLine()
            chartLnAxes.dataAxis.hideTickMarks()

            chartLnAxes.categoryAxis.hideAxisLine()
            chartLnAxes.categoryAxis.hideTickMarks()
            chartLnAxes.categoryAxis.tickLabelRotateAngle = -45f

            chartLnAxes.dataAxis.tickLabelRotateAngle = -45f
            chartLnAxes.dataAxis.tickLabelPaint.color = Color.rgb(48, 145, 255)

            chartLnAxes.categoryAxis.tickLabelPaint.color = Color.rgb(199, 64, 219)
            chartLnAxes.dataAxis.horizontalTickAlign = Paint.Align.RIGHT
            chartLnAxes.dataAxis.tickLabelPaint.textAlign = Paint.Align.LEFT

            // 调整轴显示位置
            chartLnAxes.dataAxisLocation = XEnum.AxisLocation.RIGHT
            chartLnAxes.categoryAxisLocation = XEnum.AxisLocation.LEFT
        } catch (e: Exception) {
            Log.e(tag, e.toString())
        }
    }

    private fun chartLabelsAxes() {
        mLabelsAxes.add("0")
        mLabelsAxes.add("10")
        mLabelsAxes.add("20")
        mLabelsAxes.add("30")
        mLabelsAxes.add("40")
        mLabelsAxes.add("50")
        mLabelsAxes.add("60")
        mLabelsAxes.add("70")
        mLabelsAxes.add("80")
    }

    private fun chartDataSetAxes() {
        // 线1的数据集
        val linePoint1: MutableList<PointD> = ArrayList()
        linePoint1.add(PointD(0.0, 0.0))
        linePoint1.add(PointD(1.0, 10.0))
        linePoint1.add(PointD(2.0, 20.0))
        linePoint1.add(PointD(3.0, 70.0))

        val dataSeries1 = SplineData("", linePoint1, Color.rgb(54, 141, 238))
        dataSeries1.dotStyle = XEnum.DotStyle.RING // 设置点的样式为圆环

        // 设定数据源
        chartDataAxes.add(dataSeries1)
    }


    private fun chartLabels() { //X轴数据
        for (i in 1..10) {
            mLabels.add("$i'")
        }
    }

    private fun chartDataSetPie() {
        chartDataPie.add(PieData("closed", "25%", 25.0, Color.rgb(155, 187, 90)))
        chartDataPie.add(PieData("inspect", "45%", 45.0, Color.rgb(191, 79, 75)))
        chartDataPie.add(PieData("workdone", "15%", 15.0, Color.rgb(60, 173, 213)))
        chartDataPie.add(PieData("dispute", "15%", 15.0, Color.rgb(90, 79, 88)))
    }

    override fun render(canvas: Canvas) {
        try {
            chart.render(canvas)
            chartLn.render(canvas)
            chartLnAxes.render(canvas)
            chartPie.render(canvas)

            drawVerticalLineAndYValues(canvas) // 绘制垂直线和Y值
        } catch (e: Exception) {
            Log.e(tag, e.toString())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                touchX = event.x
                invalidate() // 请求重绘视图
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun drawVerticalLineAndYValues(canvas: Canvas) {
        if (touchX != -1f) {
            val linePaint = Paint()
            linePaint.color = Color.RED
            linePaint.strokeWidth = 2f
            canvas.drawLine(touchX, 0f, touchX, height.toFloat(), linePaint)

            // 获取所有折线图数据集合
            for (lineData in chartData) {
                val dataPoints = lineData.linePoint
                val dataCount = dataPoints.size
                for (i in 0 until dataCount) {
                    // 计算每个数据点的X坐标
                    val x = chartLn.plotArea.left + (i * chartLn.plotArea.width / (dataCount - 1))
                    // 计算每个数据点的Y坐标
                    val value = dataPoints[i]
                    val y =
                        chartLn.plotArea.bottom - (value * chartLn.plotArea.height / chartLn.dataAxis.axisMax).toFloat()

                    // 如果触摸点接近某个数据点的X坐标，就显示其Y值，并标识数据线的名称
                    if (abs((touchX - x).toDouble()) < 10) {
                        val textPaint = Paint()
                        textPaint.color = Color.BLACK
                        textPaint.textSize = 20f
                        canvas.drawText(value.toString(), touchX, y, textPaint)
                        Log.d(
                            tag,
                            "Draw Y value for " + lineData.lineKey + ": " + value + " at X: " + touchX + ", Y: " + y
                        )
                    }
                }
            }
        }
    }
}