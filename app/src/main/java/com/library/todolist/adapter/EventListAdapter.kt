package com.library.todolist.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.library.todolist.R
import com.library.todolist.TimeConverter
import com.library.todolist.realm.EventModelRealm

class EventListAdapter(private val context: Activity)  : AdapterConfiguration {
    override fun <T> getItemViewType(position: Int, data: T): Int = GenericAdapter.ITEM

    override fun getViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        Item(LayoutInflater.from(context).inflate(R.layout.event_item_recycle_view, parent, false))


    override fun <T> bindToHolder(
        holder: RecyclerView.ViewHolder,
        data: BaseRecylerViewModel,
        listData: ArrayList<T>,
        isLastItem: Boolean
    ) {
        holder as Item
        data as EventModelRealm
        holder.tvName.text =data.note
        if(data.name.isNullOrEmpty()){
            holder.tvNameUser.visibility= View.GONE
        }else{
            holder.tvNameUser.visibility= View.VISIBLE
            holder.tvNameUser.text =data.name
        }
        holder.tvDate.text = TimeConverter.getDate(data.time!!, TimeConverter.DD_MM_YY_HH_MM)
    }
    inner class Item(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvNameUser: TextView = view.findViewById(R.id.tvNameUser)
    }
}