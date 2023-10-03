package com.example.falaai.permission

import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionService(private val keyMyPermissionsList: Int) {
    fun verifyPermissions(activity: Activity, permissions: Array<String>) {
        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(activity, permissions, keyMyPermissionsList)
        }
    }

    fun verifyPermissions(fragment: Fragment, permissions: Array<String>) {
        if (permissions.isNotEmpty()) {
            fragment.requestPermissions(permissions, keyMyPermissionsList)
        }
    }
}