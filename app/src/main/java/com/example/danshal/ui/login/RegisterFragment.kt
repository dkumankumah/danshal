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
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import com.example.danshal.R
import com.example.danshal.databinding.FragmentRegisterBinding
import com.example.danshal.ui.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.StringBuilder

class RegisterFragment : Fragment() {
    lateinit var textView: TextView
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        //auth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_registerFragment_to_blankFragment
//            )
            if (validate()){
                createUser(binding.etUsername.text.toString(), binding.etPassword.text.toString())
            }
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

    private fun validate(): Boolean{
        return if (Patterns.EMAIL_ADDRESS.matcher(binding.etUsername.text.toString()).matches()
            && binding.etPassword.text.toString().isNotBlank()
            && binding.etPassword.text.length > 4
                && binding.etPasswordVal.text.isNotBlank()
                && binding.etName.text.isNotBlank()){
                passwordChecker()
        }else{
            Toast.makeText(this.context, getString(R.string.empty_field), Toast.LENGTH_LONG).show()
            false
        }

    }

    private fun passwordChecker(): Boolean {
        return if (binding.etPassword.text.toString().equals(binding.etPasswordVal.text.toString())){
            true
        }else{
            Toast.makeText(this.context, getString(R.string.wachtwoord_ongelijk), Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                //Unique id generated by Firebase
                val id = auth.currentUser?.uid

                //Data inputfields
                val naam = binding.etName.text.toString()
                val adres = binding.etAdres.text.toString()
                val postcode = binding.etPostcode.text.toString()
                val plaats = binding.etPlaats.text.toString()
                val email = email

                val user = id?.let { User(naam, adres, postcode, plaats, email, it, false ) }
                // Add a new document with a generated ID
                user?.let {
                    db.collection("users")
                            .add(it)
                            .addOnSuccessListener { documentReference ->
                                Log.d("Cloud", "DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Cloud", "Error adding document", e)
                            }
                }
                findNavController().navigate(
                    R.id.action_registerFragment_to_blankFragment
                )
                Log.e("Task", "Succes")
            }else{
                Log.e("Task", "Failed..."+task.exception)
            }

        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
//            reload();
            findNavController().navigate(
                R.id.action_registerFragment_to_blankFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}