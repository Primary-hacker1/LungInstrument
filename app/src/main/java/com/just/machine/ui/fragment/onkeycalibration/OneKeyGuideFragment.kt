package com.just.machine.ui.fragment.onkeycalibration

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import com.common.base.CommonBaseFragment
import com.just.news.R
import com.just.news.databinding.FragmentOnekeyGuideBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * 一键定标-导航
 */
@AndroidEntryPoint
class OneKeyGuideFragment : CommonBaseFragment<FragmentOnekeyGuideBinding>() {
    override fun loadData() {

    }

    override fun initView() {
        binding.tvOnekeyGuideContent.text = Html.fromHtml(getString(R.string.onekey_calibration_guide_content))
    }

    override fun initListener() {

    }

    override fun getViewBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentOnekeyGuideBinding.inflate(inflater, container, false)
}