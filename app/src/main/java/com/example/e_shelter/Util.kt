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


object Constants {
    const val ADMINS_CHILD = "admins"
    const val USERS_CHILD = "users"
    const val SHELTERS_CHILD = "shelters"
    const val ANIMALS_CHILD = "animals"
    const val ADOPTION_APPLICATIONS_CHILD = "adoption_applications"
    const val SIGN_UP_APPLICATIONS_CHILD = "shelter_sign_up_applications"
    const val FAVOURITES_CHILD = "favourites"
}


@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("dd-MM-yyyy")
        .format(systemTime).toString()
}

@SuppressLint("SimpleDateFormat")
fun convertLongToPeriodDateString(dateFrom: Long, dateTo: Long): String {
    return SimpleDateFormat("dd-MM-yyyy")
        .format(dateFrom).toString().plus(" - ").plus(SimpleDateFormat("dd-MM-yyyy")
            .format(dateTo).toString())
}

fun getAnimalPicFileName(currentAnimalId: Long?): String {
    val firebaseAuth = App.firebaseAuth
    val currentUser = firebaseAuth.user

    return "animal_photo_".plus(currentUser!!.uid).plus("_").plus(currentAnimalId.toString()).plus(".jpg")
}

fun getShelterPicFileName(): String {
    val firebaseAuth = App.firebaseAuth
    val currentUser = firebaseAuth.user

    return "shelter_logo_".plus(currentUser!!.uid).plus(".jpg")
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

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 6 && password.matches("^[A-Za-z0-9]+\$".toRegex())
}

