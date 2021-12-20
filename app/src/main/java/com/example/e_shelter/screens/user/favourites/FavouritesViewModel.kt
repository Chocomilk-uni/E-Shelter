package com.example.e_shelter.screens.user.favourites

import androidx.lifecycle.ViewModel
import com.example.e_shelter.App

class FavouritesViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao

    var animals = database.getFavourites(App.userId)


}