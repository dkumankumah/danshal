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

class AdminUsersFragment : Fragment() {

    private lateinit var adminUsersViewModel: AdminUsersViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        adminUsersViewModel =
                ViewModelProvider(this).get(AdminUsersViewModel::class.java)
        val root = inflater.inflate(R.layout.admin_users_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.text_admin_users)
        adminUsersViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}