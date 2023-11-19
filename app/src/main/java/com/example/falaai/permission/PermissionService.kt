package com.example.falaai.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.falaai.constant.Constants.Companion.REQUEST_CODE_PERMISSIONS
import com.example.falaai.constant.Constants.Companion.REQUIRED_PERMISSIONS

class PermissionService {
    fun verifyPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
        )
    }
    fun allPermissionsGranted(context: Context) = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context, it
        ) == PackageManager.PERMISSION_GRANTED
    }
}