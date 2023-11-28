package com.example.falaai.storage

import PreferencesStorage
import android.content.Context
import com.example.falaai.model.ModelChat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ChatStorage(context: Context) : PreferencesStorage(context = context) {
    private val keyChatList = "chatList"

    fun saveList(listFavorites: List<ModelChat>) {
        val jsonListTracked = Gson().toJson(listFavorites)
        getEditor().putString(keyChatList, jsonListTracked).commit()
    }

    fun getList(): MutableList<ModelChat> {
        val jsonListTracked = getPreferences().getString(keyChatList, "[]")
        return Gson().fromJson(
            jsonListTracked,
            object : TypeToken<MutableList<ModelChat>>() {}.type
        )
    }

    fun deleteList() {
        getEditor().remove(keyChatList).commit()
    }

    fun saveItem(modelChat: ModelChat) {
        val list = getList()
        list.find { it.id == modelChat.id }?.let {
            list[list.indexOf(it)] = modelChat
            saveList(list)
        } ?: run {
            list.add(modelChat)
            saveList(list)
        }
    }

    fun removeItem(modelChat: ModelChat) {
        val list = getList()
        list.find { it.id == modelChat.id }?.let {
            list.remove(it)
            saveList(list)
        }
    }
}