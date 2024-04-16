package com.just.machine.ui.fragment.cardiopulmonary.result

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.just.machine.model.DynamicResultBean
import com.just.machine.ui.adapter.ResultAdapter
import com.just.news.R
import com.just.news.databinding.FragmentResultBinding

class FragmentResultLayout : FrameLayout {

    private val tag = FragmentResultLayout::class.java.name

    var binding: FragmentResultBinding

    private var mContext: Context

    private var adapter: ResultAdapter? = null

    constructor(context: Context) : super(context) {
        mContext = context
        val layoutInflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(layoutInflater, getLayout(), this, true)
        initView()
    }

    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes) {
        mContext = context
        val layoutInflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(layoutInflater, getLayout(), this, true)
        initView()
    }

    constructor(context: Context, attributes: AttributeSet?, int: Int) : super(
        context,
        attributes,
        int
    ) {
        mContext = context
        val layoutInflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(layoutInflater, getLayout(), this, true)
        initView()
    }

    private fun initView() {
        adapter = ResultAdapter(context)
        adapter?.setItemsBean(
            mutableListOf(
                DynamicResultBean("Time", "2024-4-16"),
                DynamicResultBean("Load", "80"),
                DynamicResultBean("HR(1/min)", "2024-4-16"),
            )
        )
        binding.rvResultData.layoutManager = LinearLayoutManager(context)
        binding.rvResultData.adapter = adapter
    }


    private fun getLayout(): Int {
        return R.layout.fragment_result
    }
}
