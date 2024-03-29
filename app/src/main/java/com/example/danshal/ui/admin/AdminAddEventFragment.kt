package com.example.danshal.ui.admin

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageView
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
    private val adminDashboardDetailsViewModel: AdminDashboardViewModel by activityViewModels()

    private var _binding: AdminAddEventFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var date: Date
    private var idContent : String = ""
    private var image: String = ""

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

        binding.imageView.setOnClickListener { openGalleryForImage() }
        binding.btnAddEvent.setOnClickListener { postEvent() }

        val cal: Calendar = Calendar.getInstance()
        setDate(cal)
        addDatePicker()
        observeCurrentContent()
    }

    private fun observeCurrentContent() {
        adminDashboardDetailsViewModel.currentContent.observe(viewLifecycleOwner, {
            if(it != null){
                val event = it as Event
                binding.addressLayout.etHousenumber.setText(event.address.housenumber.toString())
                binding.addressLayout.etHousenumberExt.setText(event.address.housenumberExtension.toString())
                binding.addressLayout.etPostcode.setText(event.address.postcode)
                binding.addressLayout.etStreet.setText(event.address.street)
                binding.addressLayout.etPlace.setText(event.address.place)

                binding.etAddTitle.setText(event.title)
                binding.etAddDescription.setText(event.content)
                binding.etAddTicket.setText(event.ticket)

                binding.btnAddEvent.text = getString(R.string.admin_update_post)
                image = event.imageUrl.toString()
                idContent = event.id

                val cal: Calendar = Calendar.getInstance()
                cal.time = event.date
                setDate(cal)
            }
        })
    }

    private fun setDate(calendar: Calendar) {
        binding.tvAddDate.text = "Datum: ${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)}"
        this.date = convertDate(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))
    }

    private fun addDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.btnAddDate.setOnClickListener {
            context?.let {
                val dpd = DatePickerDialog(
                    it,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        // Display Selected date in TextView
                        this.date = convertDate(dayOfMonth, monthOfYear, year)
                        binding.tvAddDate.text = "Datum: $dayOfMonth-${monthOfYear + 1}-$year"
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
        }
    }

    private fun convertDate(day: Int, month: Int, year: Int): Date {
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DATE, day)
        return cal.time
    }

    private fun openGalleryForImage() {
        context?.let {
            CropImage
                .activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(it, this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri: Uri? = result?.uriContent
                binding.imageView.setImageURI(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result!!.error
            }
        }
    }

    private fun postEvent() {
        val housenumber = binding.addressLayout.etHousenumber.text?.toString()
        val housenumberExtension = binding.addressLayout.etHousenumberExt.text?.toString()
        val postcode = binding.addressLayout.etPostcode.text?.toString()
        val street = binding.addressLayout.etStreet.text?.toString()
        val place = binding.addressLayout.etPlace.text?.toString()

        val title = binding.etAddTitle.text?.toString()
        val description = binding.etAddDescription.text?.toString()

        val ticket = binding.etAddTicket.text?.toString()

        if (validate(housenumber) && validate(postcode) && validate(street) && validate(place) && validate(title)
            && validate(description) && validate(ticket)) {
            val address = Address(housenumber!!.toInt(), housenumberExtension, postcode!!, street!!, place!!)
            val event = Event(address, this.date, binding.switchAddExclusive.isChecked, ticket.toString())
            event.title = title!!
            event.content = description!!

            if (adminDashboardDetailsViewModel.checkCurrentContent()) {
                event.imageUrl = image
                adminDashboardDetailsViewModel.updateEvent(event)
                addImageToStorage(idContent)

                findNavController().navigate(R.id.action_adminAddEventFragment_to_nav_admin_dashboard)
                Toast.makeText(context, "Event is bijgewerkt", Toast.LENGTH_SHORT).show()
            } else {
                addToDatabase(event)
            }
        } else {
            Toast.makeText(
                context,
                "Er zijn een aantal verplichte velden niet ingevuld",
                Toast.LENGTH_SHORT
            ).show()
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
                Toast.makeText(
                    context,
                    "Uploaden van foto/video is niet gelukt",
                    Toast.LENGTH_SHORT
                ).show()
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
                Toast.makeText(
                    context,
                    "Het is niet gelukt het event toe te voegen",
                    Toast.LENGTH_SHORT
                ).show()
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