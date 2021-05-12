package com.example.danshal.ui.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.danshal.R
import com.example.danshal.databinding.AdminAddFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminAddFragment : Fragment() {
    private val adminDashboardDetailsViewModel: AdminDashboardViewModel by activityViewModels()

    private var _binding: AdminAddFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth

        _binding = AdminAddFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adminDashboardDetailsViewModel.clearCurrentContent()

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

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if (currentUser == null){
            findNavController().navigate(
                R.id.action_nav_admin_add_to_nav_login
            )
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