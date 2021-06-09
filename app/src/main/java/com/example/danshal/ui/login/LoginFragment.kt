package com.example.danshal.ui.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.danshal.R
import com.example.danshal.SharedUserViewModel
import com.example.danshal.databinding.LoginFragmentBinding
import com.example.danshal.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private val viewModel: SharedUserViewModel by activityViewModels()

    lateinit var textView: TextView
    lateinit var wachtwoord: TextView

    private lateinit var auth: FirebaseAuth

    private lateinit var loginViewModel: LoginViewModel

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)

        auth = Firebase.auth

        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createRegisterSpan()
        createPasswordSpan()
        binding.btnLogin.setOnClickListener {
            if (validate()){
                logIn(binding.etUsername.text.toString(), binding.etPassword.text.toString())
            }
        }

    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            findNavController().navigate(
                R.id.action_nav_login_to_nav_home
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
                    R.id.action_nav_login_to_nav_register
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
        } else{
            Toast.makeText(this.context, getString(R.string.empty_field), Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.e("Task", "Succes")
                auth.currentUser?.let { dataFetch(it.uid) }
                viewModel.checkLoggedIn()
            }
            else{
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, getString(R.string.invalid_password), Toast.LENGTH_LONG).show()
                }
                if (task.exception is FirebaseAuthInvalidUserException) {
                    Toast.makeText(context, getString(R.string.invalid_username), Toast.LENGTH_LONG).show()
                }
                Log.e("Task", "Failed..." + task.exception)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dataFetch(userId: String) {
        val docRef = db.collection("users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d("clouddata fetching", "DocumentSnapshot data: ${document.data}")
                    val user = document.toObject<User>()
                    if (user != null) {
                        if (user.admin) {
                            findNavController().navigate(
                                R.id.action_nav_login_to_nav_admin_dashboard
                            )
                        } else findNavController().navigate(
                            R.id.action_nav_login_to_nav_home
                        )
                    }
                } else {
                    Log.d("clouddata fething", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("clouddata fething", "get failed with ", exception)
            }
    }

    //Creating clickable span
    private fun createPasswordSpan() {
        wachtwoord = binding.tvWachtwoord
        val text = binding.tvWachtwoord.text.toString()
        val spannableString = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                showForm()
            }
        }
        spannableString.setSpan(clickableSpan, 0, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        wachtwoord.setText(spannableString, TextView.BufferType.SPANNABLE)
        wachtwoord.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showForm() {
//        val builder = AlertDialog.Builder(requireContext())
//        val dialogLayout = layoutInflater.inflate(R.layout.reset_form_dialog, null, false)
//
//        builder.setView(dialogLayout)
//        builder.setCancelable(false)
//
//        val email = dialogLayout.findViewById<EditText>(R.id.et_dialog_email)
//
//        builder.setPositiveButton("Verzenden") { dialog, _ ->
//            if (email.text.isNotBlank()){
//                Firebase.auth.sendPasswordResetEmail(email.text.toString())
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d("Firebase", "Email sent.")
//                            Toast.makeText(
//                                context, R.string.verificatie,
//                                Toast.LENGTH_LONG
//                            ).show()
//
//                            dialog.dismiss()
//                        }
//                    }
//            }
//            else {
//                Toast.makeText(
//                    context,
//                    R.string.email_invoeren,
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//
//        builder.setNegativeButton("Annuleren") { dialog, _ ->
//            dialog.cancel()
//        }
//
//
//        builder.show()

        val popup = PopupWindow(context)
        val view = layoutInflater.inflate(R.layout.reset_form_dialog, null)

        popup.contentView = view
        popup.isOutsideTouchable = true
        popup.width = ViewGroup.LayoutParams.MATCH_PARENT
        popup.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val email = view.findViewById<EditText>(R.id.et_dialog_email)
        val annuleren = view.findViewById<TextView>(R.id.tv_annuleren)
        val versturen = view.findViewById<TextView>(R.id.tv_versturen)

        versturen.setOnClickListener {
            if (email.text.isNotBlank()){
                Firebase.auth.sendPasswordResetEmail(email.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Firebase", "Email sent.")
                            Toast.makeText(
                                context, R.string.verificatie,
                                Toast.LENGTH_LONG
                            ).show()

                            popup.dismiss()
                        }
                    }
            }
            else {
                Toast.makeText(
                    context,
                    R.string.email_invoeren,
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        annuleren.setOnClickListener {
            popup.dismiss()
        }

        popup.showAtLocation(this.view, Gravity.CENTER, 0,0)
    }

}