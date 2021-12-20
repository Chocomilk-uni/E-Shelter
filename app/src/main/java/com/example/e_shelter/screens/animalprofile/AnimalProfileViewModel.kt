package com.example.e_shelter.screens.animalprofile

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.e_shelter.*
import com.example.e_shelter.database.entities.*
import kotlinx.coroutines.launch

class AnimalProfileViewModel(private val animalId: Long = 0L) : ViewModel() {

    private val database = App.database.eShelterDatabaseDao
    var animal = MutableLiveData<Animal>()
    private var shelter = MutableLiveData<Shelter>()
    private var currentUser = MutableLiveData<User>()
    private var favouritesEntry = MutableLiveData<Favourites?>()
    var profilePic: Bitmap? = null
    var isAdmin: Boolean = false
    var favouritesEntryExist: Boolean = false

    private val _addToFavouritesSuccess = MutableLiveData<Boolean?>()
    val addToFavouritesSuccess: LiveData<Boolean?>
        get() = _addToFavouritesSuccess

    private val _deleteFromFavouritesSuccess = MutableLiveData<Boolean?>()
    val deleteFromFavouritesSuccess: LiveData<Boolean?>
        get() = _deleteFromFavouritesSuccess

    private val _applicationAlreadyExistError = MutableLiveData<Boolean?>()
    val applicationAlreadyExistError: LiveData<Boolean?>
        get() = _applicationAlreadyExistError

    private val _applicationCreateSuccess = MutableLiveData<Boolean?>()
    val applicationCreateSuccess: LiveData<Boolean?>
        get() = _applicationCreateSuccess

    fun doneShowingSnackBar() {
        _addToFavouritesSuccess.value = null
        _deleteFromFavouritesSuccess.value = null
        _applicationCreateSuccess.value = null
        _applicationAlreadyExistError.value = null
    }

    init {
        initializeAnimalAndUser()
    }

    val shelterNameString = Transformations.map(shelter) { shelter ->
        formatShelterName(shelter!!)
    }

    val addressString = Transformations.map(shelter) { shelter ->
        formatAddress(shelter!!)
    }

    val ageString = Transformations.map(animal) { animal ->
        formatAge(animal!!)
    }

    private fun initializeAnimalAndUser() {
        animal.value = database.getAnimal(animalId)
        shelter.value = database.getShelter(animal.value!!.shelterId)
        profilePic = loadImageFromStorage(animal.value!!.profilePicPath!!)

        currentUser.value = database.getUser(App.userId)
        if (currentUser.value!!.shelterId != null)
            isAdmin = true
        else {
            favouritesEntry.value = database.getFavouritesEntry(currentUser.value!!.id, animal.value!!.id)
            if (favouritesEntry.value != null) favouritesEntryExist = true
        }
    }

    fun onAddToFavourites() {
        if (!favouritesEntryExist) {
            val newFavouritesEntry =
                Favourites(animalId = animal.value!!.id, userId = currentUser.value!!.id)

            viewModelScope.launch {
                insert(newFavouritesEntry)
                favouritesEntryExist = true
                _addToFavouritesSuccess.value = true
            }
        }

        else {
            viewModelScope.launch {
                delete(favouritesEntry.value!!)
                favouritesEntryExist = false
                _deleteFromFavouritesSuccess.value = true
            }
        }
    }

    fun onApplyForAdoption() {
        if (!checkIfApplicationExist()) {
            val newAdoptionApplication = AdoptionApplication(name = currentUser.value!!.name!!, phoneNumber = currentUser.value!!.phoneNumber!!,
            date = System.currentTimeMillis(), animalId = animal.value!!.id, userId = currentUser.value!!.id)

            viewModelScope.launch {
                insert(newAdoptionApplication)
                _applicationCreateSuccess.value = true
            }
        }
        else _applicationAlreadyExistError.value = true
    }

    private fun checkIfApplicationExist(): Boolean {
        val application = database.getApplicationByUserAnimal(currentUser.value!!.id, animal.value!!.id)
        return application != null
    }

    private suspend fun insert(favourite: Favourites) {
        database.insert(favourite)
    }

    private suspend fun insert(application: AdoptionApplication) {
        database.insert(application)
    }

    private suspend fun delete(favourite: Favourites) {
        database.delete(favourite)
    }
}