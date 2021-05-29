package com.library.todolist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.library.todolist.adapter.EventListAdapter
import com.library.todolist.adapter.GenericAdapter
import com.library.todolist.databinding.ThirdFragmentBinding
import com.library.todolist.realm.EventModelRealm
import com.library.todolist.realm.RealmControllerEvent

class ThirdPage : Fragment() {
    private var _binding: ThirdFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val realmControllerEvent by lazy { RealmControllerEvent() }
    private val eventListAdapter: GenericAdapter<EventModelRealm> by lazy {
        GenericAdapter<EventModelRealm>(
            EventListAdapter(
                requireActivity()
            )
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ThirdFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvList.layoutManager = LinearLayoutManager(context)
        binding.rvList.adapter = eventListAdapter
        eventListAdapter.clearData()
        eventListAdapter.addAll(realmControllerEvent.getTimeLineEvent())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        eventListAdapter.clearData()
        eventListAdapter.addAll(realmControllerEvent.getTimeLineEvent())
    }
}