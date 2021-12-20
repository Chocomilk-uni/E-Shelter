package com.example.e_shelter.firebase.entities

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference

class AdoptionApplicationFirebase(val reference: DatabaseReference, val user: FirebaseUser) {
}