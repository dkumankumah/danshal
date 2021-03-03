package com.example.danshal.ui.login

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
//        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            if (validate()){
//                findNavController().navigate(
//                    R.id.action_loginFragment_to_blankFragment
//                )
                logIn(binding.etUsername.text.toString(), binding.etPassword.text.toString())
            }

        }

        createRegisterSpan()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
//            reload();
            findNavController().navigate(
                R.id.action_loginFragment_to_blankFragment
            )
        }
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
        return if (Patterns.EMAIL_ADDRESS.matcher(binding.etUsername.text.toString()).matches() && binding.etPassword.text.toString().isNotBlank()){
            true
        }else{
            Toast.makeText(this.context, getString(R.string.empty_field), Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.e("Task", "Succes")
                findNavController().navigate(
                    R.id.action_loginFragment_to_blankFragment
                )
            }
            else{
                Log.e("Task", "Failed..."+task.exception)
                Toast.makeText(this.context, getString(R.string.login_failed), Toast.LENGTH_LONG)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}