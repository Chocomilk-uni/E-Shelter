package com.example.e_shelter.screens.shelteradmin.animaladdedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class AddEditAnimalViewModelFactory(private val animalId: Long) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditAnimalViewModel::class.java)) {
            return AddEditAnimalViewModel(animalId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}