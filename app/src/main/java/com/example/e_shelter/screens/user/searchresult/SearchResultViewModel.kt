package com.example.e_shelter.screens.user.searchresult

import androidx.lifecycle.ViewModel
import com.example.e_shelter.App
import com.example.e_shelter.database.entities.Animal
import com.example.e_shelter.screens.user.search.SearchModel

class SearchResultViewModel(searchModel: SearchModel) : ViewModel() {
    private val database = App.database.eShelterDatabaseDao

    private var allAnimals = database.getAllAnimals()
    var resultedAnimals = mutableListOf<Animal>()

    var zeroResults: Boolean = false

    init {
        for (animal in allAnimals) {
            val shelter = database.getShelter(animal.shelterId)
            if (shelter!!.city == searchModel.city && animal.status == "Ждёт семью") {
                if (animal.species == searchModel.species) {
                    if ((searchModel.gender != "Любой" && animal.gender == searchModel.gender) || searchModel.gender == "Любой") {
                        if ((searchModel.breed != "Любая" && animal.breed == searchModel.breed) || searchModel.breed == "Любая") {
                            if ((searchModel.ageFrom != null && animal.age >= searchModel.ageFrom!!) || searchModel.ageFrom == null) {
                                if ((searchModel.ageTo != null && animal.age <= searchModel.ageTo!!) || searchModel.ageTo == null) {
                                    if ((searchModel.isSterilised && animal.isSterilised) || !searchModel.isSterilised) {
                                        if ((searchModel.isVaccinated && animal.isVaccinated) || !searchModel.isVaccinated) {
                                            resultedAnimals.add(animal)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (resultedAnimals.size == 0) {
            zeroResults = true
        }
    }
}