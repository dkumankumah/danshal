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

class AdminAddFragment : Fragment() {

    private lateinit var adminAddViewModel: AdminAddViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        adminAddViewModel =
                ViewModelProvider(this).get(AdminAddViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_admin_add, container, false)
        val textView: TextView = root.findViewById(R.id.text_admin_add)
        adminAddViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}