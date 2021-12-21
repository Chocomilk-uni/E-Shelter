package com.example.e_shelter.firebase

import com.example.e_shelter.firebase.entities.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FirebaseDatabaseRepo {
    var firebaseDatabase = FirebaseDatabase.getInstance()
    private var databaseReference: DatabaseReference = firebaseDatabase.reference
    lateinit var animalFirebase: AnimalFirebase
    lateinit var adoptionApplicationFirebase: AdoptionApplicationFirebase
    lateinit var favouritesFirebase: FavouritesFirebase
    var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    var shelterFirebase: ShelterFirebase = ShelterFirebase(databaseReference)
    var userFirebase: UserFirebase = UserFirebase(databaseReference)
    var shelterSignUpApplicationFirebase: ShelterSignUpApplicationFirebase = ShelterSignUpApplicationFirebase(databaseReference)

    init {
        user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            animalFirebase = AnimalFirebase(databaseReference, user)
            adoptionApplicationFirebase = AdoptionApplicationFirebase(databaseReference, user)
            favouritesFirebase = FavouritesFirebase(databaseReference, user)
        }
    }

    fun initRepo() {
        user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            animalFirebase = AnimalFirebase(databaseReference, user)
            adoptionApplicationFirebase = AdoptionApplicationFirebase(databaseReference, user)
            favouritesFirebase = FavouritesFirebase(databaseReference, user)
        }
    }
}