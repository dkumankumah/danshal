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

class AdminAddPostFragment : Fragment() {

    private lateinit var adminAddPostViewModel: AdminAddPostViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        adminAddPostViewModel =
                ViewModelProvider(this).get(AdminAddPostViewModel::class.java)
        val root = inflater.inflate(R.layout.admin_add_post_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.text_admin_add_post)
        adminAddPostViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}