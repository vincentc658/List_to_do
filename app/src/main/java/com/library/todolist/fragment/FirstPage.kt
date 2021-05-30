package com.library.todolist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.todolist.*
import com.library.todolist.adapter.EventListAdapter
import com.library.todolist.adapter.GenericAdapter
import com.library.todolist.databinding.FirstFragmentBinding
import com.library.todolist.realm.EventModelRealm
import com.library.todolist.realm.RealmControllerEvent
import java.text.SimpleDateFormat
import java.util.*

class FirstPage : Fragment(), OnDeleteEventListener {
    private var _binding: FirstFragmentBinding? = null

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
        realmControllerEvent.deleteEvent(time){
            filterDate()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FirstFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvList.layoutManager = LinearLayoutManager(context)
        binding.rvList.adapter = eventListAdapter
        binding.tvAddList.setOnClickListener {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat(TimeConverter.YYYY_MM_DD)
            val formatedDate = formatter.format(time)
            (requireActivity() as SecondActivity).showPopUpAddList(formatedDate, 1)
        }

        filterDate()
    }

    private fun filterDate() {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat(TimeConverter.YYYY_MM_DD)
        val date = formatter.format(time)
        val data = realmControllerEvent.getEvent()
        val filteredEvent = ArrayList<EventModelRealm>()
        data.forEach {
            if (TimeConverter.getDate(it.time!!, TimeConverter.YYYY_MM_DD) == date) {
                filteredEvent.add(it)
            }
        }
        eventListAdapter.clearData()
        eventListAdapter.addAll(filteredEvent)
        if (filteredEvent.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
        } else {
            binding.tvNoData.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        filterDate()
    }

    fun refresh() {
        filterDate()
    }
}