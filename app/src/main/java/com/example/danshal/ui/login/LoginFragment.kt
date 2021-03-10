package com.example.danshal.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.danshal.R
import com.example.danshal.databinding.FragmentAdminAddBinding
import com.example.danshal.databinding.LoginFragmentBinding
import com.example.danshal.ui.admin.AdminAddViewModel

class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

}