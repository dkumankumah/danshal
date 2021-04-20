package com.example.danshal.ui.admin

import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.danshal.R
import com.example.danshal.databinding.AdminGiveawayAttendeesFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class AdminGiveawayAttendeesFragment : BottomSheetDialogFragment() {
    private var _binding: AdminGiveawayAttendeesFragmentBinding? = null
    private val binding get() = _binding!!
    private var user: FirebaseUser? = null
    private val viewModel: AdminDashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminGiveawayAttendeesFragmentBinding.inflate(inflater, container, false)
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
        setWinnerContentInvisible()
    }

    private fun setWinnerContentInvisible() {
        binding.winnerName.visibility = View.INVISIBLE
        binding.winnerEmail.visibility = View.INVISIBLE
        binding.tvWinner.visibility = View.INVISIBLE
    }

    // Bind the layout items to the current giveaway.
    private fun observeGiveAway() {
        viewModel.currentGiveAway.observe(viewLifecycleOwner, { giveAway ->

            binding.tvCurrentGiveAwayTitle.text = giveAway.title
            // TODO: Year in date is not right and shows a weird number
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.GERMAN)
            binding.tvDate.text = "Einddatum: ${formatter.format(giveAway.endDate)}"

            giveAway.participants?.let {
                binding.tvCurrentGiveAwayAttendees.text = "Deelnemers: ${it.size}"
            }

            binding.btnChooseWinner.setOnClickListener {
                if (giveAway.participants?.isNotEmpty() == true) {
                    giveAway.participants?.let { it1 -> chooseWinner(it1) }
                }
            }
        })
    }

    private fun chooseWinner(attendees: List<String>) {
        val winner = attendees.random()
        viewModel.userById(winner)
        viewModel.giveawayWinnerData.observe(viewLifecycleOwner, { user ->
            binding.winnerName.visibility = View.VISIBLE
            binding.winnerEmail.visibility = View.VISIBLE
            binding.tvWinner.visibility = View.VISIBLE

            binding.winnerEmail.text = user.email
            binding.winnerName.text = user.naam
        })
    }

    fun newInstance(): AdminGiveawayAttendeesFragment {
        return AdminGiveawayAttendeesFragment()
    }
}