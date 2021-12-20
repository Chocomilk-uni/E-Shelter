package com.example.e_shelter.screens.user.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_shelter.App

class SearchViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao
    var cities = mutableListOf<String>()
    var breeds = mutableListOf<String>()
    var genders = mutableListOf("Любой", "Мальчик", "Девочка")
    lateinit var searchModel: SearchModel

    private val _navigateToSearchResult = MutableLiveData<Boolean?>()
    val navigateToSearchResult: LiveData<Boolean?>
        get() = _navigateToSearchResult

    init {
        val uniqueCities = database.getUniqueCities()
        for (city in uniqueCities) {
            cities.add(city)
        }

        val uniqueBreeds = database.getUniqueCities()
        breeds.add("Любая")
        for (breed in uniqueBreeds) {
            breeds.add(breed)
        }
    }

    fun doneNavigating() {
        _navigateToSearchResult.value = null
    }

    fun onSearch(
        city: String,
        species: String,
        breed: String,
        ageFrom: String?,
        ageTo: String?,
        gender: String,
        isSterilised: Boolean,
        isVaccinated: Boolean
    ) {
        searchModel = SearchModel(city = city, breed = breed, species = species, ageFrom = ageFrom?.toIntOrNull(),
        ageTo = ageTo?.toIntOrNull(), gender = gender, isSterilised = isSterilised, isVaccinated = isVaccinated)

        _navigateToSearchResult.value = true
    }
}