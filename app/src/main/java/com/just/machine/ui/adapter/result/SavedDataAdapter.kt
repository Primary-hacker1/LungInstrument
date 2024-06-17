package com.just.machine.ui.adapter.result

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.just.news.R

class SavedDataAdapter(
    private val context: Context,
    private val data: MutableList<String>,
//    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<SavedDataAdapter.ViewHolder>() {

    private var listener: SavedListener? = null

    private var selectedItem = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.savedItemTextView)
        val llItem: LinearLayout = itemView.findViewById(R.id.ll_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_saved_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item
        holder.itemView.setOnClickListener { listener?.onClick(item,position) }


        if (position == selectedItem) {
            holder.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf4f5fa))
        } else {
            holder.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }
    }

    fun setItemOnClickListener(listener: SavedListener) {
        this.listener = listener
    }

    /**
     * @param position 点击 item 的 position
     * */
    fun toggleItemBackground(position: Int) {
        selectedItem = if (selectedItem == position) {
            -1
        } else {
            position
        }
        notifyDataSetChanged()
    }

    interface SavedListener {
        fun onClick(item: String, position: Int)
    }

    fun dataClean() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size
}
