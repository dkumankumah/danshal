package com.example.danshal.ui.admin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.danshal.R
import com.example.danshal.databinding.AdminAddPostFragmentBinding
import com.example.danshal.models.Notification
import com.example.danshal.models.Post
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class AdminAddPostFragment : Fragment() {
    private val REQUEST_CODE = 100

    private lateinit var adminAddPostViewModel: AdminAddPostViewModel

    private var _binding: AdminAddPostFragmentBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore
    private val storage = Firebase.storage("gs://danshal-c7e70.appspot.com/")

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

        binding.btnAddUpload.setOnClickListener {
            openGalleryForImage()
        }
    }

    private fun postContent() {
        val title = binding.etAddTitle.text?.toString()
        val description = binding.etAddDescription.text?.toString()

        if (validate(title) && validate(description)) {
            val post = Post(binding.switchAddExclusive.isChecked)
            post.title = title!!
            post.content = description!!

            addToDatabase(post)
        } else {
            Toast.makeText(context, "Er zijn een aantal verplichte velden niet ingevuld", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToDatabase(post: Post) {
        db.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                addImageToStorage(documentReference.id)

                db.collection("posts").document(documentReference.id)
                    .update("id", documentReference.id)

                val notificationText = if (post.exclusive) "Exclusieve post is toegevoegd: ${post.title}" else "Post is toegevoegd: ${post.title}"
                db.collection("notifications")
                    .add(Notification(notificationText))

                findNavController().navigate(R.id.action_adminAddPostFragment_to_nav_admin_dashboard)

                Toast.makeText(context, "De post is toegevoegd", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("Cloud", "Error adding document", e)
                Toast.makeText(context, "Het is niet gelukt de post toe te voegen", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            binding.imageView.setImageURI(data?.data) // handle chosen image
        }
    }

    private fun addImageToStorage(document: String) {
        if (binding.imageView.drawable != null) {
            binding.imageView.isDrawingCacheEnabled = true
            binding.imageView.buildDrawingCache()
            val bitmap = (binding.imageView.drawable as BitmapDrawable).bitmap

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storageRef = storage.reference
            val ref = storageRef.child("content_images/post-$document.jpg")
            val uploadTask = ref.putBytes(data)

            /**
             * Uploading video/photo to storage
             */
            uploadTask.addOnFailureListener {
                Toast.makeText(context, "Uploaden van foto/video is niet gelukt", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                /**
                 * Check if the upload is done
                 * If completed retrieve the download url and add it to the database column
                 */
                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    ref.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val eventsRef = db.collection("posts").document(document)

                        eventsRef
                            .update("imageUrl", task.result.toString())
                    }
                }
            }
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