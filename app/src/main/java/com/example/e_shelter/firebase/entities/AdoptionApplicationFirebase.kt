package com.example.e_shelter.firebase.entities

import com.example.e_shelter.App
import com.example.e_shelter.Constants
import com.example.e_shelter.database.entities.AdoptionApplication
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class AdoptionApplicationFirebase(private val reference: DatabaseReference, val user: FirebaseUser?) {
    val database = App.database.eShelterDatabaseDao

    init {
        subscribeOnDataChanges()
    }

    fun sendAdoptionApplication(application: AdoptionApplication) {
        if (user != null) {
            reference.child(Constants.USERS_CHILD).child(user.uid).
            child(Constants.ADOPTION_APPLICATIONS_CHILD).child(application.id.toString()).setValue(application)
        }
    }

    private fun subscribeOnDataChanges() {
        if (user != null) {
            reference.child(Constants.USERS_CHILD).child(user.uid).
            child(Constants.ADOPTION_APPLICATIONS_CHILD).addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.getValue(AdoptionApplication::class.java) != null) {
                        if (database.getAdoptionApplication(snapshot.getValue(AdoptionApplication::class.java)!!.id) == null) {
                            database.insert(snapshot.getValue(AdoptionApplication::class.java)!!)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.getValue(AdoptionApplication::class.java)?.let { database.update(it) }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    database.deleteSignUpApplicationById(snapshot.getValue(AdoptionApplication::class.java)!!.id)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}