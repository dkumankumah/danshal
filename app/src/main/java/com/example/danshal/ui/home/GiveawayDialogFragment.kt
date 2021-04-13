package com.example.danshal.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class GiveawayDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    private var user: FirebaseUser? = null
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var giveAwayId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        user = Firebase.auth.currentUser
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGiveAway()
    }

    // Bind the layout items to the current giveaway.
    private fun observeGiveAway() {
        viewModel.currentGiveAway.observe(viewLifecycleOwner, {
            binding.tvCurrentGiveAwayTitle.text = it.title
            binding.tvGiveAwayDate.text = convertDate(it.endDate)
            giveAwayId = it.id

            if (it.imageUrl != null && it.imageUrl != "") {
                Glide.with(this).load(it.imageUrl).into(binding.ivCurrentGiveAway)
            } else {
                binding.ivCurrentGiveAway.setImageResource(R.drawable.event1)
            }

            // User is logged in, check if they are subscribed to this giveaway
            if (user != null) {
                // change button functionality if user has already subscribed or not
                if (it.participants.contains(user!!.uid)) btnUnSubscribe(giveAwayId) else btnSubscribe(giveAwayId)
                observeEnterGiveAway()
            } else {
                binding.btnParticipate.setOnClickListener {
                    Toast.makeText(
                        activity,
                        getString(R.string.msg_login_giveaway),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModel.errorText.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })
    }

    // Change the button and tis clicklistener when subscribing/unsubscribing from a giveaway
    private fun btnUnSubscribe(giveAwayId: String) {
        binding.btnParticipate.text = getString(R.string.title_btn_unsubscribe)
        binding.btnParticipate.setOnClickListener {
            viewModel.removeUserFromGiveAway(giveAwayId)
            btnSubscribe(giveAwayId)
            activity?.viewModelStore?.clear()
        }
    }

    private fun btnSubscribe(giveAwayId: String) {
        binding.btnParticipate.text = getString(R.string.title_btn_subscribe)
        binding.btnParticipate.setOnClickListener {
            viewModel.addUserToGiveAway(giveAwayId)
            btnUnSubscribe(giveAwayId)
            activity?.viewModelStore?.clear()
        }
    }

    // Return feedback to the user when (un)subscribing to a giveaway
    private fun observeEnterGiveAway() {
        viewModel.giveAwayStatus.observe(viewLifecycleOwner, {
            val msg: String =
                if (it) getString(R.string.msg_update_succes) else getString(R.string.msg_update_unsub)
            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
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