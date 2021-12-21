package com.example.e_shelter.screens.user.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.e_shelter.App
import com.example.e_shelter.database.entities.Animal

class FavouritesViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao
    private val firebaseAuth = App.firebaseAuth
    var animals: LiveData<List<Animal>> = database.getFavourites(firebaseAuth.user!!.uid)

    var animalsList = database.getFavouritesList(firebaseAuth.user!!.uid)
}