package com.example.e_shelter.screens.shelteradmin.home

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.e_shelter.*
import com.example.e_shelter.database.entities.Animal
import com.example.e_shelter.database.entities.Shelter
import com.example.e_shelter.database.entities.User
import kotlinx.coroutines.launch

class HomeShelterViewModel : ViewModel() {

    private val database = App.database.eShelterDatabaseDao

    val shelter = MutableLiveData<Shelter>()
    var animals = database.getAnimalsByShelter(App.userId)
    var logoPic: Bitmap? = null
    lateinit var dogs: LiveData<List<Animal>>
    lateinit var cats: LiveData<List<Animal>>
    lateinit var exotic: LiveData<List<Animal>>
    private var currentUser = MutableLiveData<User>()

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
        currentUser.value = database.getUser(App.userId)
        shelter.value = database.getShelter(currentUser.value!!.shelterId!!)

        dogs = database.getAnimalsBySpecies("Собака", currentUser.value!!.shelterId!!)
        cats = database.getAnimalsBySpecies("Кот", currentUser.value!!.shelterId!!)
        exotic = database.getExoticAnimals("Собака","Кот", currentUser.value!!.shelterId!!)

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

        viewModelScope.launch {
            update(shelter.value!!)
            initializeShelterAndUser()
            _editSuccess.value = true
        }
    }

    private suspend fun update(shelter: Shelter) {
        database.update(shelter)
    }
}