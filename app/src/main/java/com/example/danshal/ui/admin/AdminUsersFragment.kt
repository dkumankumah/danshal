package com.example.danshal.ui.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.danshal.databinding.AdminUsersFragmentBinding
import com.example.danshal.models.User

class AdminUsersFragment : Fragment() {

    private var _binding: AdminUsersFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: AdminUserAdapter
    private var users = arrayListOf<User>()
    private val adminUsersViewModel: AdminUsersViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = AdminUsersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter = AdminUserAdapter(users)

        observeUsers()
        initViews()

        binding.btnRegister.setOnClickListener {

        }
    }

    private fun observeUsers() {
        adminUsersViewModel.getUsers()

        adminUsersViewModel.users.observe(viewLifecycleOwner, {
            users.clear()
            users.addAll(it)
            userAdapter.notifyDataSetChanged()
        })

    }

    private fun initViews() {
        binding.rvUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvUser.adapter = userAdapter
    }

}