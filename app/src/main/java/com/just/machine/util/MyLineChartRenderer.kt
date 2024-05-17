package com.just.machine.util

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import kotlin.math.max

class MyLineChartRenderer : LineChartRenderer{
    constructor(
        chart: LineDataProvider,
        animator: ChartAnimator,
        viewPortHandler: ViewPortHandler
    ) : super(chart, animator, viewPortHandler){
        this.viewPortHandler = viewPortHandler
    }

    private var viewPortHandler: ViewPortHandler
    private var isHeart = false
    var pos:FloatArray = FloatArray(4) //颜色区间的位置

    var colors: IntArray = IntArray(8) //区间的颜色
    var range: IntArray = IntArray(3)

    private var mLineBuffer = FloatArray(4)
    override fun drawLinear(c: Canvas, dataSet: ILineDataSet) {
        val entryCount = dataSet.entryCount
        val isDrawSteppedEnabled = dataSet.mode == LineDataSet.Mode.STEPPED
        val pointsPerEntryPair = if (isDrawSteppedEnabled) 4 else 2

        val trans = mChart.getTransformer(dataSet.axisDependency)

        val phaseY = mAnimator.phaseY

        mRenderPaint.style = Paint.Style.STROKE

        var canvas: Canvas? = null

        // if the data-set is dashed, draw on bitmap-canvas

        // if the data-set is dashed, draw on bitmap-canvas
        canvas = if (dataSet.isDashedLineEnabled) {
            mBitmapCanvas
        } else {
            c
        }

        mXBounds[mChart] = dataSet

        // if drawing filled is enabled

        // if drawing filled is enabled
        if (dataSet.isDrawFilledEnabled && entryCount > 0) {
            drawLinearFill(c, dataSet, trans, mXBounds)
        }

        // more than 1 color

        // more than 1 color
        if (dataSet.colors.size > 1) {
            if (mLineBuffer.size <= pointsPerEntryPair * 2) mLineBuffer =
                FloatArray(pointsPerEntryPair * 4)
            for (j in mXBounds.min..mXBounds.range + mXBounds.min) {
                var e: Entry? = dataSet.getEntryForIndex(j) ?: continue
                mLineBuffer[0] = e!!.x
                mLineBuffer[1] = e.y * phaseY
                if (j < mXBounds.max) {
                    e = dataSet.getEntryForIndex(j + 1)
                    if (e == null) break
                    if (isDrawSteppedEnabled) {
                        mLineBuffer[2] = e.x
                        mLineBuffer[3] = mLineBuffer[1]
                        mLineBuffer[4] = mLineBuffer[2]
                        mLineBuffer[5] = mLineBuffer[3]
                        mLineBuffer[6] = e.x
                        mLineBuffer[7] = e.y * phaseY
                    } else {
                        mLineBuffer[2] = e.x
                        mLineBuffer[3] = e.y * phaseY
                    }
                } else {
                    mLineBuffer[2] = mLineBuffer[0]
                    mLineBuffer[3] = mLineBuffer[1]
                }
                trans.pointValuesToPixel(mLineBuffer)
                if (!mViewPortHandler.isInBoundsRight(mLineBuffer[0])) break

                // make sure the lines don't do shitty things outside
                // bounds
                if (!mViewPortHandler.isInBoundsLeft(mLineBuffer[2]) || !mViewPortHandler.isInBoundsTop(
                        mLineBuffer[1]
                    ) && !mViewPortHandler
                        .isInBoundsBottom(mLineBuffer[3])
                ) continue

                // get the color that is set for this line-segment
                mRenderPaint.color = dataSet.getColor(j)
                canvas!!.drawLines(mLineBuffer, 0, pointsPerEntryPair * 2, mRenderPaint)
            }
        } else { // only one color per dataset
            if (mLineBuffer.size < max(
                    entryCount * pointsPerEntryPair,
                    pointsPerEntryPair
                ) * 2
            ) mLineBuffer = FloatArray(
                max(
                    entryCount * pointsPerEntryPair, pointsPerEntryPair
                ) * 4
            )
            var e1: Entry?
            var e2: Entry?
            e1 = dataSet.getEntryForIndex(mXBounds.min)
            if (e1 != null) {
                var j = 0
                for (x in mXBounds.min..mXBounds.range + mXBounds.min) {
                    e1 = dataSet.getEntryForIndex(if (x == 0) 0 else x - 1)
                    e2 = dataSet.getEntryForIndex(x)
                    if (e1 == null || e2 == null) continue
                    mLineBuffer[j++] = e1.x
                    mLineBuffer[j++] = e1.y * phaseY
                    if (isDrawSteppedEnabled) {
                        mLineBuffer[j++] = e2.x
                        mLineBuffer[j++] = e1.y * phaseY
                        mLineBuffer[j++] = e2.x
                        mLineBuffer[j++] = e1.y * phaseY
                    }
                    mLineBuffer[j++] = e2.x
                    mLineBuffer[j++] = e2.y * phaseY
                }
                if (j > 0) {
                    trans.pointValuesToPixel(mLineBuffer)
                    val size = max((mXBounds.range + 1) * pointsPerEntryPair, pointsPerEntryPair) * 2
                    mRenderPaint.color = dataSet.color
                    if(isHeart){
                        val linear = LinearGradient(0f, mViewPortHandler.contentRect.top, 0f, mViewPortHandler.contentRect.bottom, colors, pos, Shader.TileMode.CLAMP)
                        linear.setLocalMatrix(viewPortHandler.matrixTouch)
                        mRenderPaint.shader = linear
                    }
                    canvas!!.drawLines(mLineBuffer, 0, size, mRenderPaint)
                }
            }
        }

        mRenderPaint.pathEffect = null
    }
    fun setHeartLine(medium:Int, larger:Int, limit:Int, colors:IntArray) {
        this.isHeart = true
        range[0] = limit
        range[1] = larger
        range[2] = medium
        val pos = FloatArray(4)
        val Ymax = (mChart as LineChart).axisLeft.axisMaximum
        val Ymin = (mChart as LineChart).axisLeft.axisMinimum
        pos[0] = (Ymax - limit) / (Ymax - Ymin);
        pos[1] = (limit - larger) / (Ymax - Ymin) + pos[0]
        pos[2] = (larger - medium) / (Ymax - Ymin) + pos[1]
        pos[3] = 1f

        this.pos = FloatArray(pos.size*2)
        this.colors =IntArray(colors.size * 2)
        var index = 0
        pos.forEachIndexed { i, _ ->
            this.colors[index] = colors[i]
            this.colors[index + 1] = colors[i]
            if (i == 0) {
                this.pos[index] = 0f;
                this.pos[index + 1] = pos[i];
            } else {
                this.pos[index] = pos[i - 1];
                this.pos[index + 1] = pos[i];
            }
            index += 2
        }
    }
}