package com.example.e_shelter.screens.user.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchModel (
    var city: String,
    var species: String,
    var breed: String,
    var ageFrom: Int?,
    var ageTo: Int?,
    var gender: String,
    var isSterilised: Boolean,
    var isVaccinated: Boolean,
    var status: String = "Ждёт семью"
) : Parcelable