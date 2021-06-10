package com.example.danshal.ui.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.FragmentEventBinding
import com.example.danshal.models.Content
import com.example.danshal.models.Event
import java.text.DateFormatSymbols
import java.util.*


class EventDialogFragment : DialogFragment() {
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeEvent()
    }


    private fun observeEvent() {
        viewModel.currentContent.observe(viewLifecycleOwner, {
            val event = it as Event
            val monthName = DateFormatSymbols(Locale.ENGLISH).shortMonths

            Log.d("Event", event.address.street)

            binding.tvTitle.text = event.title
            binding.tvEventContent.text = event.content
            binding.tvEventAddress.text = getString(
                R.string.title_address_event,
                event.address.street + event.address.housenumberExtension,
                event.address.housenumber,
                event.address.place
            )

            binding.tvEventDate.text = "${Content.getDate(event.date, false)} ${monthName[Content.getDate(event.date, true)]}"
            Glide.with(this).load(event.imageUrl).into(binding.ivEventImage)

            // Share event by calling a Intent
            binding.shareButton.setOnClickListener {
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
                if (!url.contains("https://")) {
                    url = "https://${url}"
                }

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }

            binding.ivBackEvent.setOnClickListener {
                this.dismiss()
            }
        })
    }

    fun newInstance(): EventDialogFragment? {
        return EventDialogFragment()
    }
}