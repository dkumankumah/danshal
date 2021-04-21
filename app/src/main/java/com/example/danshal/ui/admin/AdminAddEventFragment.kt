package com.example.danshal.ui.admin

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.danshal.R
import com.example.danshal.databinding.AdminAddEventFragmentBinding
import com.example.danshal.models.Address
import com.example.danshal.models.Event
import com.example.danshal.models.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*

class AdminAddEventFragment : Fragment() {
    private val REQUEST_CODE = 100

    private var _binding: AdminAddEventFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var date: Date

    private val db = Firebase.firestore
    private val storage = Firebase.storage("gs://danshal-c7e70.appspot.com/")
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Firebase.auth

        _binding = AdminAddEventFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView.setOnClickListener {
            openGalleryForImage()
        }

        binding.btnAddEvent.setOnClickListener {
            postEvent()
        }

        setDate()
        addDatePicker()
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
            val ref = storageRef.child("content_images/event-$document.jpg")
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
                        val eventsRef = db.collection("events").document(document)

                        eventsRef
                            .update("imageUrl", task.result.toString())
                    }
                }
            }
        }
    }

    private fun setDate() {
        this.date = Calendar.getInstance().time
        // TODO: Year is not showing up right in view, but is right added to this.date
        binding.tvAddDate.text = "Datum: ${this.date.date}-${this.date.month + 1}-${this.date.year}"
    }

    private fun postEvent() {
        val housenumber = binding.addressLayout.etHousenumber.text?.toString()
        val housenumberExtension = binding.addressLayout.etHousenumberExt.text?.toString()
        val postcode = binding.addressLayout.etPostcode.text?.toString()
        val street = binding.addressLayout.etStreet.text?.toString()
        val place = binding.addressLayout.etPlace.text?.toString()

        val title = binding.etAddTitle.text?.toString()
        val description = binding.etAddDescription.text?.toString()

        if (validate(housenumber) && validate(postcode) && validate(street) && validate(place) && validate(title) && validate(description)) {
            val address = Address(housenumber!!.toInt(), housenumberExtension, postcode!!, street!!, place!!)
            val event = Event(address, this.date, binding.switchAddExclusive.isChecked)
            event.title = title!!
            event.content = description!!

            addToDatabase(event)
        } else {
            Toast.makeText(context, "Er zijn een aantal verplichte velden niet ingevuld", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToDatabase(event: Event) {
        db.collection("events")
            .add(event)
            .addOnSuccessListener { documentReference ->
                addImageToStorage(documentReference.id)

                db.collection("events").document(documentReference.id)
                    .update("id", documentReference.id)

                db.collection("notifications")
                    .add(Notification("Event toegevoegd: ${event.title}"))

                findNavController().navigate(R.id.action_adminAddEventFragment_to_nav_admin_dashboard)
                Toast.makeText(context, "Event is toegevoegd", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Het is niet gelukt het event toe te voegen", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Check if value is null or empty
     */
    private fun validate(value: String?): Boolean {
        return value != null && value != ""
    }

    private fun addDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.btnAddDate.setOnClickListener {
            context?.let {
                val dpd = DatePickerDialog(it, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    this.date = Date(year, monthOfYear, dayOfMonth)
                    binding.tvAddDate.text = "Datum: $dayOfMonth-${monthOfYear + 1}-$year"
                }, year, month, day)
                dpd.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}