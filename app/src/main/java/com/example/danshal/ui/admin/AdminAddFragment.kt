package com.example.danshal.ui.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.danshal.R
import com.example.danshal.databinding.AdminAddFragmentBinding

class AdminAddFragment : Fragment() {

    private lateinit var adminAddViewModel: AdminAddViewModel

    private var _binding: AdminAddFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adminAddViewModel =
            ViewModelProvider(this).get(AdminAddViewModel::class.java)

        _binding = AdminAddFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddEvent.setOnClickListener {
            startAdminAddFragment(R.id.action_nav_admin_add_to_adminAddEventFragment)
        }

        binding.btnAddGiveAway.setOnClickListener {
            startAdminAddFragment(R.id.action_nav_admin_add_to_adminAddGiveAwayFragment)
        }

        binding.btnAddPost.setOnClickListener {
            startAdminAddFragment(R.id.action_nav_admin_add_to_adminAddPostFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startAdminAddFragment(id: Int) {
        findNavController().navigate(
            id
        )
    }

}