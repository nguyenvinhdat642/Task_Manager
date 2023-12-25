package com.example.taskmanager.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.taskmanager.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AvatarFragment : Fragment() {
    private lateinit var btnLogout: MaterialButton
    private lateinit var btnProfile: MaterialButton
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var ivAvatar: ImageView
    private lateinit var auth: FirebaseAuth
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_avatar, container, false)
        auth = FirebaseAuth.getInstance()

        btnLogout = view.findViewById(R.id.btnLogout)
        btnProfile = view.findViewById(R.id.btnProfile)
        tvName = view.findViewById(R.id.name)
        tvEmail = view.findViewById(R.id.tvEmail)
        ivAvatar = view.findViewById(R.id.profile_image)

        setupButtons()
        loadUserProfile()

        return view
    }

    private fun setupButtons() {
        btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        btnProfile.setOnClickListener {
            findNavController().navigate(R.id.action_avatar_to_profileFragment)
        }
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
        builder.setMessage("Are you sure?")
        builder.setPositiveButton("Yes") { _, _ ->
            performLogout()
        }
        builder.setNegativeButton("No", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun performLogout() {
        auth.signOut()
        clearUserLoginInfo()
        findNavController().navigate(R.id.action_avatar_to_loginFragment)
    }

    private fun clearUserLoginInfo() {
        val sharedPreferences = requireActivity().getPreferences(android.content.Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("userEmail")
        editor.apply()
    }

    private fun loadUserProfile() {
        currentUser = auth.currentUser
        currentUser?.let { user ->
            tvName.text = user.displayName
            tvEmail.text = user.email

            val photoUrl = user.photoUrl
            photoUrl?.let {
                Glide.with(requireContext())
                    .load(it)
                    .into(ivAvatar)
            }
        }
    }
}
