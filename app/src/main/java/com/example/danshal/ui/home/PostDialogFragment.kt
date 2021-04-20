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
import com.example.danshal.models.Post


class PostDialogFragment : DialogFragment() {
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()

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
            dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observePost()
    }

    private fun observePost() {
        viewModel.currentContent.observe(viewLifecycleOwner, {
            if(it.imageUrl.toString().contains("content_videos")) {
                binding.vvPost.setVideoURI(Uri.parse(it.imageUrl))
                binding.ivPost.visibility = View.GONE
                binding.vvPost.visibility = View.VISIBLE
                val vidControl: MediaController = MediaController(context)
                vidControl.setAnchorView(binding.vvPost)
                binding.vvPost.setMediaController(vidControl)
                binding.vvPost.start()
            } else {
                binding.ivPost.visibility = View.VISIBLE
                binding.vvPost.visibility = View.GONE
                Glide.with(this).load(it.imageUrl).into(binding.ivPost)
            }

        })
    }

    fun newInstance(): PostDialogFragment? {
        return PostDialogFragment()
    }
}