package com.example.e_shelter.screens.animalprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AnimalProfileViewModelFactory(private val animalId: Long) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AnimalProfileViewModel::class.java)) {
            return AnimalProfileViewModel(animalId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}