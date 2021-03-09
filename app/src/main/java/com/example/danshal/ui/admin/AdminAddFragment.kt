package com.example.danshal.ui.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.danshal.databinding.FragmentAdminAddBinding

class AdminAddFragment : Fragment() {

    private lateinit var adminAddViewModel: AdminAddViewModel

    private var _binding: FragmentAdminAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adminAddViewModel =
            ViewModelProvider(this).get(AdminAddViewModel::class.java)

        _binding = FragmentAdminAddBinding.inflate(inflater, container, false)

//        binding.btnAddEvent.setOnClickListener { startAdminAddFragment(AdminAddEventFragment) }
        return binding.root

        // val eventButton: Button = root.findViewById(R.id.btn_add_event)
        // val giveAwayButton: Button = root.findViewById(R.id.btn_add_give_away)
        // val postButton: Button = root.findViewById(R.id.btn_add_post)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startAdminAddFragment(fragment: Fragment) {

    }

}