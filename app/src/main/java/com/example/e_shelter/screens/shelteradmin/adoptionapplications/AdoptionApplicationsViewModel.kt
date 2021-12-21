package com.example.e_shelter.screens.shelteradmin.adoptionapplications

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.e_shelter.App
import com.example.e_shelter.database.entities.AdoptionApplication

class AdoptionApplicationsViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao
    private val firebaseAuth = App.firebaseAuth

    lateinit var applications: LiveData<List<AdoptionApplication>>

    init {
        initializeUserAndShelter()
    }

    private fun initializeUserAndShelter() {
        val currentUser =  database.getUser(firebaseAuth.user!!.uid)
        applications = database.getApplications(currentUser!!.shelterId!!)
    }
}