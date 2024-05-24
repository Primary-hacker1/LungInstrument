package com.just.machine.ui.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentChildAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragments = mutableListOf<Fragment>()
    private val fragmentNames = mutableListOf<String>()

    fun addFragment(fragment: Fragment, fragmentName: String? = "fragment") {
        fragments.add(fragment)
        fragmentNames.add(fragmentName ?: "fragment")
        notifyItemInserted(fragments.size - 1)
    }

    fun removeFragment(position: Int) {
        if (position < fragments.size) {
            fragments.removeAt(position)
            fragmentNames.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateFragment(position: Int, newFragment: Fragment, newName: String? = "fragment") {
        if (position < fragments.size) {
            fragments[position] = newFragment
            fragmentNames[position] = newName ?: "fragment"
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getAllFragmentNames(): List<String> = fragmentNames
}





