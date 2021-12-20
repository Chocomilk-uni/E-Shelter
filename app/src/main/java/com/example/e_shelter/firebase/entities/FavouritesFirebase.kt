package com.example.e_shelter.firebase.entities

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class FavouritesFirebase(val reference: DatabaseReference, val user: FirebaseUser) {
}