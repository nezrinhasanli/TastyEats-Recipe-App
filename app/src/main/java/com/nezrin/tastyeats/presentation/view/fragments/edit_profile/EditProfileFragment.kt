package com.nezrin.tastyeats.presentation.view.fragments.edit_profile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.nezrin.tastyeats.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint

class EditProfileFragment : Fragment() {

private lateinit var binding:FragmentEditProfileBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionResultLauncher: ActivityResultLauncher<String>
    private lateinit var storage: FirebaseStorage
    var selectPicture: Uri? = null
    private val firestore= Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentEditProfileBinding.inflate(inflater,container,false)

        storage=Firebase.storage

        getCurrentUsername()
        registerLauncher()

        binding.cancelButton.setOnClickListener {
            findNavController().navigate(EditProfileFragmentDirections.fromEditToProfile())
        }

        binding.editPicture.setOnClickListener {
            selectImage(it)
        }

        binding.saveButton.setOnClickListener {
            upload()
        }

        return binding.root
    }
    private fun registerLauncher() {

        //galeriyadan sekli goturub qayidandan sonra gorulen is

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intentFromResult = result.data

                    if (intentFromResult != null) {
                        selectPicture = intentFromResult.data
                        selectPicture?.let {
                            binding.profileImage.setImageURI(it)
                        }
                    }
                }

            }

        //allow use gallery

        permissionResultLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {

                if (it) {

                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)

                } else {

                    Toast.makeText(requireContext(), "Permission needed", Toast.LENGTH_SHORT).show()

                }


            }


    }

    fun selectImage(view: View) {

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Give permission") {
                        permissionResultLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    }.show()
            } else {

                permissionResultLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)

            }

        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)


        }


    }

    fun upload() {

        val progress = ProgressDialog(requireContext())
        progress.setMessage("Please wait updating profile")
        progress.show()

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference
        val imageReference = reference.child("profileImage/$imageName")

        if (selectPicture != null) {
            imageReference.putFile(selectPicture!!).addOnSuccessListener {
                val uploadPictureReference = storage.reference.child("profileImage").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    val ref = firestore.collection("Users").document(Firebase.auth.currentUser!!.uid)


                    ref.update("imageUrl", downloadUrl)
                    ref.update("username", binding.username.text.toString()).addOnSuccessListener {
                        progress.dismiss()
                        findNavController().navigate(EditProfileFragmentDirections.fromEditToProfile())

                    }

                }


            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        } else {
            val ref = firestore.collection("Users").document(Firebase.auth.currentUser!!.uid)

            ref.update("username", binding.username.text.toString()).addOnSuccessListener {
                progress.dismiss()
                findNavController().navigate(EditProfileFragmentDirections.fromEditToProfile())

            }

        }

    }

    private fun getCurrentUsername(){
        Firebase.firestore.collection("Users").document(Firebase.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { value ->
            if (value!=null){
                val username=value.get("username") as String
                binding.username.setText(username)
                val imageUrl=value.get("imageUrl") as String
                Glide.with(requireContext()).load(imageUrl).into(binding.profileImage)
            }
        }

    }
}