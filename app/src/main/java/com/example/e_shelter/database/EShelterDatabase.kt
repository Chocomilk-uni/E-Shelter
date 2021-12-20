package com.example.e_shelter.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.e_shelter.database.entities.*

@Database(
    entities = [AdoptionApplication::class, Animal::class, Favourites::class, User::class,
        Shelter::class, ShelterSignUpApplication::class],
    version = 19,
    exportSchema = false
)

abstract class EShelterDatabase : RoomDatabase() {

    abstract val eShelterDatabaseDao: EShelterDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: EShelterDatabase? = null

        fun getInstance(app: Application): EShelterDatabase {

            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        app,
                        EShelterDatabase::class.java,
                        "eshelter_database"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}