package com.example.falaai.extension

import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.falaai.R
import com.example.falaai.constant.EnumAlertType
import com.example.falaai.databinding.LayoutSnackBarBinding
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.showMessage(message: String, alertType: EnumAlertType) {
    var binding: LayoutSnackBarBinding

    this.runOnUiThread {
        val customSnackBar = Snackbar.make(
            window.decorView, "", Snackbar.LENGTH_LONG
        )
        val viewSnackBar = customSnackBar.view
        viewSnackBar.setBackgroundResource(android.R.color.transparent)
        val params = viewSnackBar.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        params.height = resources.getDimension(R.dimen.heightTopSnackBar).toInt()
        params.width = resources.displayMetrics.widthPixels
        params.topMargin = 0
        params.marginStart = 0
        viewSnackBar.layoutParams = params
        val layout = customSnackBar.view as Snackbar.SnackbarLayout
        binding = LayoutSnackBarBinding.inflate(layoutInflater)
        val view = binding.root
        binding.snackBarTitle.text = message
        binding.snackBarButtonClose.setOnClickListener {
            customSnackBar.dismiss()
        }
        when (alertType) {
            EnumAlertType.ERROR -> {
                binding.snackBarIcon.setImageResource(R.drawable.ic_alert_error)
                binding.snackBarRoot.setBackgroundResource(R.color.colorAlertError)
                binding.snackBarButtonClose.setColorFilter(
                    ContextCompat.getColor(
                        this, R.color.colorAlertErrorDark
                    )
                )
            }

            EnumAlertType.WARNING -> {
                binding.snackBarIcon.setImageResource(R.drawable.ic_warning)
                binding.snackBarRoot.setBackgroundResource(R.color.colorAlertWarning)
                binding.snackBarButtonClose.setColorFilter(
                    ContextCompat.getColor(
                        this, R.color.colorAlertWarningDark
                    )
                )
            }

            EnumAlertType.CANCELED -> {
                binding.snackBarIcon.setImageResource(R.drawable.ic_error)
                binding.snackBarRoot.setBackgroundResource(R.color.colorAlertError)
                binding.snackBarButtonClose.setColorFilter(
                    ContextCompat.getColor(
                        this, R.color.colorAlertErrorDark
                    )
                )
            }

            EnumAlertType.NO_CONNECTION -> {
                binding.snackBarIcon.setImageResource(R.drawable.ic_no_connection)
                binding.snackBarRoot.setBackgroundResource(R.color.colorAlertNoConnection)
                binding.snackBarButtonClose.setColorFilter(
                    ContextCompat.getColor(
                        this, R.color.colorAlertNoConnectionDark
                    )
                )
            }

            EnumAlertType.SUCCESS -> {
                binding.snackBarIcon.setImageResource(R.drawable.ic_success)
                binding.snackBarRoot.setBackgroundResource(R.color.colorAlertSuccess)
                binding.snackBarButtonClose.setColorFilter(
                    ContextCompat.getColor(
                        this, R.color.colorAlertSuccessDark
                    )
                )
            }
        }
        layout.setPadding(0, 0, 0, 0)
        layout.addView(view, 0)
        customSnackBar.duration = 3000
        customSnackBar.show()
    }
}

fun AppCompatActivity.setStatusBarTransparent() {
    window.statusBarColor = getColor(R.color.topBar)
}