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

class AdminDashboardFragment : Fragment() {

    private lateinit var adminDashboardViewModel: AdminDashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        adminDashboardViewModel =
                ViewModelProvider(this).get(AdminDashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_admin_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_admin_dashboard)
        adminDashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}