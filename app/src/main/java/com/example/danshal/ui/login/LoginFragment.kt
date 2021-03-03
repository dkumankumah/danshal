package com.example.danshal.ui.login

import android.content.Intent
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.danshal.R
import com.example.danshal.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment: Fragment() {
    lateinit var textView: TextView
    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            if (validate()){
//                findNavController().navigate(
//                    R.id.action_loginFragment_to_blankFragment
//                )
                logIn(binding.etUsername.toString(), binding.etPassword.toString())
            }

        }

        createRegisterSpan()
    }

    //Creating clickable span
    private fun createRegisterSpan() {
        textView = binding.tvRegister
        val text = binding.tvRegister.text.toString()
        val spannableString = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment
                )
            }
        }
        spannableString.setSpan(clickableSpan, 14, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        textView.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun validate(): Boolean{
        return if (binding.etUsername.text.toString().isNotBlank() && binding.etPassword.text.toString().isNotBlank()){
            true
        }else{
            Toast.makeText(this.context, getString(R.string.empty_field), Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                findNavController().navigate(
                    R.id.action_loginFragment_to_blankFragment
                )
            }
            else{
                Toast.makeText(this.context, getString(R.string.login_failed), Toast.LENGTH_LONG)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}