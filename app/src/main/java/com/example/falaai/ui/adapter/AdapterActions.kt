package com.example.falaai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.falaai.databinding.ItemSettingsActionBinding
import com.example.falaai.model.ModelAction

open class AdapterActions(
    private val context: Context,
    private val actions: List<ModelAction>,
) : RecyclerView.Adapter<AdapterActions.ViewHolder>() {

    private var onClickAction: ((action: ModelAction) -> Unit)? = null

    fun setOnCLickAction(callback: ((action: ModelAction) -> Unit)) {
        this.onClickAction = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSettingsActionBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding, onClickAction)
    }

    override fun getItemCount(): Int = actions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = actions[position]
        holder.bindView(action)
    }

    open class ViewHolder(
        binding: ItemSettingsActionBinding,
        private val onClickAction: ((action: ModelAction) -> Unit)?,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val icon: ImageView = binding.ItemActionImage
        private val description: TextView = binding.itemActionDescription

        fun bindView(action: ModelAction) {
            onClickAction?.let {
                itemView.setOnClickListener { it(action) }
            }
            icon.setImageResource(action.icon)
            description.text = action.title
        }
    }
}