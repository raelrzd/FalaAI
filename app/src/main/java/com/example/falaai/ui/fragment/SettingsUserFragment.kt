package com.example.falaai.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.falaai.R
import com.example.falaai.constants.Constants.Companion.KEY_PERMISSIONS_PHOTO
import com.example.falaai.databinding.FragmentSettingsUserBinding
import com.example.falaai.permission.PermissionService
import com.example.falaai.storage.UserStorage
import com.example.falaai.ui.dialog.DialogSelectorImage
import com.example.falaai.utils.loadImageFromAppData
import com.example.falaai.utils.saveImageToAppData
import de.hdodenhof.circleimageview.CircleImageView

class SettingsUserFragment : Fragment() {

    private var _binding: FragmentSettingsUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageViewUser: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsUserBinding.inflate(inflater, container, false)
        val view = binding.root
        setupButtonSaveUserName()
        hasUserName()
        setupImageUser()
        setupClickEditPhoto()
        return view
    }

    private fun setupImageUser() {
        imageViewUser = binding.settingsImageUser
        val user = UserStorage(requireContext()).getUser()
        if (user?.photo != null) {
            imageViewUser.setImageBitmap(loadImageFromAppData(requireContext()))
        } else {
            imageViewUser.setImageResource(R.drawable.ic_add_photo)
        }
    }

    private fun setupClickEditPhoto() {
        binding.settingsImageUser.setOnClickListener {
            val permissionCamera = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
            PermissionService(KEY_PERMISSIONS_PHOTO).verifyPermissions(this, permissionCamera)
        }
    }

    private fun setupButtonSaveUserName() {
        binding.settingsOutlinedTextField.setEndIconOnClickListener {
            val inputText = binding.settingsOutlinedTextField.editText?.text.toString()
            UserStorage(requireContext()).editUserName(inputText)
        }
    }

    private fun hasUserName() {
        UserStorage(requireContext()).getUser()?.name?.let { userName ->
            if (binding.settingsOutlinedTextField.editText?.text.toString().isBlank())
                binding.settingsOutlinedTextField.editText?.setText(userName)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            KEY_PERMISSIONS_PHOTO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSelectorImage()
                }
            }
        }
    }

    private fun openSelectorImage() {
        DialogSelectorImage().apply {
            setOnClickEditImage { imageUri ->
                UserStorage(requireContext()).editUserPhoto(imageUri)
                imageUri?.let { saveImageToAppData(requireContext(), it) }
                setupImageUser()
            }
        }.show(parentFragmentManager, null)
    }
}