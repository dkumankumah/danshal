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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminAddPostFragment : Fragment() {

    private lateinit var adminAddPostViewModel: AdminAddPostViewModel

    private var _binding: AdminAddPostFragmentBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore

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
        val title = binding.etAddTitle.text?.toString()
        val description = binding.etAddDescription.text?.toString()

        if (validate(title) && validate(description)) {
            val post = Post(title!!, description!!, binding.switchAddExclusive.isChecked, R.drawable.event1)

            addToDatabase(post)
        } else {
            // TODO: Toast is not showing up
            Toast.makeText(context, "Er zijn een aantal verplichte velden niet ingevuld", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToDatabase(post: Post) {
        db.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d("Cloud", "DocumentSnapshot added with ID: ${documentReference.id}")
                // TODO: Toast is not showing up
                Toast.makeText(context, "De post is toegevoegd", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("Cloud", "Error adding document", e)
                // TODO: Toast is not showing up
                Toast.makeText(context, "Het is niet gelukt de post toe te voegen", Toast.LENGTH_SHORT).show()
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