package com.example.e_shelter.screens.shelteradmin.adoptionapplications

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.e_shelter.App
import com.example.e_shelter.database.entities.AdoptionApplication

class AdoptionApplicationsViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao

    lateinit var applications: LiveData<List<AdoptionApplication>>

    init {
        initializeUserAndShelter()
    }

    private fun initializeUserAndShelter() {
        val currentUser = database.getUser(App.userId)
        applications = database.getApplications(currentUser.shelterId!!)
    }
}