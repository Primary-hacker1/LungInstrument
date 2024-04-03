package com.just.machine.ui.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

//class FragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
//
//    private val fragments = mutableListOf<Fragment>()
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun addFragment(fragment: Fragment) {
//        fragments.add(fragment)
//        notifyDataSetChanged()
//    }
//
//    override fun getItemCount(): Int {
//        return fragments.size
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return fragments[position]
//    }
//}

class FragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val fragments: List<Fragment>) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}



