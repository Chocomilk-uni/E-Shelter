package com.example.e_shelter.firebase.entities

import com.example.e_shelter.App
import com.example.e_shelter.Constants
import com.example.e_shelter.database.entities.Animal
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

data class AnimalFirebase(val reference: DatabaseReference, val user: FirebaseUser?) {
    val database = App.database.eShelterDatabaseDao

    init {
        subscribeOnDataChanges()
    }

    fun sendAnimal(animal: Animal) {
        if (user != null) {
            reference.child(Constants.ADMINS_CHILD).child(user.uid).child(Constants.ANIMALS_CHILD).child(animal.id.toString()).setValue(animal)
        }
    }

    fun deleteAnimal(animal: Animal) {
        deleteConnectedEntries(animal)

        reference.child(Constants.ADMINS_CHILD).child(user!!.uid).child(Constants.ANIMALS_CHILD).
        child(animal.id.toString()).removeValue()
    }

    private fun deleteConnectedEntries(animal: Animal) {
        val uniqueUids = database.getUniqueUids()

        val connectedApplications = database.getApplicationsByAnimal(animal.id)
        for (uid in uniqueUids) {
            for (application in connectedApplications) {
                reference.child(Constants.USERS_CHILD).child(uid)
                    .child(Constants.ADOPTION_APPLICATIONS_CHILD).child(application.id.toString())
                    .removeValue()
            }
        }

        val connectedFavourites = database.getFavouritesByAnimal(animal.id)
        for (uid in uniqueUids) {
            for (favouritesEntry in connectedFavourites) {
                reference.child(Constants.USERS_CHILD).child(uid)
                    .child(Constants.FAVOURITES_CHILD).child(favouritesEntry.id.toString())
                    .removeValue()
            }
        }
    }

    private fun subscribeOnDataChanges() {
        reference.child(Constants.ADMINS_CHILD).child(user!!.uid).child(Constants.ANIMALS_CHILD).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.getValue(Animal::class.java) != null) {
                    if (database.getAnimal(snapshot.getValue(Animal::class.java)!!.id) == null) {
                        database.insert(snapshot.getValue(Animal::class.java)!!)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(Animal::class.java)?.let { database.update(it) }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                deleteConnectedEntries(snapshot.getValue(Animal::class.java)!!)
                database.deleteAnimalById(snapshot.getValue(Animal::class.java)!!.id)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}