package com.example.danshal.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.danshal.R
import com.example.danshal.databinding.FragmentLogoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Logout_Fragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var _binding: FragmentLogoutBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        auth = Firebase.auth

        _binding = FragmentLogoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnJa.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_nav_logout_page_to_nav_home)
        }

        binding.btnNee.setOnClickListener {
            findNavController().navigate(R.id.action_nav_logout_page_to_nav_home)
        }

    }



}