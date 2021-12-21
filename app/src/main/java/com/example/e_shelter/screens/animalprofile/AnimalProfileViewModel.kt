package com.example.e_shelter.screens.animalprofile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.e_shelter.*
import com.example.e_shelter.database.entities.*

class AnimalProfileViewModel(private val animalId: Long = 0L) : ViewModel() {

    private val database = App.database.eShelterDatabaseDao
    private val firebaseDatabase = App.firebaseDatabase
    private val firebaseAuth = App.firebaseAuth
    private lateinit var currentUser: User
    var animal = MutableLiveData<Animal>()
    private var shelter = MutableLiveData<Shelter>()
    private var favouritesEntry: Favourites? = null
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

        currentUser =  database.getUser(firebaseAuth.user!!.uid)!!
        if (currentUser.shelterId != null)
                isAdmin = true
            else {
                favouritesEntry = database.getFavouritesEntry(currentUser.uid, animal.value!!.id)
                if (favouritesEntry != null) favouritesEntryExist = true
            }
    }

    fun onAddToFavourites() {
        if (!favouritesEntryExist) {
            val newFavouritesEntry =
                Favourites(animalId = animal.value!!.id, userUid = currentUser.uid)

            database.insert(newFavouritesEntry)
            val currentFavouritesEntry = database.getLastFavouritesEntry()
            firebaseDatabase.favouritesFirebase.sendFavouritesEntry(currentFavouritesEntry!!)

            favouritesEntryExist = true
            _addToFavouritesSuccess.value = true
        }

        else {
            val currentFavouritesEntry = database.getFavouritesEntry(currentUser.uid, animal.value!!.id)
            database.delete(currentFavouritesEntry!!)
            firebaseDatabase.favouritesFirebase.deleteFavouritesEntry(currentFavouritesEntry)
            favouritesEntryExist = false
            _deleteFromFavouritesSuccess.value = true
        }
    }

    fun onApplyForAdoption() {
        if (!checkIfApplicationExist()) {
            val newAdoptionApplication = AdoptionApplication(name = currentUser.name!!, phoneNumber = currentUser.phoneNumber!!,
            date = System.currentTimeMillis(), animalId = animal.value!!.id, userUid = currentUser.uid)

            database.insert(newAdoptionApplication)
            val application = database.getLastAdoptionApplication()
            firebaseDatabase.adoptionApplicationFirebase.sendAdoptionApplication(application!!)

            _applicationCreateSuccess.value = true
        }
        else _applicationAlreadyExistError.value = true
    }

    private fun checkIfApplicationExist(): Boolean {
        val application = database.getApplicationByUserAnimal(currentUser.uid, animal.value!!.id)
        return application != null
    }
}