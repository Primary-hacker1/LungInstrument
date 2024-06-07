package com.just.machine.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.SeekBar
import android.widget.TextView
import com.just.news.R

/**
 * SeekBar移动时弹出对应的气泡加数字*/
@SuppressLint("StaticFieldLeak")
object SeekBarPopUtils {

    private var popWin: PopupWindow? = null
    private var clPopPar: FrameLayout? = null
    private var tvPopTxt: TextView? = null

    @SuppressLint("MissingInflatedId")
    fun showPop(context : Context, seekBar: SeekBar){
        popWin = PopupWindow()
        val mPopView = LayoutInflater.from(context).inflate(R.layout.item_popup_win,null,false)
        clPopPar = mPopView.findViewById(R.id.cl_pop_par)
        tvPopTxt = mPopView.findViewById(R.id.tv_pop_txt)

        popWin?.contentView = mPopView
        popWin?.height = dp2px(context,30)
        popWin?.width = seekBar.width
        popWin?.showAsDropDown(seekBar,0,-dp2px(context,40) - popWin!!.height)
    }


    fun move(context:Context,progress: Int,proStr:String,seekBar: SeekBar){
        if (clPopPar != null){
            val tvPopWidth = dp2px(context,60)
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                tvPopWidth, dp2px(context,30)
            )
//            params.startToStart = clPopPar!!.id
            params.marginStart = (seekBar.width - tvPopWidth)/100 * progress + tvPopWidth/3
            tvPopTxt?.layoutParams = params

            tvPopTxt?.text = proStr
        }
    }


    fun dismiss(){
        popWin?.dismiss()
        popWin = null
        clPopPar = null
        tvPopTxt = null
    }

    private fun dp2px(context:Context, dpVal: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpVal.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}