package com.example.danshal.ui.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.danshal.R
import com.example.danshal.databinding.AdminDashboardFragmentBinding
import com.example.danshal.models.Content
import com.example.danshal.models.Notification
import com.example.danshal.ui.home.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminDashboardFragment : Fragment() {
    private var _binding: AdminDashboardFragmentBinding? = null
    private val binding get() = _binding!!

    private val notifications = arrayListOf<Notification>()
    private val adminNotificationAdapter = AdminNotificationAdapter(notifications)
    private val adminDashboardDetailsViewModel: AdminDashboardViewModel by activityViewModels()

    private val db = Firebase.firestore
    private val viewModel: AdminDashboardViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminDashboardFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        viewModel.getAllEvents()
        viewModel.getAllGiveAways()
        viewModel.getAllPosts()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        adminDashboardDetailsViewModel.clearCurrentContent()

        binding.btnEvent.setOnClickListener {
            viewModel.detailContentType = Content.TYPE.EVENT
            findNavController().navigate(R.id.action_nav_admin_dashboard_to_adminDashboardDetailsFragment)
        }
        binding.btnGiveaway.setOnClickListener {
            viewModel.detailContentType = Content.TYPE.GIVEAWAY
            findNavController().navigate(R.id.action_nav_admin_dashboard_to_adminDashboardDetailsFragment)
        }
        binding.btnPost.setOnClickListener {
            viewModel.detailContentType = Content.TYPE.POST
            findNavController().navigate(R.id.action_nav_admin_dashboard_to_adminDashboardDetailsFragment)
        }

        binding.btnAddEvent.setOnClickListener {
            findNavController().navigate(R.id.action_nav_admin_dashboard_to_adminAddEventFragment)
        }

        binding.btnAddGiveaway.setOnClickListener {
            findNavController().navigate(R.id.action_nav_admin_dashboard_to_adminAddGiveAwayFragment)
        }

        binding.btnAddPost.setOnClickListener {
            findNavController().navigate(R.id.action_nav_admin_dashboard_to_adminAddPostFragment)
        }

        initViews()
    }

    private fun initViews() {
        binding.rvNotifications.layoutManager = GridLayoutManager(context, 1)
        binding.rvNotifications.adapter = adminNotificationAdapter

        val notificationsRef = db.collection("notifications")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(10)

        notificationsRef.get()
            .addOnSuccessListener { data ->
                if (data != null) {
                    for (notification in data.toObjects(Notification::class.java)) {
                        notifications.add(Notification(notification.text))
                    }
                }

                adminNotificationAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.w("ADMIN DASHBOARD", "Failed retrieving notifications")
            }

        adminNotificationAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if (currentUser == null){
            findNavController().navigate(
                R.id.action_nav_admin_dashboard_to_nav_login
            )
        }
    }

}