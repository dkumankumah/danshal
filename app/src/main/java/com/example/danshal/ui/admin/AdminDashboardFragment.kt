package com.example.danshal.ui.admin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.danshal.databinding.AdminDashboardFragmentBinding
import com.example.danshal.models.Event
import com.example.danshal.models.Notification
import com.example.danshal.ui.home.HomeAdapter
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
            Log.i("ADMIN DASHBOARD", "clicked event overview")
        }
        binding.cvGiveaways.setOnClickListener {
            Log.i("ADMIN DASHBOARD", "clicked giveaways overview")
        }
        binding.cvPosts.setOnClickListener {
            Log.i("ADMIN DASHBOARD", "clicked posts overview")
        }
        binding.cvPostsExcl.setOnClickListener {
            Log.i("ADMIN DASHBOARD", "clicked exclusive posts overview")
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
        val eventsRef = db.collection("events")
        val giveawaysRef = db.collection("giveaways")
        val postsRef = db.collection("posts").whereEqualTo("exclusive", false)
        val exclPostsRef = db.collection("posts").whereEqualTo("exclusive", true)

        eventsRef.get()
            .addOnSuccessListener { events ->
                binding.tvNumberEvents.text = events.size().toString()
                binding.tvNumberEvents.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Log.w("ADMIN DASHBOARD", "Failed retrieving events")
            }

        giveawaysRef.get()
            .addOnSuccessListener { giveaways ->
                binding.tvNumberGiveaways.text = giveaways.size().toString()
                binding.tvNumberGiveaways.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Log.w("ADMIN DASHBOARD", "Failed retrieving giveaways")
            }

        postsRef.get()
            .addOnSuccessListener { posts ->
                binding.tvNumberPosts.text = posts.size().toString()
                binding.tvNumberPosts.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Log.w("ADMIN DASHBOARD", "Failed retrieving posts")
            }

        exclPostsRef.get()
            .addOnSuccessListener { exclusivePosts ->
                binding.tvNumberExclPosts.text = exclusivePosts.size().toString()
                binding.tvNumberExclPosts.visibility = View.VISIBLE
            }
            .addOnFailureListener {
                Log.w("ADMIN DASHBOARD", "Failed retrieving exclusive posts")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}