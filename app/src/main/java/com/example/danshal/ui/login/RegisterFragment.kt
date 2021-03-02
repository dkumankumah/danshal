package com.example.danshal.ui.login

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import com.example.danshal.R
import com.example.danshal.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    lateinit var textView: TextView

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerFragment_to_blankFragment
            )
        }

        createLoginSpan()
    }

    //Creating clickable span
    private fun createLoginSpan() {
        textView = binding.tvLogin
        val text = binding.tvLogin.text.toString()
        val spannableString = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                findNavController().navigate(
                    R.id.action_registerFragment_to_loginFragment
                )
            }
        }
        spannableString.setSpan(clickableSpan, 16, 23, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}