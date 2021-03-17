package com.example.danshal.ui.admin

import android.app.DatePickerDialog
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
import com.example.danshal.databinding.AdminAddEventFragmentBinding
import com.example.danshal.models.Address
import com.example.danshal.models.Event
import com.example.danshal.models.Notification
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AdminAddEventFragment : Fragment() {

    private lateinit var adminAddEventViewModel: AdminAddEventViewModel

    private var _binding: AdminAddEventFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var date: Date

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adminAddEventViewModel =
            ViewModelProvider(this).get(AdminAddEventViewModel::class.java)

        _binding = AdminAddEventFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddEvent.setOnClickListener {
            postEvent()
        }

        setDate()
        addDatePicker()
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
            val event = Event(title!!, description!!, address, this.date, binding.switchAddExclusive.isChecked, R.drawable.event1, false)

            addToDatabase(event)
        } else {
            // TODO: Toast is not showing up
            Toast.makeText(context, "Er zijn een aantal verplichte velden niet ingevuld", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToDatabase(event: Event) {
        db.collection("events")
            .add(event)
            .addOnSuccessListener { documentReference ->
                Log.d("Cloud", "DocumentSnapshot added with ID: ${documentReference.id}")
                db.collection("notifications")
                    .add(Notification("Event toegevoegd: ${event.title}"))
                findNavController().navigate(R.id.action_adminAddEventFragment_to_nav_admin_dashboard)
                // TODO: Toast is not showing up
                Toast.makeText(context, "Event is toegevoegd", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("Cloud", "Error adding document", e)
                // TODO: Toast is not showing up
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