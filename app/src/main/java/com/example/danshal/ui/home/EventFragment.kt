package com.example.danshal.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.FragmentEventBinding
import com.example.danshal.models.Content
import com.example.danshal.models.Event
import java.text.DateFormatSymbols
import java.util.*

class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvent()
    }

    private fun observeEvent() {
        viewModel.currentContent.observe(viewLifecycleOwner, {
            val event = it as Event
            val monthName = DateFormatSymbols(Locale.ENGLISH).shortMonths

            binding.tvTitle.text = event.title
            binding.tvEventContent.text = event.content
            binding.tvAddress.text =  getString(R.string.title_address_event,
                event.address.street + event.address.housenumberExtension, event.address.housenumber, event.address.place)
            binding.tvDay.text = Content.getDate(event.date, false).toString()
            binding.tvMonth.text = monthName[Content.getDate(event.date, true)]
            Glide.with(this).load(event.imageUrl).into(binding.ivEventImage)

            binding.btnTicket.setOnClickListener {
                Toast.makeText(activity, "I dont work yet", Toast.LENGTH_SHORT).show()
            }
        })
    }
}