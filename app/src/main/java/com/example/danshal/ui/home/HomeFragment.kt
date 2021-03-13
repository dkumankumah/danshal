package com.example.danshal.ui.home

import android.os.Bundle
import android.view.*
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.danshal.R
import com.example.danshal.databinding.FragmentHomeBinding
import com.example.danshal.models.Event

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val events = arrayListOf<Event>()
    private val homeAdapter = HomeAdapter(events)
    private val upEventAdapter = UpEventAdapter(events)

    private lateinit var homeViewModel: HomeViewModel

    // Menu options:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // let the app know that this fragment is expecting menu related callbacks
        setHasOptionsMenu(true)
    }

    // Merge menu of this fragment with the appBar. Because of the inflater instantation
    // a fragment menu has been created
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        // Set teh visibility of the items in the menu
        menu.findItem(R.id.action_settings).isVisible = true
        menu.findItem(R.id.action_filter).isVisible = true
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
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        binding.rvEvents.layoutManager = GridLayoutManager(context, 1)
        binding.rvEvents.adapter = homeAdapter
//        binding.rvUpEvents.layoutManager = GridLayoutManager(context, 1)
//        binding.rvUpEvents.adapter = upEventAdapter

        for (i in Event.EVENT_EXAMPLES.indices) {
            events.add(Event.EVENT_EXAMPLES[i])
        }

        homeAdapter.notifyDataSetChanged()
        upEventAdapter.notifyDataSetChanged()
    }

    private fun openFilterWindow() {
        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show()
        val dialog = EventFilterDialogFragment()
        dialog.showsDialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null;
    }
}