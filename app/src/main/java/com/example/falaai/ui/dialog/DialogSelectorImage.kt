package com.example.falaai.ui.dialog

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.falaai.databinding.DialogSelectorImageBinding

const val KEY_START_CAMERA = 1717
const val KEY_START_GALLERY = 1728

class DialogSelectorImage() : DialogFragment() {

    private var _binding: DialogSelectorImageBinding? = null
    private val binding get() = _binding!!
    private var onClickEditImage: ((imageUri: String?) -> Unit)? = null
    private val imageUri: Uri? by lazy {
        val values = ContentValues()
        requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DialogSelectorImageBinding.inflate(inflater, container, false)
        val view = binding.root
        this.dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.dialogLayoutCamera.setOnClickListener {
            openCamera()
        }

        binding.dialogLayoutGallery.setOnClickListener {
            openGallery()
        }

        binding.dialogLayoutNoPhoto.setOnClickListener {
            onResultSelectImage(null)
        }



        return view
    }

    private fun openCamera() {
        val goCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        goCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(goCamera, KEY_START_CAMERA)
    }

    private fun openGallery() {
        val goGallery = Intent(Intent.ACTION_GET_CONTENT)
        goGallery.type = "image/*"
        startActivityForResult(goGallery, KEY_START_GALLERY)
    }

    fun setOnClickEditImage(onClickEditImage: (imageUri: String?) -> Unit) {
        this.onClickEditImage = onClickEditImage
    }

    private fun onResultSelectImage(imageUri: String?) {
        dismiss()
        onClickEditImage?.let { it(imageUri) }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            KEY_START_CAMERA -> {
                if (resultCode == Activity.RESULT_OK) {
                    onResultSelectImage(imageUri!!.toString())
                }
            }

            KEY_START_GALLERY -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val uriImage: Uri = data.data!!
                    onResultSelectImage(uriImage.toString())
                }
            }
        }
    }

}