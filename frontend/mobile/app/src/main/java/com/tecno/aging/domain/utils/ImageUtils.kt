package com.tecno.aging.domain.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

fun convertUriToBase64(contentResolver: ContentResolver, uri: Uri): String? {
    return try {
        val inputStream = contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        val byteArray = outputStream.toByteArray()
        Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Formata uma string base64 para ser exibida em um componente de imagem
 * Adiciona o prefixo "data:image/jpeg;base64," se necessário
 * @param base64String String em base64 que pode ou não ter o prefixo
 * @return String formatada com o prefixo ou null se a string estiver vazia
 */
fun formatBase64ForDisplay(base64String: String?): String? {
    return base64String?.let {
        when {
            it.isBlank() -> null
            it.startsWith("data:image") -> it
            else -> "data:image/jpeg;base64,$it"
        }
    }
}

/**
 * Converte uma string base64 em um ImageBitmap para exibição no Compose
 * @param base64String String em base64 (com ou sem prefixo data:image)
 * @return ImageBitmap ou null se houver erro
 */
fun base64ToImageBitmap(base64String: String?): ImageBitmap? {
    if (base64String.isNullOrBlank()) {
        return null
    }

    return try {
        // Remove o prefixo data:image se existir
        val cleanBase64 = if (base64String.startsWith("data:image")) {
            base64String.substringAfter("base64,")
        } else {
            base64String
        }

        // Decodifica o base64 para byte array
        val decodedBytes = Base64.decode(cleanBase64, Base64.DEFAULT)

        // Converte byte array para Bitmap
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

        if (bitmap != null) {
            Log.d("ImageUtils", "Base64 convertido com sucesso. Tamanho: ${bitmap.width}x${bitmap.height}")
            bitmap.asImageBitmap()
        } else {
            Log.e("ImageUtils", "Erro ao decodificar bitmap a partir do base64")
            null
        }
    } catch (e: Exception) {
        Log.e("ImageUtils", "Erro ao converter base64 para ImageBitmap: ${e.message}", e)
        null
    }
}