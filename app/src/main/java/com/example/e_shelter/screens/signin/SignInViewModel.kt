package com.example.e_shelter.screens.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_shelter.App

class SignInViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao
    val firebaseAuth = App.firebaseAuth
    val firebaseDatabase = App.firebaseDatabase

    private val _navigateToHomeUser = MutableLiveData<Boolean?>()

    val navigateToHomeUser: LiveData<Boolean?>
        get() = _navigateToHomeUser

    private val _navigateToHomeShelter = MutableLiveData<Boolean?>()

    val navigateToHomeShelter: LiveData<Boolean?>
        get() = _navigateToHomeShelter

    fun doneNavigating() {
        _navigateToHomeUser.value = null
        _navigateToHomeShelter.value = null
    }

    private val _loginNotExist = MutableLiveData<Boolean?>()
    val loginNotExist: LiveData<Boolean?>
        get() = _loginNotExist


    fun doneShowingSnackBar() {
        _loginNotExist.value = null
    }

    fun onSignIn(login: String, password: String) {
        if (!checkIfEmailExists(login)) {
            _loginNotExist.value = true
        } else {
            firebaseAuth.signIn(login, password)
        }
    }

    private fun checkIfEmailExists(login: String): Boolean {
        val existingUserByEmail = database.getUserByEmail(login)

        return existingUserByEmail != null
    }
}