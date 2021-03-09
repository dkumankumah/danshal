package com.example.danshal.ui.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
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

        val spinner: Spinner = root.findViewById(R.id.spinner_admin_add)
        // Create an ArrayAdapter using the string array and a default spinner layout
        context?.let {
            ArrayAdapter.createFromResource(
                    it,
                R.array.admin_add_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        }
        return root
    }

}