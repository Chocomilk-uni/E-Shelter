package com.example.e_shelter.screens.shelteradmin.home

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.e_shelter.*
import com.example.e_shelter.database.entities.Animal
import com.example.e_shelter.database.entities.Shelter

class HomeShelterViewModel : ViewModel() {

    private val database = App.database.eShelterDatabaseDao

    var shelter = MutableLiveData<Shelter>()
    var logoPic: Bitmap? = null
    lateinit var animals: LiveData<List<Animal>>
    lateinit var dogs: LiveData<List<Animal>>
    lateinit var cats: LiveData<List<Animal>>
    lateinit var exotic: LiveData<List<Animal>>
    private val firebaseDatabase = App.firebaseDatabase
    private val firebaseAuth = App.firebaseAuth

    private var path: String? = null

    private val _editSuccess = MutableLiveData<Boolean?>()
    val editSuccess: LiveData<Boolean?>
        get() = _editSuccess

    init {
        initializeShelterAndUser()
    }

    fun doneShowingSnackBar() {
        _editSuccess.value = null
    }

    val addressString = Transformations.map(shelter) { shelter ->
        formatAddress(shelter!!)
    }

    val shelterNameString = Transformations.map(shelter) { shelter ->
        formatShelterName(shelter!!)
    }


    private fun initializeShelterAndUser() {
        val currentUser = database.getUser(firebaseAuth.user!!.uid)
        shelter.value = database.getShelter(currentUser!!.shelterId!!)
        animals = database.getAnimalsByShelter(currentUser.shelterId!!)

        dogs = database.getAllAnimalsBySpecies("Собака", currentUser.shelterId!!)
        cats = database.getAllAnimalsBySpecies("Кот", currentUser.shelterId!!)
        exotic = database.getAllExoticAnimals("Собака", "Кот", currentUser.shelterId!!)

        val shelterLogo = shelter.value!!.logoPic
        if (shelterLogo != null) {
            path = shelterLogo
            logoPic = loadImageFromStorage(path!!)
        }
    }

    fun onAttachPic(bitmap: Bitmap) {
        logoPic = bitmap
        path = saveToInternalStorage(bitmap, getShelterPicFileName())
    }

    fun onEdit(name: String, phoneNumber: String, city: String, address: String) {
        shelter.value!!.name = name
        shelter.value!!.address = address
        shelter.value!!.city = city
        shelter.value!!.phoneNumber = phoneNumber
        shelter.value!!.logoPic = path

        database.update(shelter.value!!)
        firebaseDatabase.shelterFirebase.sendShelter(shelter.value!!)

        initializeShelterAndUser()
        _editSuccess.value = true
    }
}