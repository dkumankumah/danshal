package com.example.danshal.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.FragmentEventBinding
import com.example.danshal.models.Content
import com.example.danshal.models.Event
import java.text.DateFormatSymbols
import java.time.Year
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
            binding.tvAddress.text = getString(
                R.string.title_address_event,
                event.address.street + event.address.housenumberExtension,
                event.address.housenumber,
                event.address.place
            )
            binding.tvDay.text = Content.getDate(event.date, false).toString()
            binding.tvMonth.text = monthName[Content.getDate(event.date, true)]
            Glide.with(this).load(event.imageUrl).into(binding.ivEventImage)

            // Share event by calling a Intent
            binding.btnShare.setOnClickListener {
                val date = "${Content.getDate(event.date, false)} ${
                    monthName[Content.getDate(
                        event.date,
                        true
                    )]
                }"
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Danshal")
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.msg_share, event.title, date)
                )
                shareIntent.type = "text/plain"
                startActivity(shareIntent)
            }

            binding.btnTicket.setOnClickListener {
                var url = event.ticket

                // Browser intent url MUST contain https:// or else it crashes
                if(!url.contains("https://")) {
                    url = "https://${url}"
                }

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        })
    }
}