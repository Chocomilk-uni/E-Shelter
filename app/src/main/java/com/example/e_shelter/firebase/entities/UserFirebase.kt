package com.example.e_shelter.firebase.entities

import com.example.e_shelter.App
import com.example.e_shelter.Constants
import com.example.e_shelter.database.entities.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class UserFirebase(private val reference: DatabaseReference) {
    val database = App.database.eShelterDatabaseDao
    var mainChild: String = ""

    init {
        subscribeOnDataChanges()
    }

    fun sendUser(user: User) {
        mainChild = if (user.shelterId != null) Constants.ADMINS_CHILD
        else Constants.USERS_CHILD
        reference.child(mainChild).child(user.uid).setValue(user)
    }

    private fun subscribeOnDataChanges() {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot.getValue(User::class.java) != null) {
                    if (database.getUser(snapshot.getValue(User::class.java)!!.uid) == null) {
                        database.insert(snapshot.getValue(User::class.java)!!)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(User::class.java)?.let { database.update(it) }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                database.deleteUserById(snapshot.getValue(User::class.java)!!.uid)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }

        reference.child(Constants.ADMINS_CHILD).addChildEventListener(childEventListener)
        reference.child(Constants.USERS_CHILD).addChildEventListener(childEventListener)
    }
}
