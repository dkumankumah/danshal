package com.example.danshal.ui.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.danshal.R

class AdminAddEventFragment : Fragment() {

    private lateinit var adminAddEventViewModel: AdminAddEventViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminAddEventViewModel =
            ViewModelProvider(this).get(AdminAddEventViewModel::class.java)
        val root = inflater.inflate(R.layout.admin_add_event_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.text_admin_add_event)
        adminAddEventViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}