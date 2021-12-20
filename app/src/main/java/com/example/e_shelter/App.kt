package com.example.e_shelter

import android.app.Application
import com.example.e_shelter.database.EShelterDatabase
import com.example.e_shelter.firebase.FirebaseAuth
import com.example.e_shelter.firebase.FirebaseRepo

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        lateinit var database: EShelterDatabase
            private set

        // TODO: remove and use firebase
        var userId: Long = 0L

        lateinit var firebaseAuth: FirebaseAuth
            private set
        lateinit var firebaseDatabase: FirebaseRepo
            private  set
    }


    override fun onCreate() {
        super.onCreate()

        instance = this
        database = EShelterDatabase.getInstance(instance)

//        firebaseDatabase = FirebaseRepo()
//        firebaseAuth = FirebaseAuth()
    }
}