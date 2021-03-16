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
import com.example.danshal.R
import com.example.danshal.databinding.AdminAddGiveAwayFragmentBinding
import com.example.danshal.models.GiveAway
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AdminAddGiveAwayFragment : Fragment() {

    private lateinit var adminAddGiveAwayViewModel: AdminAddGiveAwayViewModel

    private var _binding: AdminAddGiveAwayFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var date: Date

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        adminAddGiveAwayViewModel =
            ViewModelProvider(this).get(AdminAddGiveAwayViewModel::class.java)

        _binding = AdminAddGiveAwayFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddGiveAway.setOnClickListener {
            postGiveAway()
        }

        setDate()
        addDatePicker()
    }

    private fun setDate() {
        this.date = Calendar.getInstance().time
        // TODO: Year is not showing up right in view, but is right added to this.date
        binding.tvAddDate.text = "Datum: ${this.date.date}-${this.date.month + 1}-${this.date.year}"
    }

    private fun postGiveAway() {
        val title = binding.etAddTitle.text?.toString()
        val description = binding.etAddDescription.text?.toString()

        if (validate(title) && validate(description)) {
            val giveAway = GiveAway(title!!, description!!, emptyList(), this.date, R.drawable.event1)

            addToDatabase(giveAway)
        } else {
            // TODO: Toast is not showing up
            Toast.makeText(context, "Er zijn een aantal verplichte velden niet ingevuld", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToDatabase(giveAway: GiveAway) {
        db.collection("giveaways")
            .add(giveAway)
            .addOnSuccessListener { documentReference ->
                Log.d("Cloud", "DocumentSnapshot added with ID: ${documentReference.id}")
                // TODO: Toast is not showing up
                Toast.makeText(context, "Give away is toegevoegd", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.w("Cloud", "Error adding document", e)
                // TODO: Toast is not showing up
                Toast.makeText(context, "Het is niet gelukt de give away toe te voegen", Toast.LENGTH_SHORT).show()
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