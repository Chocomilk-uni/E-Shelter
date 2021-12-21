package com.example.e_shelter.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.e_shelter.App
import com.example.e_shelter.database.EShelterDatabaseDao
import com.example.e_shelter.database.entities.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthRepo {
    private var firebaseAuth = FirebaseAuth.getInstance()
    var user: FirebaseUser? = firebaseAuth.currentUser
    private var database: EShelterDatabaseDao = App.database.eShelterDatabaseDao

    private val _signInError = MutableLiveData<Boolean?>()
    val signInError: LiveData<Boolean?>
        get() = _signInError

    private val _signUpSuccess = MutableLiveData<Boolean?>()
    val signUpSuccess: LiveData<Boolean?>
        get() = _signUpSuccess

    private val _navigateToSignIn = MutableLiveData<Boolean?>()
    val navigateToSignIn: LiveData<Boolean?>
        get() = _navigateToSignIn

    private val _signUpError = MutableLiveData<Boolean?>()
    val signUpError: LiveData<Boolean?>
        get() = _signUpError

    private val _navigateToHomeShelter = MutableLiveData<Boolean?>()
    val navigateToHomeShelter: LiveData<Boolean?>
        get() = _navigateToHomeShelter

    private val _navigateToHomeUser = MutableLiveData<Boolean?>()
    val navigateToHomeUser: LiveData<Boolean?>
        get() = _navigateToHomeUser

    fun doneShowingSnackBar() {
        _signUpSuccess.value = null
        _signInError.value = null
    }

    fun doneNavigating() {
        _navigateToSignIn.value = null
        _navigateToHomeUser.value = null
        _navigateToHomeShelter.value = null
    }

    fun signIn(email: String, password: String) {
        firebaseAuth.signOut()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                user = firebaseAuth.currentUser
                App.firebaseDatabase.initRepo()
                val currentUser = database.getUser(user!!.uid)
                if (currentUser != null) {
                    if (currentUser.shelterId != null) {
                        _navigateToHomeShelter.value = true
                    }
                    else _navigateToHomeUser.value = true
                }
            } else {
                Log.w("myLog", "signInWithEmail:failure", it.exception)
                _signInError.value = true
            }
        }
    }

    fun signUp(name: String, phoneNumber: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val newUser = User(
                    uid = firebaseAuth.currentUser!!.uid,
                    phoneNumber = phoneNumber,
                    email = email,
                    password = password,
                    name = name
                )
                database.insert(newUser)
                App.firebaseDatabase.userFirebase.sendUser(newUser)
                _signUpSuccess.value = true
                _navigateToSignIn.value = true
            } else _signUpError.value = true
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }
}