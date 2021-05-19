package com.example.danshal.ui.home

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.MediaController
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.danshal.databinding.FragmentPostBinding


class PostDialogFragment : DialogFragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private var toggle: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
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
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePost()

        binding.ivPost.setOnClickListener { toggleContent() }
    }

    private fun observePost() {
        viewModel.currentContent.observe(viewLifecycleOwner, {
            Glide.with(this).load(it.imageUrl).into(binding.ivPost)
            binding.tvPostTitleDialog.text = it.title
            binding.tvPostContentDialog.text = it.content
        })
    }

    private fun toggleContent() {
        toggle = !toggle
        if(toggle) {
            binding.clPostContent.visibility = View.GONE
        } else {
            binding.clPostContent.visibility = View.VISIBLE
        }
    }

    fun newInstance(): PostDialogFragment? {
        return PostDialogFragment()
    }
}