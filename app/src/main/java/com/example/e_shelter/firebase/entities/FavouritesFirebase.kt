package com.example.e_shelter.firebase.entities

import com.example.e_shelter.App
import com.example.e_shelter.Constants
import com.example.e_shelter.database.entities.Favourites
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class FavouritesFirebase(private val reference: DatabaseReference, val user: FirebaseUser?) {
    val database = App.database.eShelterDatabaseDao

    init {
        subscribeOnDataChanges()
    }

    fun sendFavouritesEntry(favourites: Favourites) {
        if (user != null) {
            reference.child(Constants.USERS_CHILD).child(user.uid).child(Constants.FAVOURITES_CHILD)
                .child(favourites.id.toString()).setValue(favourites)
        }
    }

    fun deleteFavouritesEntry(favourites: Favourites) {
        if (user != null) {
            reference.child(Constants.USERS_CHILD).child(user.uid).child(Constants.FAVOURITES_CHILD)
                .child(favourites.id.toString()).removeValue()
        }
    }

    private fun subscribeOnDataChanges() {
        if (user != null) {
            reference.child(Constants.USERS_CHILD).child(user.uid).child(Constants.FAVOURITES_CHILD).addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.getValue(Favourites::class.java) != null) {
                        if (database.getFavouriteEntry(snapshot.getValue(Favourites::class.java)!!.id) == null) {
                            database.insert(snapshot.getValue(Favourites::class.java)!!)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot.getValue(Favourites::class.java)?.let { database.update(it) }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    database.deleteFavouritesById(snapshot.getValue(Favourites::class.java)!!.id)
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}