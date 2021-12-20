package com.example.e_shelter.screens.user.searchresult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_shelter.screens.user.search.SearchModel

class SearchResultViewModelFactory(private val searchModel: SearchModel) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
            return SearchResultViewModel(searchModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}