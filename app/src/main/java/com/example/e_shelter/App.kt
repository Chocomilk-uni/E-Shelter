package com.example.e_shelter

import android.app.Application
import com.example.e_shelter.database.EShelterDatabase
import com.example.e_shelter.firebase.FirebaseAuthRepo
import com.example.e_shelter.firebase.FirebaseDatabaseRepo

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        lateinit var database: EShelterDatabase
            private set

        lateinit var firebaseAuth: FirebaseAuthRepo
            private set
        lateinit var firebaseDatabase: FirebaseDatabaseRepo
            private  set
    }


    override fun onCreate() {
        super.onCreate()

        instance = this
        database = EShelterDatabase.getInstance(instance)

        firebaseDatabase = FirebaseDatabaseRepo()
        firebaseAuth = FirebaseAuthRepo()
    }
}