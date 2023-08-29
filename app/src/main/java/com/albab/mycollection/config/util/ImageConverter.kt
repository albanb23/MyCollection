package com.albab.mycollection.config.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class ImageConverter {
    companion object {
        @TypeConverter
        fun bitmapToBase64(bitmap: Bitmap): String {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val bytes = baos.toByteArray()
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }

        @TypeConverter
        fun base64ToBitmap(base64: String): Bitmap {
            try {
                val encodedBytes = Base64.decode(base64, Base64.DEFAULT)
                return BitmapFactory.decodeByteArray(encodedBytes, 0, encodedBytes.size)
            } catch (e: Exception) {
                throw Exception(e.message)
            }
        }

        @TypeConverter
        fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return baos.toByteArray()
        }
    }
}