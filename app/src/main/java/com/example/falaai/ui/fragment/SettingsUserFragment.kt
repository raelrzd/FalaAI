package com.example.falaai.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.R
import com.example.falaai.constants.EnumAction
import com.example.falaai.databinding.FragmentSettingsUserBinding
import com.example.falaai.model.ModelAction
import com.example.falaai.permission.PermissionService
import com.example.falaai.storage.ChatStorage
import com.example.falaai.storage.UserStorage
import com.example.falaai.ui.adapter.AdapterActions
import com.example.falaai.ui.dialog.DialogSelectorImage
import com.example.falaai.utils.loadImageFromAppData
import com.example.falaai.utils.saveImageToAppData
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
                EnumAction.DELETE_ALL_CONVERSATIONS,
                R.drawable.ic_delete_chats,
                "Apagar todas as conversas"
            ),
            ModelAction(
                EnumAction.DELETE_USER_DATA,
                R.drawable.ic_delete_user,
                "Apagar dados do usuário"
            )
        )
        adapterActions = AdapterActions(requireContext(), listModelActions).apply {
            this.setOnCLickAction { action ->
                verifyActionSelected(action)
            }
        }
        recyclerView.adapter = adapterActions
    }

    private fun verifyActionSelected(action: ModelAction) {
        when (action.action) {
            EnumAction.DELETE_ALL_CONVERSATIONS -> {
                ChatStorage(requireContext()).deleteList()
                chatsFragment.clearHistoricChat()
            }

            EnumAction.DELETE_USER_DATA -> {
                binding.settingsOutlinedTextField.editText?.setText("")
                imageViewUser.setImageResource(R.drawable.ic_add_photo)
                UserStorage(requireContext()).deleteUser()
            }

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
        }
    }

    private fun hasUserName() {
        UserStorage(requireContext()).getUser()?.name?.let { userName ->
            if (binding.settingsOutlinedTextField.editText?.text.toString().isBlank())
                binding.settingsOutlinedTextField.editText?.setText(userName)
        }
    }

    fun openSelectorImage() {
        DialogSelectorImage().apply {
            setOnClickEditImage { imageUri ->
                UserStorage(requireContext()).editUserPhoto(imageUri)
                imageUri?.let { saveImageToAppData(requireContext(), it) }
                setupImageUser()
            }
        }.show(parentFragmentManager, null)
    }
}