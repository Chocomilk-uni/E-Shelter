package com.example.e_shelter.screens.user.signup

import androidx.lifecycle.*
import com.example.e_shelter.App
import com.example.e_shelter.database.entities.User
import kotlinx.coroutines.*

class SignUpUserViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao

    private val _navigateToSignIn = MutableLiveData<Boolean?>()

    val navigateToSignIn: LiveData<Boolean?>
        get() = _navigateToSignIn

    fun doneNavigating() {
        _navigateToSignIn.value = null
    }

    private val _emailExistsError = MutableLiveData<Boolean?>()
    val emailExistsError: LiveData<Boolean?>
        get() = _emailExistsError

    private val _phoneExistsError = MutableLiveData<Boolean?>()
    val phoneExistsError: LiveData<Boolean?>
        get() = _phoneExistsError

    private val _signUpSuccess = MutableLiveData<Boolean?>()
    val signUpSuccess: LiveData<Boolean?>
        get() = _signUpSuccess

    fun doneShowingSnackBar() {
        _emailExistsError.value = null
        _phoneExistsError.value = null
        _signUpSuccess.value = null
    }

    fun onSignUp(name: String, phoneNumber: String, email: String, password: String) {
        if (!checkIfPhoneExists(phoneNumber)) {
            if (!checkIfEmailExists(email)) {
                val newUser =
                    User(password = password, phoneNumber = phoneNumber, email = email, name = name)

                viewModelScope.launch {
                    insert(newUser)
                    _signUpSuccess.value = true
                    _navigateToSignIn.value = true
                }
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

    private suspend fun insert(user: User) {
        database.insert(user)
    }
}