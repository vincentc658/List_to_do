package com.library.todolist.adapter

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import kotlin.collections.ArrayList


class GenericAdapter<T : BaseRecylerViewModel>(
    private var itemAdapterListener: AdapterConfiguration? = null,
    private var recyclerView: RecyclerView? = null,
) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        val ITEM = 200
    }

    private var isReverseList = false
    private var isLoadOnLastItem = false
    private val listOfData = mutableListOf<T>()
    private var linearLayoutManager: LinearLayoutManager? = recyclerView?.layoutManager as LinearLayoutManager?


    override fun getItemViewType(position: Int): Int {
         return   itemAdapterListener!!.getItemViewType(position, listOfData[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        return when (viewType) {
//            LOADING -> Loading(
//                    (LayoutInflater.from(itemAdapterListener!!.getContext()).inflate(
//                            R.layout.rv_item_loading,
//                            parent,
//                            false
//                    ))
//            )
//
//            else -> {
                return itemAdapterListener!!.getViewHolder(parent, viewType)
//            }
//        }
    }

    override fun getItemCount(): Int = listOfData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            itemAdapterListener?.bindToHolder<T>(holder, listOfData[position], listOfData as ArrayList<T>,isLastItem = position == listOfData.size - 1)
    }


    fun isLoadOnLastItem(isLoad: Boolean) {
        isLoadOnLastItem = isLoad
    }



    fun addAll(list: MutableList<T>) {

        list.forEach {
            listOfData.add(it)
            notifyItemInserted(itemCount-1)
        }
    }

    fun addDataToBottom(data: T) {
        listOfData.add(0, data)
        notifyItemInserted(0)
    }

    fun addData(data: T) {
        listOfData.add(data)
        notifyItemInserted(listOfData.size - 1)
    }



    fun getData(position: Int): T = listOfData[position]

    fun getAllData(): MutableList<T> = listOfData

    fun updateData(position: Int, data: T) {
        listOfData[position] = data
        notifyItemChanged(position)
    }


    fun clearData() {
        listOfData.clear()
        notifyDataSetChanged()
    }
    fun clearAtPositionFrom(start : Int, end : Int){
        for(i in end downTo start){
            removeItemAtPosition(i)
        }
    }
    fun removeItemAtPosition(position: Int){
        listOfData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeLastItem() {
        listOfData.removeAt(itemCount - 1)
        notifyItemRemoved(itemCount)
        notifyItemChanged(itemCount - 1)
    }



}