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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.danshal.R
import com.example.danshal.databinding.FragmentLoginBinding

class LoginFragment: Fragment() {
    lateinit var textView: TextView

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
//        createRegisterLink()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_blankFragment
            )
        }

        createRegisterLink()
    }

    private fun createRegisterLink() {
        Log.i("createRegisterLink", "first")
        textView = binding.tvLogin
        val text = binding.tvLogin.text.toString()
        Log.i("createRegisterLink", text)
        val spannableString = SpannableString(text)
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment
                )
            }
        }
        spannableString.setSpan(clickableSpan1, 14, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//
//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    setContentView(R.layout.activity_main)
//    textView = findViewById(R.id.textView)
//    val text = "I want THIS and THIS to be CLICKED"
//    val spannableString = SpannableString(text)
//    val clickableSpan1: ClickableSpan = object : ClickableSpan() {
//        override fun onClick(widget: View?) {
//            Toast.makeText(this@MainActivity, "THIS", Toast.LENGTH_SHORT).show()
//        }
//    }
//    val clickableSpan2: ClickableSpan = object : ClickableSpan() {
//        override fun onClick(widget: View?) {
//            Toast.makeText(this@MainActivity, "THIS", Toast.LENGTH_SHORT).show()
//        }
//    }
//    val clickableSpan3: ClickableSpan = object : ClickableSpan() {
//        override fun onClick(widget: View?) {
//            Toast.makeText(this@MainActivity, "Clicked", Toast.LENGTH_SHORT).show()
//        }
//    }
//    spannableString.setSpan(clickableSpan1, 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//    spannableString.setSpan(clickableSpan2, 16, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//    spannableString.setSpan(clickableSpan3, 27, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//    textView.setText(spannableString, TextView.BufferType.SPANNABLE)
//    textView.movementMethod = LinkMovementMethod.getInstance()