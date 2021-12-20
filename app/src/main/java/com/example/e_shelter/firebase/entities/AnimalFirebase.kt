package com.example.e_shelter.firebase.entities

import com.example.e_shelter.App
import com.example.e_shelter.database.entities.Animal
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

data class AnimalFirebase(val reference: DatabaseReference, val user: FirebaseUser) {
    val database = App.database.eShelterDatabaseDao

    init {
        observe()
    }

    fun sendAnimal(animal: Animal) {

    }

    private fun observe() {
        TODO("Not yet implemented")
    }
}