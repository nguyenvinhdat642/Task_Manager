package com.example.taskmanager.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.taskmanager.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class ProfileFragment : Fragment() {
    private lateinit var name: TextInputEditText
    private lateinit var profession: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var btnAvatar: ImageButton
    private lateinit var avatar: ImageView
    private lateinit var location: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var btnBack: MaterialButton

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        name = view.findViewById(R.id.etName)
        profession = view.findViewById(R.id.etProfesson)
        email = view.findViewById(R.id.etEmail)
        location = view.findViewById(R.id.etLocation)
        btnAvatar = view.findViewById(R.id.btnAvatar)
        avatar = view.findViewById(R.id.profile_image)
        btnSave = view.findViewById(R.id.btnSave)
        btnBack = view.findViewById(R.id.navigate2)

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        name.setText(user?.displayName)
        email.setText(user?.email)

        val avatarUrl = user?.photoUrl
        if (avatarUrl != null) {
            Glide.with(this)
                .load(avatarUrl)
                .into(avatar)
        }

        btnAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSave.setOnClickListener {
            updateUserInfo()
        }

        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_avatar)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data

            if (imageUri != null) {
                uploadImage(imageUri)
            }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val imageName = "avatars/${UUID.randomUUID()}.jpg"
        val imageRef = storageReference.child(imageName)

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading")
        progressDialog.show()

        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                progressDialog.dismiss()
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build()

                    FirebaseAuth.getInstance().currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                showUploadSuccessDialog()
                                updateUserInfo()
                                updateProfession(profession.text.toString())
                            } else {
                                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showUploadSuccessDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Success")
        builder.setMessage("Image uploaded successfully!")
        builder.setPositiveButton("OK") { _, _ ->
        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun updateUserInfo() {
        val newName = name.text.toString()

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        FirebaseAuth.getInstance().currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_profileFragment_to_avatar)

                } else {
                    Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateProfession(newProfession: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId)
            val professionMap = HashMap<String, Any>()
            professionMap["profession"] = newProfession

            databaseReference.updateChildren(professionMap)
                .addOnSuccessListener {
                    Toast.makeText(context, "Profession updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to update profession", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}
