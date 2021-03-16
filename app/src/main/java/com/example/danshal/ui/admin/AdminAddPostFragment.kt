package com.example.danshal.ui.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.danshal.R
import com.example.danshal.databinding.AdminAddPostFragmentBinding
import com.example.danshal.models.Post

class AdminAddPostFragment : Fragment() {

    private lateinit var adminAddPostViewModel: AdminAddPostViewModel

    private var _binding: AdminAddPostFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        adminAddPostViewModel =
            ViewModelProvider(this).get(AdminAddPostViewModel::class.java)

        _binding = AdminAddPostFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPost.setOnClickListener {
            postContent()
        }
    }

    private fun postContent() {
        Log.i("POST", "POSTING POST")

        // Event inputs
        val title = binding.etAddTitle.text?.toString()
        val description = binding.etAddDescription.text?.toString()

        if (validate(title) && validate(description)) {
            // TODO: Post id not sending
            val post = Post(1, title!!, description!!, binding.switchAddExclusive.isChecked, R.drawable.event1)

            Log.i("POST", post.toString())
            // TODO: Make api call
            // TODO: Toast is not showing up
            Toast.makeText(context, "Post is toegevoegd", Toast.LENGTH_SHORT).show()
        } else {
            // TODO: Toast is not showing up
            Toast.makeText(context, "Er zijn een aantal verplichte velden niet ingevuld", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Check if value is null or empty
     */
    private fun validate(value: String?): Boolean {
        return value != null && value != ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}