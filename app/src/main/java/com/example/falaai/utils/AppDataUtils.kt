package com.example.falaai.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun loadImageFromAppData(context: Context): Bitmap {
    val file = File(context.filesDir, "image.jpg")
    val inputStream = FileInputStream(file)

    return BitmapFactory.decodeStream(inputStream)
}

fun saveImageToAppData(context: Context, imageUri: String) {
    val uri = Uri.parse(imageUri)
    val file = File(context.filesDir, "image.jpg")
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    val fos = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
    fos.close()
}