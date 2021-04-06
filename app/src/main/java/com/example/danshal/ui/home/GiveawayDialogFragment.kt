package com.example.danshal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GiveawayDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var giveAwayId: String


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
    }

    private fun observeGiveAway() {
        viewModel.currentGiveAway.observe(viewLifecycleOwner, Observer {
            binding.tvCurrentGiveAwayTitle.text = it.title
            binding.tvGiveAwayDate.text = convertDate(it.endDate)
            giveAwayId = it.id

            if (it.imageUrl != null && it.imageUrl != "") {
                Glide.with(this).load(it.imageUrl).into(binding.ivCurrentGiveAway)
            } else {
                binding.ivCurrentGiveAway.setImageResource(R.drawable.event1)
            }
        })

        viewModel.userLoggedIn.observe(viewLifecycleOwner, {
            binding.btnParticipate.setOnClickListener {
                if(it != null) {
                    viewModel.addUserToGiveAway(giveAwayId)
                    Toast.makeText(activity, "U neemt nu deel aan de giveaway!", Toast.LENGTH_SHORT).show() //idk werkt nog niet
                } else {
                    Toast.makeText(activity, "Log in om mee te doen aan een giveaway", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun convertDate(date: Date): String {
        val df: DateFormat = SimpleDateFormat("dd/MM/yyy")
        return df.format(date.getTime())
    }

    fun newInstance(): GiveawayDialogFragment? {
        return GiveawayDialogFragment()
    }
}