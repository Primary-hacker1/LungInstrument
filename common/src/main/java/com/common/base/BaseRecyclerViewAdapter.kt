package com.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, VB : ViewDataBinding> :
    RecyclerView.Adapter<BaseRecyclerViewAdapter<T, VB>.ViewHolder>() {

    var items: List<T> = listOf()

    private var itemClickListener: ((T) -> Unit)? = null

    lateinit var binding: VB

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate<VB>(inflater, getLayoutRes(), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: VB) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            bindData(item)

            binding.root.setOnClickListener {
                itemClickListener?.invoke(item)
            }
            binding.executePendingBindings()
        }
    }

    abstract fun bindData(item: T)
    abstract fun getLayoutRes(): Int

    fun setItemsBean(items: List<T>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: (T) -> Unit) {
        this.itemClickListener = listener
    }
}

