package com.example.e_shelter

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.e_shelter.database.entities.Animal
import com.example.e_shelter.database.entities.Shelter
import java.io.*
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("yyyy-MM-dd")
        .format(systemTime).toString()
}

fun getAnimalPicFileName(currentAnimalId: Long?): String {
    return "animal_photo_".plus(App.userId.toString()).plus("_").plus(currentAnimalId.toString()).plus(".jpg")
}

fun getShelterPicFileName(): String {
    return "shelter_logo_".plus(App.userId.toString()).plus(".jpg")
}

fun saveToInternalStorage(bitmap: Bitmap, fileName: String): String {
    val cw = ContextWrapper(App.instance)

    val directory: File = cw.getDir("image_dir", Context.MODE_PRIVATE)
    val path = File(directory, fileName)

    var fos: FileOutputStream? = null
    try {
        fos = FileOutputStream(path)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        try {
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return path.absolutePath
}

fun loadImageFromStorage(path: String): Bitmap? {
    try {
        val file = File(path)
        return BitmapFactory.decodeStream(FileInputStream(file))
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
    return null
}

fun formatAge(animal: Animal): String {
    val age = animal.age.toString()
    return when (age.takeLast(1)) {
        "1" -> age.plus(" год")
        "2", "3", "4" -> age.plus(" года")
        else -> age.plus(" лет")
    }
}

fun formatShelterName(shelter: Shelter): String {
    return ("\"").plus(shelter.name).plus("\"")
}

fun formatAddress(shelter: Shelter): String {
    return ("г. ").plus(shelter.city).plus(" ").plus(shelter.address)
}