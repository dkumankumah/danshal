package com.example.danshal.ui.profile

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.danshal.R
import com.example.danshal.databinding.FragmentProfileBinding
import com.example.danshal.models.Address
import com.example.danshal.models.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.util.*


class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    private  var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    private var fileUri: Uri? = null

    private var imageReference: StorageReference? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        imageReference = FirebaseStorage.getInstance().reference.child("user_images")

        var currentUser = auth.currentUser
        currentUser?.let { dataFetch(it.uid) }

        // let the app know that this fragment is expecting menu related callbacks
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivProfile.setOnClickListener {
            imagePicker()

        }

        binding.btnUpdate.setOnClickListener {
            if (fileUri != null){
                uploadFile()

            } else {
                updateUser()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val itemToHide = menu.findItem(R.id.action_settings)
        itemToHide.isVisible = false

        menu.findItem(R.id.action_delete).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                showDeleteDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle(getString(R.string.waarschuwing))
        val dialogLayout = layoutInflater.inflate(R.layout.delete_user_dialog, null)

        builder.setView(dialogLayout)
        builder.setNegativeButton(R.string.msg_ja) { _: DialogInterface, _: Int ->
            showEmailForm()

        }
        builder.setPositiveButton(R.string.msg_nee) { _: DialogInterface, _: Int ->

        }
        builder.show()

    }

    private fun showEmailForm() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.authtenticatie))
        val dialogLayout = layoutInflater.inflate(R.layout.user_form_dialog, null)

        builder.setView(dialogLayout)

        val email = dialogLayout.findViewById<EditText>(R.id.et_email)
        val password = dialogLayout.findViewById<EditText>(R.id.et_wachtwoord)
        builder.setNegativeButton(R.string.verwijderen) { _: DialogInterface, _: Int ->
            deleteUser(email, password)

        }
        builder.show()

    }

    private fun deleteUser(email: EditText, password: EditText) {
        var user = auth.currentUser!!
        val email = email.text.toString()
        val password = password.text.toString()

        Log.d("user", user.toString())

        val credential = EmailAuthProvider
            .getCredential(email, password )

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
            .addOnCompleteListener { Log.d("auth", "User re-authenticated.") }

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("delete", "User account deleted.")
                    findNavController().navigate(R.id.action_nav_profile_to_nav_home)
                }
            }
    }

    private fun updateUser(image: String) {
        //Unique id generated by Firebase
        val id = auth.currentUser?.uid
        val imageUri = image
        //Data inputfields
        val naam = binding.etName.text.toString()
        val adres = Address(
            binding.etNumber.text.toString().toInt(),
            null,
            binding.etPostcode.text.toString(),
            binding.etAdress.text.toString(),
            binding.etLocation.text.toString()
        )

        if (id != null) {
            db.collection("users")
                .document(id)
                .update(
                    mapOf(
                        "naam" to naam,
                        "profileImage" to imageUri,
                        "address.housenumber" to adres.housenumber,
                        "address.place" to adres.place,
                        "address.postcode" to adres.postcode,
                        "address.street" to adres.street
                    )
                )
            Log.e("db upload", imageUri)
        }
        Toast.makeText(context, R.string.gewijzigd, Toast.LENGTH_LONG).show()
    }

    private fun updateUser() {
        //Unique id generated by Firebase
        val id = auth.currentUser?.uid
        //Data inputfields
        val naam = binding.etName.text.toString()
        val adres = Address(
            binding.etNumber.text.toString().toInt(),
            null,
            binding.etPostcode.text.toString(),
            binding.etAdress.text.toString(),
            binding.etLocation.text.toString()
        )

        if (id != null) {
            db.collection("users")
                .document(id)
                .update(
                    mapOf(
                        "naam" to naam,
                        "address.housenumber" to adres.housenumber,
                        "address.place" to adres.place,
                        "address.postcode" to adres.postcode,
                        "address.street" to adres.street
                    )
                )
        }
        Toast.makeText(context, R.string.gewijzigd, Toast.LENGTH_LONG).show()
    }



    private fun dataFetch(userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("clouddata fetching", "DocumentSnapshot data: ${document.data}")
                    var user = document.toObject<User>()
                    binddata(user)
                } else {
                    Log.d("clouddata fething", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("clouddata fething", "get failed with ", exception)
            }
    }

    private fun binddata(user: User?) {
        if (user != null) {
            binding.tvProfileName.text = user.naam
            binding.tvEmail.text = user.email

            binding.etName.setText(user.naam)
            binding.etAdress.setText(user.address?.street)
            user.address?.let { it.housenumber?.let { it1 -> binding.etNumber.setText(it1.toString()) } }
            binding.etPostcode.setText(user.address?.postcode)
            binding.etLocation.setText(user.address?.place)

            context?.let {
                Glide.with(it)
                    .load(user.profileImage)
                    .into(binding.ivProfile)
            }
        }

    }

    private fun imagePicker() {
        getContext()?.let {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(it, this)
        };
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                fileUri = resultUri
                binding.ivProfile.setImageURI(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun uploadFile() {

        if (fileUri != null) {
            var imagename = UUID.randomUUID().toString()
            var fileRef = imageReference?.child("$imagename.jpg")
            val uploadTask = fileRef!!.putFile(fileUri!!)

            uploadTask.addOnFailureListener {
                Toast.makeText(context, "Uploaden van foto is niet gelukt", Toast.LENGTH_LONG).show()
            }
                .addOnSuccessListener {
                    uploadTask.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception.let {
                                throw it!!
                            }
                        }
                        fileRef.downloadUrl

                    }
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result
                                Log.d("Download result", downloadUri.toString())
                                updateUser(downloadUri.toString())
                                binding.progressBar.isVisible = false
                            }
                        }
                }
                .addOnProgressListener {
                        binding.progressBar.isVisible = true
                    }

        }
        else{
            Log.e("Error", "No URI")
        }
    }



}