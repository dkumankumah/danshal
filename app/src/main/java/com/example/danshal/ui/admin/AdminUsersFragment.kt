package com.example.danshal.ui.admin

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.danshal.R
import com.example.danshal.databinding.AdminUsersFragmentBinding
import com.example.danshal.models.Address
import com.example.danshal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminUsersFragment : Fragment() {

    private var _binding: AdminUsersFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: AdminUserAdapter
    private var users = arrayListOf<User>()
    private val adminUsersViewModel: AdminUsersViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth
        _binding = AdminUsersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter = AdminUserAdapter(users, ::onItemClick)

        observeUsers()
        initViews()
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

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun onItemClick(user: User) {
        binding.rvUser.isVisible = false
        val popup = PopupWindow(context)
        val view = layoutInflater.inflate(R.layout.popup_userview, null)
        popup.contentView = view
        popup.isOutsideTouchable = false
        popup.width = ViewGroup.LayoutParams.MATCH_PARENT
        popup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val name = view.findViewById<EditText>(R.id.et_name_popup)
        val email = view.findViewById<EditText>(R.id.et_email_popup)
        val admin = view.findViewById<Switch>(R.id.switch1)
        var bool = false

        name.setText(user.naam)
        email.setText(user.email)
        admin.isChecked = user.admin

        admin.setOnCheckedChangeListener { admin, isChecked ->
            bool = isChecked
        }

        val btnUpdate = view.findViewById<Button>(R.id.btn_update_popup)
        btnUpdate.setOnClickListener {
            adminUsersViewModel.updateUser(user, bool)
            userAdapter.notifyDataSetChanged()
            binding.rvUser.isVisible = true
            popup.dismiss()
        }

        val btnCancel = view.findViewById<Button>(R.id.btn_annuleren_popup)
        btnCancel.setOnClickListener {
            binding.rvUser.isVisible = true
            popup.dismiss()
        }

        popup.showAtLocation(this.view, Gravity.CENTER, 0,0)

    }

}