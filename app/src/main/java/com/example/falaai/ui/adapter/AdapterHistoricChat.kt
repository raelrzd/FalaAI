package com.example.falaai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.databinding.ItemChatBinding
import com.example.falaai.model.ModelChat

class AdapterHistoricChat(
    private val context: Context,
    private var chatList: MutableList<ModelChat>,
) : RecyclerView.Adapter<AdapterHistoricChat.ViewHolder>() {
    private var clickChat: ((tracked: ModelChat) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(context, clickChat, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(chatList[position], position)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    fun addMessage(modelChat: ModelChat) {
        chatList.add(modelChat)
        notifyDataSetChanged()
    }

    fun setOnClickChat(clickChat: (tracked: ModelChat) -> Unit) {
        this.clickChat = clickChat
    }

    fun clear() {
        chatList.clear()
        notifyDataSetChanged()
    }


    inner class ViewHolder(
        private val context: Context,
        private var clickChat: ((tracked: ModelChat) -> Unit)? = null,
        binding: ItemChatBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val itemChatView = binding.itemChatView
        private val title = binding.itemChatTitle

        fun bindView(modelChat: ModelChat, position: Int) {

            itemChatView.setOnClickListener {
                clickChat?.let { it(modelChat) }
            }

            if (modelChat.messages.isNotEmpty()) {
                title.text = modelChat.messages[0].content
            }

        }

    }

}



