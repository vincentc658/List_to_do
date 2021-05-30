package com.library.todolist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.todolist.OnDeleteEventListener
import com.library.todolist.SecondActivity
import com.library.todolist.TimeConverter
import com.library.todolist.adapter.EventListAdapter
import com.library.todolist.adapter.GenericAdapter
import com.library.todolist.databinding.SecondFragmentBinding
import com.library.todolist.realm.EventModelRealm
import com.library.todolist.realm.RealmControllerEvent
import java.util.*
import kotlin.collections.ArrayList

class SecondPage : Fragment(), OnDeleteEventListener {
    private var _binding: SecondFragmentBinding? = null
    private var date: String? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val realmControllerEvent by lazy { RealmControllerEvent() }
    private val eventListAdapter: GenericAdapter<EventModelRealm> by lazy {
        GenericAdapter<EventModelRealm>(
            EventListAdapter(
                requireActivity(), this
            )
        )
    }

    override fun onDelete(time: Long, isTimeLineEvent : Boolean) {
        if(isTimeLineEvent){
            realmControllerEvent.updateDataEvent(time, false){
                if (date == null) {
                    filterDate(TimeConverter.getDate(binding.cv.date, TimeConverter.YYYY_MM_DD))
                } else {
                    filterDate(date!!)
                }
            }
        }else {
            realmControllerEvent.deleteEvent(time) {
                if (date == null) {
                    filterDate(TimeConverter.getDate(binding.cv.date, TimeConverter.YYYY_MM_DD))
                } else {
                    filterDate(date!!)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SecondFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvList.layoutManager = LinearLayoutManager(context)
        binding.rvList.adapter = eventListAdapter
        binding.tvAddList.setOnClickListener {
            val formatedDate = if (date != null) {
                date
            } else {
                TimeConverter.getDate (binding.cv.date, TimeConverter.YYYY_MM_DD)
            }
            (requireActivity() as SecondActivity).showPopUpAddList(formatedDate!!, 2)
        }
        binding.cv.setOnDateChangeListener { calendarView, i, i2, i3 ->
            val c = Calendar.getInstance()
            c.set(i, i2, i3)
            val formatedDate = TimeConverter.getDate(c.timeInMillis, TimeConverter.YYYY_MM_DD)
            date= formatedDate
            filterDate(formatedDate)
        }
        filterDate(TimeConverter.getDate (binding.cv.date, TimeConverter.YYYY_MM_DD))
    }

    private fun filterDate(date: String) {
        val data = realmControllerEvent.getEvent()
        val filteredEvent = ArrayList<EventModelRealm>()
        data.forEach {
            if (TimeConverter.getDate(it.time!!, TimeConverter.YYYY_MM_DD) == date) {
                filteredEvent.add(it)
            }
        }
        eventListAdapter.clearData()
        eventListAdapter.addAll(filteredEvent)
        if(filteredEvent.isEmpty()){
            binding.tvNoData.visibility= View.VISIBLE
        }else{
            binding.tvNoData.visibility= View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun refresh(date: String){
        filterDate(date)
    }
}