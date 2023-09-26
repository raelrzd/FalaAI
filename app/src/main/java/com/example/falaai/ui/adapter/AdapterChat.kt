package com.example.falaai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.R
import com.example.falaai.constants.Constants.Companion.KEY_USER
import com.example.falaai.databinding.ItemMessageBinding
import com.example.falaai.model.ModelMessage

class AdapterChat(
    private val context: Context,
    private var messageList: MutableList<ModelMessage>
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

    fun addMessage(chatMessage: ModelMessage) {
        messageList.add(chatMessage)
        notifyDataSetChanged()
    }


    inner class ViewHolder(
        private val context: Context,
        binding: ItemMessageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val messageBalloon = binding.messageBalloon
        private val messageView = binding.messageText


        fun bindView(message: ModelMessage) {
            if (message.role == KEY_USER) {
                setLayoutMessage(R.drawable.background_send_message, isSend = true)
            } else {
                setLayoutMessage(R.drawable.background_receive_message, isSend = false)
            }
            messageView.text = message.content.toString()
        }

        private fun setLayoutMessage(layout: Int, isSend: Boolean) {
            messageBalloon.background =
                AppCompatResources.getDrawable(context, layout)
            val layoutParams = messageBalloon.layoutParams as RelativeLayout.LayoutParams
            if (isSend) layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
            else layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
            messageBalloon.layoutParams = layoutParams
        }

    }

}



