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

class AdminAddGiveAwayFragment : Fragment() {

    private lateinit var adminAddGiveAwayViewModel: AdminAddGiveAwayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adminAddGiveAwayViewModel =
            ViewModelProvider(this).get(AdminAddGiveAwayViewModel::class.java)
        val root = inflater.inflate(R.layout.admin_add_give_away_fragment, container, false)
        val textView: TextView = root.findViewById(R.id.text_admin_add_give_away)
        adminAddGiveAwayViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

}