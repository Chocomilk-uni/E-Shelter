package com.example.e_shelter.screens.user.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_shelter.App

class SignUpUserViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao
    val firebaseAuth = App.firebaseAuth

    private val _emailExistsError = MutableLiveData<Boolean?>()
    val emailExistsError: LiveData<Boolean?>
        get() = _emailExistsError

    private val _phoneExistsError = MutableLiveData<Boolean?>()
    val phoneExistsError: LiveData<Boolean?>
        get() = _phoneExistsError


    fun doneShowingSnackBar() {
        _emailExistsError.value = null
        _phoneExistsError.value = null
    }

    fun onSignUp(name: String, phoneNumber: String, email: String, password: String) {

        if (!checkIfPhoneExists(phoneNumber)) {
            if (!checkIfEmailExists(email)) {
                firebaseAuth.signUp(name, phoneNumber, email, password)
            } else _emailExistsError.value = true
        } else _phoneExistsError.value = true
    }


    private fun checkIfEmailExists(email: String): Boolean {
        val existingUserByEmail = database.getUserByEmail(email)

        return existingUserByEmail != null
    }

    private fun checkIfPhoneExists(phoneNumber: String): Boolean {
        val existingUserByPhone = database.getUserByPhone(phoneNumber)

        return existingUserByPhone != null
    }
}