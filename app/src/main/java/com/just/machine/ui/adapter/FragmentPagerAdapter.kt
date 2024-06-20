package com.just.machine.ui.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragments = mutableListOf<Fragment>()

    // 添加 Fragment 的名称列表
    private val fragmentNames = mutableListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun addFragment(fragment: Fragment, fragmentName: String? = "fragment") {
        fragments.add(fragment)
        if (fragmentName != null) {
            fragmentNames.add(fragmentName)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    // 返回所有 Fragment 名称的列表
    fun getAllFragmentNames(): List<String> {
        return fragmentNames
    }
}





