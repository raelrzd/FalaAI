package com.example.falaai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.R
import com.example.falaai.constants.Constants.Companion.KEY_ASSISTANT
import com.example.falaai.constants.Constants.Companion.KEY_ERROR_MESSAGE
import com.example.falaai.constants.Constants.Companion.KEY_LOADING_MESSAGE
import com.example.falaai.constants.Constants.Companion.KEY_USER
import com.example.falaai.databinding.ItemMessageBinding
import com.example.falaai.model.ModelMessage

class AdapterChat(
    private val context: Context,
    private var messageList: MutableList<ModelMessage>,
) : RecyclerView.Adapter<AdapterChat.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(context, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(messageList[position])
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun addLoadingMessage() {
        messageList.add(ModelMessage(role = KEY_LOADING_MESSAGE, content = ""))
        notifyItemInserted(messageList.size-1)
    }

    fun addReceiveMessage(chatMessage: ModelMessage) {
        if (chatMessage.role == KEY_ASSISTANT) {
            replaceLoading(chatMessage)
        }
    }

    fun addErrorMessage(chatMessage: ModelMessage) {
        if (chatMessage.role == KEY_ERROR_MESSAGE) {
            replaceLoading(chatMessage)
        }
    }

    private fun replaceLoading(chatMessage: ModelMessage) {
        val positionLoading =
            messageList.find { it.role == KEY_LOADING_MESSAGE }?.let { messageList.indexOf(it) }
        positionLoading?.let {
            messageList[positionLoading] = chatMessage
            notifyItemChanged(positionLoading)
        }
    }

    inner class ViewHolder(
        private val context: Context,
        binding: ItemMessageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val messageBalloon = binding.messageBalloon
        private val messageView = binding.messageText
        private val loadingAnimation = binding.messageLoadingAnimation
        private val receiveAnimation = binding.messageReceiveAnimation
        private val imageUser = binding.messageImageUser


        fun bindView(message: ModelMessage) {
            if (message.role == KEY_USER) {
                setLayoutMessage(R.drawable.background_send_message, isSend = true)
            }
            if (message.role == KEY_ASSISTANT) {
                setLayoutMessage(R.drawable.background_receive_message, isSend = false)
            }
            if (message.role == KEY_LOADING_MESSAGE) {
                messageBalloon.visibility = View.GONE
                imageUser.visibility = View.GONE
                receiveAnimation.visibility = View.GONE
                loadingAnimation.visibility = View.VISIBLE
            }
            if (message.role == KEY_ERROR_MESSAGE) {
                imageUser.visibility = View.GONE
                loadingAnimation.visibility = View.GONE
                receiveAnimation.visibility = View.VISIBLE
                messageBalloon.visibility = View.VISIBLE
                messageBalloon.background =
                    AppCompatResources.getDrawable(context, R.drawable.background_receive_message)
                val layoutParams = messageBalloon.layoutParams as RelativeLayout.LayoutParams
                receiveAnimation.setAnimation(R.raw.messagefailed)
                layoutParams.marginStart = 0
                layoutParams.removeRule(RelativeLayout.START_OF)
                layoutParams.addRule(RelativeLayout.END_OF, R.id.messageReceiveAnimation)
                messageBalloon.layoutParams = layoutParams
            }


            messageView.text = message.content
        }

        private fun setLayoutMessage(layout: Int, isSend: Boolean) {
            loadingAnimation.visibility = View.GONE
            messageBalloon.visibility = View.VISIBLE
            messageBalloon.background =
                AppCompatResources.getDrawable(context, layout)
            val layoutParams = messageBalloon.layoutParams as RelativeLayout.LayoutParams
            if (isSend) {
                imageUser.visibility = View.VISIBLE
                receiveAnimation.visibility = View.GONE
                layoutParams.addRule(RelativeLayout.START_OF, R.id.messageImageUser)
                layoutParams.removeRule(RelativeLayout.END_OF)
                layoutParams.marginStart = 16
            } else {
                imageUser.visibility = View.GONE
                receiveAnimation.setAnimation(R.raw.hello)
                receiveAnimation.visibility = View.VISIBLE
                layoutParams.marginStart = 0
                layoutParams.removeRule(RelativeLayout.START_OF)
                layoutParams.addRule(RelativeLayout.END_OF, R.id.messageReceiveAnimation)
            }
            messageBalloon.layoutParams = layoutParams
        }

    }

}



