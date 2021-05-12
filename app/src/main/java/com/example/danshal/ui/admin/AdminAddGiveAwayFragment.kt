package com.example.danshal.ui.admin

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import com.example.danshal.databinding.AdminAddGiveAwayFragmentBinding
import com.example.danshal.models.Event
import com.example.danshal.models.GiveAway
import com.example.danshal.models.Notification
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.util.*

class AdminAddGiveAwayFragment : Fragment() {
    private val REQUEST_CODE = 100
    private val adminDashboardDetailsViewModel: AdminDashboardViewModel by activityViewModels()

    private var _binding: AdminAddGiveAwayFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var date: Date
    private var idContent: String = ""
    private var image: String = ""

    private val storage = Firebase.storage("gs://danshal-c7e70.appspot.com/")
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AdminAddGiveAwayFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddGiveAway.setOnClickListener { postGiveAway() }
        binding.imageView.setOnClickListener { openGalleryForImage() }

        setDate()
        addDatePicker()
        observeCurrentGiveAway()
    }

    private fun observeCurrentGiveAway() {
        adminDashboardDetailsViewModel.currentContent.observe(viewLifecycleOwner, {
            if (it != null) {
                val giveAway = it as GiveAway

                binding.etAddDescription.setText(giveAway.content)
                binding.etAddTitle.setText(giveAway.title)
                image = giveAway.imageUrl.toString()
                idContent = giveAway.id
            }
        })
    }

    private fun setDate() {
        val cal: Calendar = Calendar.getInstance()

        binding.tvAddDate.text =
            "Datum: ${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.YEAR)}"
        this.date = Date(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR))

        // For updating an Event
        if (adminDashboardDetailsViewModel.checkCurrentContent()) {
            adminDashboardDetailsViewModel.currentContent.observe(viewLifecycleOwner, {
                val giveAway = it as GiveAway
                cal.time = giveAway.endDate

                binding.tvAddDate.text = "Datum: ${cal.get(Calendar.DAY_OF_MONTH)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.YEAR)}"
                this.date = Date(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR))
            })
        }
    }

    private fun postGiveAway() {
        val title = binding.etAddTitle.text?.toString()
        val description = binding.etAddDescription.text?.toString()

        if (validate(title) && validate(description)) {
            val giveAway = GiveAway(emptyList(), this.date)
            giveAway.title = title!!
            giveAway.content = description!!

            if(adminDashboardDetailsViewModel.checkCurrentContent()) {
                giveAway.imageUrl = image
                adminDashboardDetailsViewModel.updateGiveAway(giveAway)
                addImageToStorage(idContent)
                findNavController().navigate(R.id.action_adminAddGiveAwayFragment_to_nav_admin_dashboard)
                Toast.makeText(context, "Give away is bijgewerkt", Toast.LENGTH_SHORT).show()
            } else {
                addToDatabase(giveAway)
            }
        } else {
            // TODO: Toast is not showing up
            Toast.makeText(
                context,
                "Er zijn een aantal verplichte velden niet ingevuld",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addToDatabase(giveAway: GiveAway) {
        db.collection("giveaways")
            .add(giveAway)
            .addOnSuccessListener { documentReference ->
                addImageToStorage(documentReference.id)

                db.collection("giveaways").document(documentReference.id)
                    .update("id", documentReference.id)

                db.collection("notifications")
                    .add(Notification("Give away toegevoegd: ${giveAway.title}"))

                findNavController().navigate(R.id.action_adminAddGiveAwayFragment_to_nav_admin_dashboard)
                Toast.makeText(context, "Give away is toegevoegd", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Het is niet gelukt de give away toe te voegen",
                    Toast.LENGTH_SHORT
                ).show()
            }
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

    private fun addImageToStorage(document: String) {
        if (binding.imageView.drawable != null) {
            binding.imageView.isDrawingCacheEnabled = true
            binding.imageView.buildDrawingCache()
            val bitmap = (binding.imageView.drawable as BitmapDrawable).bitmap

            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storageRef = storage.reference
            val ref = storageRef.child("content_images/giveaway-$document.jpg")
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
                        val eventsRef = db.collection("giveaways").document(document)

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
                        this.date = Date(year, monthOfYear, dayOfMonth)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}