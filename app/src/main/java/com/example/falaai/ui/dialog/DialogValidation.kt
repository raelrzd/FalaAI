package com.example.falaai.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.example.falaai.databinding.DialogValidationBinding
import com.example.falaai.model.ModelAction

class DialogValidation(context: Context, private val action: ModelAction) : Dialog(context) {

    private lateinit var binding: DialogValidationBinding
    private var onClickConfirm: (() -> Unit)? = null
    private var onClickCancel: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogValidationBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(view)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setupButtonCancel()
        setupButtonConfirm()
        setupIconAndTitle()
    }

    fun setOnClickConfirm(onClickConfirm: () -> Unit) {
        this.onClickConfirm = onClickConfirm
    }

    fun setOnClickCancel(onClickCancel: () -> Unit) {
        this.onClickCancel = onClickCancel
    }

    private fun setupIconAndTitle() {
        binding.dialogValidationIcon.setImageResource(action.icon)
        binding.dialogValidationTitle.text = action.description
    }

    private fun setupButtonConfirm() {
        binding.dialogValidationConfirm.setOnClickListener {
            dismiss()
            onClickConfirm?.let { it() }
        }
    }

    private fun setupButtonCancel() {
        binding.dialogValidationCancel.setOnClickListener {
            dismiss()
            onClickCancel?.let { it() }
        }
    }
}