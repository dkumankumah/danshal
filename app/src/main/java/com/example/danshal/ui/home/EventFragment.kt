package com.example.danshal.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.FragmentEventBinding

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
        viewModel.currentEvent.observe(viewLifecycleOwner, {
            binding.tvTitle.text = it.title
            binding.tvEventContent.text = it.content
            binding.tvAddress.text = "Address: ${it.address.street} ${it.address.housenumber} ${it.address.housenumberExtension} \n " +
                    "Place: ${it.address.place}"
            Glide.with(this).load(it.imageUrl).into(binding.ivEvent)
        })
    }
}