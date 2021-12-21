package com.example.e_shelter.screens.shelteradmin.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_shelter.App
import com.example.e_shelter.database.entities.ShelterSignUpApplication

class SignUpShelterAdminViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao
    private val firebaseDatabase = App.firebaseDatabase

    private val _navigateToSignIn = MutableLiveData<Boolean?>()

    private lateinit var newApplication: ShelterSignUpApplication

    val navigateToSignIn: LiveData<Boolean?>
        get() = _navigateToSignIn

    fun doneNavigating() {
        _navigateToSignIn.value = null
    }

    private val _applySuccess = MutableLiveData<Boolean?>()
    val applySuccess: LiveData<Boolean?>
        get() = _applySuccess

    fun doneShowingSnackBar() {
        _applySuccess.value = null
    }

    fun onSendApplication(city: String, address: String, name: String, socialLinks: String, email: String) {
        newApplication = if (socialLinks.isEmpty()) {
            ShelterSignUpApplication(city = city, address = address, name = name, email = email)
        } else {
            ShelterSignUpApplication(city = city, address = address, name = name, email = email, socialLinks = socialLinks)
        }

            database.insert(newApplication)
            val application = database.getLastSignUpApplication()
            firebaseDatabase.shelterSignUpApplicationFirebase.sendSignUpApplication(application!!)

            _applySuccess.value = true
            _navigateToSignIn.value = true
    }
}