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
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminDashboardFragment : Fragment() {

    private lateinit var adminDashboardViewModel: AdminDashboardViewModel

    private var _binding: AdminDashboardFragmentBinding? = null
    private val binding get() = _binding!!

    private val notifications = arrayListOf<Notification>()
    private val adminNotificationAdapter = AdminNotificationAdapter(notifications)

    private val db = Firebase.firestore
    private val viewModel: AdminDashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adminDashboardViewModel =
            ViewModelProvider(this).get(AdminDashboardViewModel::class.java)

        _binding = AdminDashboardFragmentBinding.inflate(inflater, container, false)
        setTotals()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cvEvents.setOnClickListener {
            viewModel.detailContentType = Content.TYPE.EVENT
            findNavController().navigate(R.id.action_nav_admin_dashboard_to_adminDashboardDetailsFragment)
        }
        binding.cvGiveaways.setOnClickListener {
            viewModel.detailContentType = Content.TYPE.GIVEAWAY
            findNavController().navigate(R.id.action_nav_admin_dashboard_to_adminDashboardDetailsFragment)
        }
        binding.cvPosts.setOnClickListener {
            viewModel.detailContentType = Content.TYPE.POST
            findNavController().navigate(R.id.action_nav_admin_dashboard_to_adminDashboardDetailsFragment)
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

    private fun setTotals() {
        viewModel.getAllEvents()
        viewModel.getAllGiveAways()
        viewModel.getAllPosts()

        viewModel.eventListData.observe(viewLifecycleOwner, {
            binding.tvNumberEvents.text = it.size.toString()
        })

        viewModel.giveawayListData.observe(viewLifecycleOwner, {
            binding.tvNumberGiveaways.text = it.size.toString()
        })

        viewModel.postListData.observe(viewLifecycleOwner, {
            binding.tvNumberPosts.text = it.size.toString()
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}