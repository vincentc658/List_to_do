package com.library.todolist.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

interface AdapterConfiguration {
    fun <T> getItemViewType(position: Int, data: T): Int
    fun getViewHolder(parent: ViewGroup, viewType: Int):RecyclerView.ViewHolder
    fun <T> bindToHolder(holder: RecyclerView.ViewHolder, data: BaseRecylerViewModel, listData: ArrayList<T> = arrayListOf(), isLastItem: Boolean = false)
}