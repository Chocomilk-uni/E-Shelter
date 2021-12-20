package com.example.e_shelter.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.e_shelter.App
import com.example.e_shelter.database.EShelterDatabaseDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuth {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var user: FirebaseUser? = firebaseAuth.currentUser
    private var database: EShelterDatabaseDao = App.database.eShelterDatabaseDao

    private val _signInSuccess = MutableLiveData<Boolean?>()
    val signInSuccess: LiveData<Boolean?>
        get() = _signInSuccess

    private val _signInError = MutableLiveData<Boolean?>()
    val signInError: LiveData<Boolean?>
        get() = _signInError

    private val _signUpSuccess = MutableLiveData<Boolean?>()
    val signUpSuccess: LiveData<Boolean?>
        get() = _signUpSuccess

    private val _logOut = MutableLiveData<Boolean?>()
    val logOut: LiveData<Boolean?>
        get() = _logOut

    init {
        if (user != null) _signInSuccess.value = true
    }

    fun doneShowingSnackBar() {
        _signInError.value = null
    }

    fun doneNavigating() {
        _logOut.value = null
        _signInSuccess.value = null
        _signUpSuccess.value = null
    }

//    fun signIn(email: String, password: String) {
//        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
//            if (it.isSuccessful) {
//                _signInSuccess.value = true
//                App.firebaseDatabase.initializeRepo()
//            }
//            else _signInError.value = true
//            user = firebaseAuth.currentUser
//        }
//    }
//
//    fun signUp(name: String, phoneNumber: String, email: String, password: String) {
//        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
//            if (it.isSuccessful) {
//                _signUpSuccess.value = true
//                val newUser = User(id = firebaseAuth.currentUser.uid,
//                    phoneNumber = phoneNumber,
//                    email = email,
//                    password = password,
//                    name = name
//                )
//            }
//        }
//    }

    fun signOut() {
        firebaseAuth.signOut()
        user = firebaseAuth.currentUser
        _logOut.value = true
    }
}