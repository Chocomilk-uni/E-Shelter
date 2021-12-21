package com.example.e_shelter.firebase.entities

import com.example.e_shelter.App
import com.example.e_shelter.Constants
import com.example.e_shelter.database.entities.ShelterSignUpApplication
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class ShelterSignUpApplicationFirebase(private val reference: DatabaseReference) {
    val database = App.database.eShelterDatabaseDao

    init {
        subscribeOnDataChanges()
    }

    fun sendSignUpApplication(application: ShelterSignUpApplication) {
        reference.child(Constants.SIGN_UP_APPLICATIONS_CHILD).child(application.id.toString()).setValue(application)
    }

    private fun subscribeOnDataChanges() {
        reference.child(Constants.SIGN_UP_APPLICATIONS_CHILD).addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.getValue(ShelterSignUpApplication::class.java) != null) {
                    if (database.getSignUpApplication(snapshot.getValue(ShelterSignUpApplication::class.java)!!.id) == null) {
                        database.insert(snapshot.getValue(ShelterSignUpApplication::class.java)!!)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(ShelterSignUpApplication::class.java)?.let { database.update(it) }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                database.deleteSignUpApplicationById(snapshot.getValue(ShelterSignUpApplication::class.java)!!.id)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}