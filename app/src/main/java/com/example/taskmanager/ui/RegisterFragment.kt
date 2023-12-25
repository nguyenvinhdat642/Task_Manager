package com.example.taskmanager.ui

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.taskmanager.MainActivity
import com.example.taskmanager.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class RegisterFragment : Fragment() {
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confilmpass: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var btnGoogle: ImageButton
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_register, container, false)
        auth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }!!

        email = view.findViewById(R.id.etEmail)
        password = view.findViewById(R.id.etPassword)
        confilmpass = view.findViewById(R.id.etConfilmPassword)
        btnRegister = view.findViewById(R.id.btnRegister)
        btnGoogle = view.findViewById(R.id.btnGoogle)

        btnGoogle.setOnClickListener {
            signInGoogle()
        }

        btnRegister.setOnClickListener {
            val enterEmail = email.text.toString().trim()
            val enterPassword = password.text.toString().trim()
            val confirmedPassword = confilmpass.text.toString().trim()

            if (enterEmail.isEmpty() || enterPassword.isEmpty() || confirmedPassword.isEmpty()) {
                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            } else if (enterPassword != confirmedPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(enterEmail, enterPassword)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            val user: FirebaseUser? = auth.currentUser
                            findNavController().navigate(R.id.action_registerFragment3_to_home)
                            requireActivity().finish()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
        return view

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){

            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account : GoogleSignInAccount? = task.result
            if(account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful){
                findNavController().navigate(R.id.action_registerFragment3_to_home)

            }else{
                Toast.makeText(context, it.exception.toString() , Toast.LENGTH_SHORT).show()

            }
        }
    }
}