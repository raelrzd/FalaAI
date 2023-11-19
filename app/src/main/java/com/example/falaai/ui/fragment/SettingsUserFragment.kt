package com.example.falaai.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.R
import com.example.falaai.constant.EnumAction
import com.example.falaai.constant.EnumAlertType
import com.example.falaai.databinding.FragmentSettingsUserBinding
import com.example.falaai.extension.showMessage
import com.example.falaai.model.ModelAction
import com.example.falaai.permission.PermissionService
import com.example.falaai.storage.ChatStorage
import com.example.falaai.storage.UserStorage
import com.example.falaai.ui.adapter.AdapterActions
import com.example.falaai.ui.dialog.DialogSelectorImage
import com.example.falaai.ui.dialog.DialogValidation
import com.example.falaai.utils.loadImageFromAppData
import com.example.falaai.utils.saveImageToAppData
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView

class SettingsUserFragment : Fragment() {

    private var _binding: FragmentSettingsUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageViewUser: CircleImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterActions: AdapterActions
    private lateinit var listModelActions: MutableList<ModelAction>
    private val chatsFragment = HistoricChatFragment()

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
        setupActions()
        return view
    }

    private fun setupActions() {
        recyclerView = binding.settingsRecyclerActions
        listModelActions = mutableListOf(
            ModelAction(
                action = EnumAction.DELETE_ALL_CHATS,
                icon = R.drawable.ic_delete_chats,
                title = "Apagar todas as conversas",
                description = "Tem certeza que deseja apagar todas as conversas com IA?"
            ), ModelAction(
                action = EnumAction.DELETE_USER_DATA,
                icon = R.drawable.ic_delete_user,
                title = "Apagar configurações do usuário",
                description = "Tem certeza que deseja apagar os dados do usuário?"
            )
        )
        adapterActions = AdapterActions(requireContext(), listModelActions).apply {
            this.setOnCLickAction { action ->
                DialogValidation(requireContext(), action).apply {
                    setOnClickConfirm {
                        verifyActionSelected(action)
                    }
                    show()
                }
            }
        }
        recyclerView.adapter = adapterActions
    }

    private fun verifyActionSelected(action: ModelAction) {
        val appCompatActivity = requireActivity() as AppCompatActivity
        when (action.action) {
            EnumAction.DELETE_ALL_CHATS -> {
                ChatStorage(requireContext()).deleteList()
                chatsFragment.clearHistoricChat()
                appCompatActivity.showMessage(
                    "As conversas do app foram apagadas!", EnumAlertType.SUCCESS
                )
            }

            EnumAction.DELETE_USER_DATA -> {
                binding.settingsOutlinedTextField.editText?.setText("")
                imageViewUser.setImageResource(R.drawable.ic_add_photo)
                UserStorage(requireContext()).deleteUser()
                appCompatActivity.showMessage(
                    "As configurações do usuário foram apagadas!", EnumAlertType.SUCCESS
                )
            }

            else -> {}
        }
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
            if (PermissionService().allPermissionsGranted(requireContext())) openSelectorImage()
            else PermissionService().verifyPermissions(requireActivity())
        }
    }

    private fun setupButtonSaveUserName() {
        binding.settingsOutlinedTextField.setEndIconOnClickListener {
            val inputText = binding.settingsOutlinedTextField.editText?.text.toString()
            UserStorage(requireContext()).editUserName(inputText)
            val appCompatActivity = requireActivity() as AppCompatActivity
            appCompatActivity.showMessage("Nome atualizado com sucesso!", EnumAlertType.SUCCESS)
            val editText = binding.settingsInlinedTextField
            val imm = appCompatActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
        }
    }

    private fun hasUserName() {
        UserStorage(requireContext()).getUser()?.name?.let { userName ->
            if (binding.settingsOutlinedTextField.editText?.text.toString()
                    .isBlank()
            ) binding.settingsOutlinedTextField.editText?.setText(userName)
        }
    }

    fun openSelectorImage() {
        DialogSelectorImage().apply {
            setOnClickEditImage { imageUri ->
                UserStorage(requireContext()).editUserPhoto(imageUri)
                imageUri?.let { saveImageToAppData(requireContext(), it) }
                setupImageUser()
                val appCompatActivity = requireActivity() as AppCompatActivity
                appCompatActivity.showMessage("Foto atualizada com sucesso!", EnumAlertType.SUCCESS)
            }
        }.show(parentFragmentManager, null)
    }
}