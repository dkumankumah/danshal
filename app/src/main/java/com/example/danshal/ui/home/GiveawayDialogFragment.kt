package com.example.danshal.ui.home

import android.os.Bundle
import android.util.Log
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
        binding.btnParticipate.text =
            if (viewModel.checkUserSub() == true) getString(R.string.title_btn_unsubscribe) else getString(
                R.string.title_btn_subscribe
            )
        observeGiveAway()
    }

    // Bind the layout items to the current giveaway.
    private fun observeGiveAway() {
        viewModel.currentGiveAway.observe(viewLifecycleOwner, { giveAway ->
            binding.tvCurrentGiveAwayTitle.text = giveAway.title
            binding.tvGiveAwayDate.text = convertDate(giveAway.endDate)
            binding.tvGiveAwayContent.text = giveAway.content

            if (giveAway.imageUrl != null && giveAway.imageUrl != "") {
                Glide.with(this).load(giveAway.imageUrl).into(binding.ivCurrentGiveAway)
            } else {
                binding.ivCurrentGiveAway.setImageResource(R.drawable.event1)
            }

            binding.btnParticipate.setOnClickListener {
                if (viewModel.isLoggedIn()) {
                    if(viewModel.isSub) {
                        viewModel.removeUserFromGiveAway()
                        Toast.makeText(activity, R.string.msg_update_unsub, Toast.LENGTH_SHORT).show()
                        binding.btnParticipate.text = getString(R.string.title_btn_subscribe)
                    } else {
                        viewModel.addUserToGiveAway()
                        Toast.makeText(activity, R.string.msg_update_succes, Toast.LENGTH_SHORT).show()
                        binding.btnParticipate.text = getString(R.string.title_btn_unsubscribe)
                    }
                    viewModel.loadGiveAway()
                } else {
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

    private fun convertDate(date: Date): String {
        val df: DateFormat = SimpleDateFormat("dd/MM/yyy")
        return df.format(date.getTime())
    }

    fun newInstance(): GiveawayDialogFragment? {
        return GiveawayDialogFragment()
    }
}