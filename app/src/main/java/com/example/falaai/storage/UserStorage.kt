package com.example.falaai.storage

import PreferencesStorage
import android.content.Context
import android.net.Uri
import com.example.falaai.model.ModelUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserStorage(context: Context) : PreferencesStorage(context = context) {
    private val keyUser = "user"

    private fun saveUser(contact: ModelUser) {
        val jsonContact = Gson().toJson(contact)
        val editor = getEditor()
        editor.putString(keyUser, jsonContact)
        editor.commit()
    }

    fun getUser(): ModelUser? {
        val jsonModelContact = getPreferences().getString(keyUser, "{}")
        return Gson().fromJson(jsonModelContact, object : TypeToken<ModelUser>() {}.type)
    }

    fun editUserName(name: String?) {
        val user = getUser()?.apply {
            this.name = name
        }
        if (user != null) {
            saveUser(user)
        }
    }

    fun editUserPhoto(imageUri: String?) {
        val user = getUser()?.apply {
            this.photo = imageUri
        }
        if (user != null) {
            saveUser(user)
        }
    }

}