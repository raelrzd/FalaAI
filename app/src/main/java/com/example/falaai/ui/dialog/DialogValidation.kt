package com.example.falaai.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.example.falaai.constants.EnumAction
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
        when(action.action) {
            EnumAction.DELETE_ALL_CONVERSATIONS -> {
                binding.dialogValidationIcon.setImageResource(action.icon)
                binding.dialogValidationTitle.text = "Tem certeza que deseja apagar todas as conversas com IA?"
            }

            EnumAction.DELETE_USER_DATA -> {
                binding.dialogValidationIcon.setImageResource(action.icon)
                binding.dialogValidationTitle.text = "Tem certeza que deseja apagar os dados do usuário?"
            }
        }
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