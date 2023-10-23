package com.example.falaai.constants

import android.Manifest
import android.os.Build

interface Constants {
    companion object {
        const val KEY_OPEN_CHAT = "openChat"
        const val KEY_USER = "user"
        const val KEY_ASSISTANT = "assistant"
        const val KEY_LOADING_MESSAGE = "loading"
        const val KEY_ERROR_MESSAGE = "error"
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }.toTypedArray()


    }
}