package com.example.taskmanager.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.taskmanager.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.grpc.Context

class AvatarFragment : Fragment() {
    private lateinit var btnLogout: MaterialButton
    private lateinit var auth: FirebaseAuth
    private lateinit var btnProfile: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_avatar, container, false)
        auth = FirebaseAuth.getInstance()
        btnLogout = view.findViewById(R.id.btnLogout)
        btnProfile = view.findViewById(R.id.btnProfile)

        setupButtons()

        return view
    }

    private fun setupButtons() {
        btnLogout.setOnClickListener {
            auth.signOut()
            clearUserLoginInfo()
            findNavController().navigate(R.id.action_avatar_to_loginFragment)
        }

        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_avatar_to_profileFragment)
        }
    }

    private fun clearUserLoginInfo() {
        val sharedPreferences = requireActivity().getPreferences(android.content.Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("userEmail")
        editor.apply()
    }
}
