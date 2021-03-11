package com.example.danshal.ui.admin

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.danshal.databinding.AdminAddEventFragmentBinding
import java.util.*

class AdminAddEventFragment : Fragment() {

    private lateinit var adminAddEventViewModel: AdminAddEventViewModel

    private var _binding: AdminAddEventFragmentBinding? = null
    private val binding get() = _binding!!

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

        addDatePicker()
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
                    binding.tvAddDate.text = "Datum: $dayOfMonth-$month-$year"
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