package com.example.danshal.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.danshal.R
import com.example.danshal.databinding.FragmentHomeBinding
import com.example.danshal.models.Address
import com.example.danshal.models.Event
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private val events = arrayListOf<Event>()
    private val homeAdapter = HomeAdapter(events)
    private val upEventAdapter = UpEventAdapter(events)

    private var currentEventType: String? = null

    private val viewModel: HomeViewModel by activityViewModels()
    private val db = Firebase.firestore


    // Menu options:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentEventType = getString(R.string.title_event)
        // let the app know that this fragment is expecting menu related callbacks
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val itemToHide = menu.findItem(R.id.action_settings)
        itemToHide.isVisible = false

        menu.findItem(R.id.action_filter).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                openFilterWindow()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.rvEvents.layoutManager = GridLayoutManager(context, 1)
        val controller =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_down_to_up)
        binding.rvEvents.layoutAnimation = controller

        binding.rvEvents.adapter = homeAdapter

        // retrieve events
        viewModel.getEvents()

        viewModel.eventListData.observe(viewLifecycleOwner, {
            events.addAll(it)
            homeAdapter.notifyDataSetChanged()
        })

//        setFilter()
        upEventAdapter.notifyDataSetChanged()
        binding.rvEvents.scheduleLayoutAnimation()
    }

    private fun setFilter() {
        binding.rvEvents.adapter = homeAdapter
        events.clear()

        if (currentEventType == getString(R.string.title_up_event)) {
            binding.rvEvents.adapter = upEventAdapter

            // get today's date and the date of 7 days from now
            val start: Timestamp = Timestamp.now()
            val range: Calendar = Calendar.getInstance()
            range.add(Calendar.DATE, +7)
            val end = Timestamp(range.time)


            val docRef = db.collection("events")
                .orderBy("date", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("date", start.toDate())
                .whereLessThanOrEqualTo("date", end.toDate())

            docRef.get()
                .addOnSuccessListener { event ->
                    if (event != null) {
                        for (result in event.toObjects(Event::class.java)) {
                            events.add(
                                Event(
                                    result.title, result.content,
                                    Address(
                                        result.address.housenumber,
                                        result.address.housenumberExtension.toString(),
                                        result.address.postcode,
                                        result.address.street,
                                        result.address.place
                                    ),
                                    result.date, result.exclusive, result.image
                                )
                            )
                        }
                    }
                    upEventAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.d("fetching", "No such document")
                }
        } else {
            println("NIET HIER")
            fetchEvents()
        }
    }


    private fun fetchEvents() {
        val docRef = db.collection("events")
            .orderBy("date", Query.Direction.ASCENDING)
            .whereGreaterThanOrEqualTo("date", Timestamp.now().toDate())
        docRef.get()
            .addOnSuccessListener { event ->
                if (event != null) {
                    Log.d("Fetching events", "Document Snapshot data: ${event.size()}")
                    events.clear()
                    for (result in event.toObjects(Event::class.java)) {
                        events.add(
                            Event(
                                result.title, result.content,
                                Address(
                                    result.address.housenumber,
                                    result.address.housenumberExtension.toString(),
                                    result.address.postcode,
                                    result.address.street,
                                    result.address.place
                                ),
                                result.date, result.exclusive, result.image
                            )
                        )
                    }
                }
                homeAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d("fetching", "No such document")
            }
    }

    private fun openFilterWindow() {
        val dialogItems =
            arrayOf(getString(R.string.title_event), getString(R.string.title_up_event))
        val checkedItem = dialogItems.indexOf(currentEventType)

        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(resources.getString(R.string.title_filter))
                .setNeutralButton(resources.getString(R.string.action_cancel_filter)) { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton(resources.getString(R.string.action_filter)) { dialog, which ->
                    // Respond to positive button press
                    initViews()
                }
                // Single-choice items (initialized with checked item)
                .setSingleChoiceItems(dialogItems, checkedItem) { dialog, which ->
                    // Respond to item chosen
                    println(which)
                    currentEventType = dialogItems[which]
                }
                .show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}