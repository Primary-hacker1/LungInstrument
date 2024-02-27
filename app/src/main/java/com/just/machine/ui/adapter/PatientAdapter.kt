package com.just.machine.ui.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.common.base.setNoRepeatListener
import com.just.machine.model.PatientBean
import com.just.machine.ui.activity.PatientActivity
import com.just.news.R
import kotlinx.android.synthetic.main.item_layout_patient.view.ivLinesItemDelete

/**
 *create by 2024/2/27
 * 患者信息adapter
 *@author zt
 */
class PatientAdapter(private val dataList: MutableList<PatientBean>) :
    RecyclerView.Adapter<PatientAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_patient, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.itemView.ivLinesItemDelete.setNoRepeatListener {
            deleteItem(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun deleteItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    // RecyclerView ViewHolder
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(data: PatientBean) {
            itemView.findViewById<TextView>(R.id.tvLinesItemContent).text = data.content
            itemView.findViewById<ImageView>(R.id.ivLinesItemDelete)
        }
    }

    class SwipeToDeleteCallback(context: Context) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)
        private val intrinsicWidth = deleteIcon?.intrinsicWidth
        private val intrinsicHeight = deleteIcon?.intrinsicHeight
        private val background = ColorDrawable(Color.RED)

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            (viewHolder.itemView.context as PatientActivity)
                .adapter.deleteItem(viewHolder.adapterPosition)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView
            val itemHeight = itemView.bottom.toFloat() - itemView.top.toFloat()

            // Draw red background
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            background.draw(c)

            // Calculate position of delete icon
            val iconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
            val iconMargin = (itemHeight - intrinsicHeight) / 2
            val iconLeft = itemView.right - iconMargin - intrinsicWidth!!
            val iconRight = itemView.right - iconMargin
            val iconBottom = iconTop + intrinsicHeight

            // Draw delete icon
            deleteIcon?.setBounds(
                iconLeft.toInt(),
                iconTop.toInt(),
                iconRight.toInt(),
                iconBottom.toInt()
            )
            deleteIcon?.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
}