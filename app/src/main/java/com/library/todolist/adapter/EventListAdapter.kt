package com.library.todolist.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.library.todolist.OnAttendEventListener
import com.library.todolist.OnDeleteEventListener
import com.library.todolist.R
import com.library.todolist.TimeConverter
import com.library.todolist.realm.EventModelRealm

class EventListAdapter(
    private val context: Activity,
    private val callback: OnDeleteEventListener?,
    private val callbackAttend: OnAttendEventListener? = null
) : AdapterConfiguration {
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
        holder.tvName.text = data.note
        holder.tvSubNote.text = "Note : ${data.subNote}"
        if (data.name.isNullOrEmpty()) {
            holder.tvNameUser.visibility = View.GONE
        } else {
            holder.tvNameUser.visibility = View.VISIBLE
            holder.tvNameUser.text = data.name
        }
        holder.tvDate.text = TimeConverter.getDate(data.time!!, TimeConverter.DD_MM_YY_HH_MM)
        if (callback != null) {
            holder.ivDelete.visibility = View.VISIBLE
            holder.tvAttend.visibility = View.GONE
            holder.ivDelete.setOnClickListener {
                callback.onDelete(data.time!!, !data.name.isNullOrEmpty())
            }
        } else {
            holder.ivDelete.visibility = View.GONE
            if (data.attend!!) {
                holder.tvAttend.visibility = View.GONE
            } else {
                holder.tvAttend.visibility = View.VISIBLE
                holder.tvAttend.setOnClickListener {
                    callbackAttend?.attendEvent(data.time!!)
                }
            }
        }

    }

    inner class Item(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvNameUser: TextView = view.findViewById(R.id.tvNameUser)
        val tvSubNote: TextView = view.findViewById(R.id.tvSubNote)
        val ivDelete: ImageView = view.findViewById(R.id.ivDelete)
        val tvAttend: TextView = view.findViewById(R.id.tvAttend)
    }
}