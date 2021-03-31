package com.example.danshal.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.FragmentBottomSheetBinding
import com.example.danshal.models.Content
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class GiveawayDialogFragment: BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val viewModel: HomeViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGiveAway()

        binding.btnParticipate.setOnClickListener {
            viewModel.addUserToGiveAway()
        }
    }

    private fun observeGiveAway() {
        viewModel.currentGiveAway.observe(viewLifecycleOwner, Observer {
            binding.tvCurrentGiveAwayTitle.text = it.title
            binding.tvGiveAwayDate.text = "${getDate(it.endDate, false)} ${getDate(it.endDate, true)}"
            if(it.imageUrl != null && it.imageUrl != "") {
                Glide.with(this).load(it.imageUrl).into(binding.ivCurrentGiveAway)
            } else {
                binding.ivCurrentGiveAway.setImageResource(R.drawable.event1)
            }
        })
    }

    // TODO: wordt ook gebruikt in homeadapter! (dubbel)
    // either return the day(false) or month(true)
    fun getDate(date: Date, type: Boolean): Int {
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        //return the month (starts at 0) or day
        return if(type) cal.get(Calendar.MONTH) else cal.get(Calendar.DAY_OF_MONTH)
    }

    fun newInstance(): GiveawayDialogFragment? {
        return GiveawayDialogFragment()
    }
}