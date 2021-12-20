package com.example.e_shelter.screens.shelteradmin.animaladdedit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.example.e_shelter.App
import com.example.e_shelter.convertLongToDateString
import com.example.e_shelter.database.entities.Animal
import com.example.e_shelter.getAnimalPicFileName
import com.example.e_shelter.saveToInternalStorage
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class AddEditAnimalViewModel(private val animalId: Long = 0L) : ViewModel() {
    private val database = App.database.eShelterDatabaseDao

    var animal = MutableLiveData<Animal?>()
    var profilePic: Bitmap? = null
    lateinit var species: String
    lateinit var gender: String
    lateinit var status: String
    var admissionDate: Long? = null

    private var path: String? = null

    private val _saveSuccess = MutableLiveData<Boolean?>()
    val saveSuccess: LiveData<Boolean?>
        get() = _saveSuccess

    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome

    private val _updateSpinners = MutableLiveData<Boolean?>()
    val updateSpinners: LiveData<Boolean?>
        get() = _updateSpinners

    fun doneShowingSnackBar() {
        _saveSuccess.value = null
    }

    fun doneNavigating() {
        _navigateToHome.value = null
    }

    fun doneUpdating() {
        _updateSpinners.value = null
    }

    init {
        initializeAnimal()
    }

    val dateString = Transformations.map(animal) { animal ->
        formatDateString(animal)
    }

    val ageString = Transformations.map(animal) { animal ->
        formatAgeString(animal)
    }

    private fun initializeAnimal() {
        animal.value = database.getAnimal(animalId)

        if (animal.value != null) {
            val profilePicPath = animal.value!!.profilePicPath
            if (profilePicPath != null) {
                path = profilePicPath
                profilePic = loadImageFromStorage(path!!)
            }
            species = animal.value!!.species
            gender = animal.value!!.gender
            status = animal.value!!.status
            admissionDate = animal.value!!.admissionDate

            _updateSpinners.value = true
        }
    }

    fun onSave(
        name: String, age: Int, breed: String, isVaccinated: Boolean,
        isSterilised: Boolean, description: String, admissionDate: Long,
        species: String, animalStatus: String, gender: String
    ) {
        val foundHome: Long? = if (animalStatus == "Пристроен(-а) в семью") {
            System.currentTimeMillis()
        } else null

        if (animal.value == null) {
            val newAnimal = Animal(
                name = name,
                age = age,
                breed = breed,
                isSterilised = isSterilised,
                isVaccinated = isVaccinated,
                description = description,
                admissionDate = admissionDate,
                species = species,
                status = animalStatus,
                profilePicPath = path,
                shelterId = App.userId,
                gender = gender,
                foundHomeDate = foundHome
            )

            viewModelScope.launch {
                insert(newAnimal)
                _saveSuccess.value = true
                _navigateToHome.value = true
            }
        }

        else {
            animal.value!!.name = name
            animal.value!!.age = age
            animal.value!!.breed = breed
            animal.value!!.isSterilised = isSterilised
            animal.value!!.isVaccinated = isVaccinated
            animal.value!!.description = description
            animal.value!!.admissionDate = admissionDate
            animal.value!!.species = species
            animal.value!!.status = animalStatus
            animal.value!!.profilePicPath = path
            animal.value!!.shelterId = App.userId
            animal.value!!.foundHomeDate = foundHome
            animal.value!!.gender = gender

            viewModelScope.launch {
                update(animal.value!!)
                initializeAnimal()
                _saveSuccess.value = true
                _navigateToHome.value = true
            }
        }
    }

    private suspend fun insert(animal: Animal) {
        database.insert(animal)
    }

    private suspend fun update(animal: Animal) {
        database.update(animal)
    }

    private fun getCurrentId(): Long {
        val lastAnimal = database.getLastAnimal()
        return if (lastAnimal == null) {
            0L + 1
        } else lastAnimal.id + 1
    }

    private fun loadImageFromStorage(path: String): Bitmap? {
        try {
            val file = File(path)
            return BitmapFactory.decodeStream(FileInputStream(file))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun onAttachPic(bitmap: Bitmap) {
        profilePic = bitmap
        path = saveToInternalStorage(bitmap, getAnimalPicFileName(getCurrentId()))
    }

    private fun formatDateString(animal: Animal?): String {
        return if (animal == null) {
            "Дата поступления в приют"
        } else convertLongToDateString(animal.admissionDate)
    }

    private fun formatAgeString(animal: Animal?): String? {
        return animal?.age?.toString()
    }
}