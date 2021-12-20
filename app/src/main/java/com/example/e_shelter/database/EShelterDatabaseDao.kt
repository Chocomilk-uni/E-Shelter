package com.example.e_shelter.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.e_shelter.database.entities.*

@Dao
interface EShelterDatabaseDao {

    // Insert methods
    @Insert
    suspend fun insert(user: User)

    @Insert
    suspend fun insert(animal: Animal)

    @Insert
    suspend fun insert(adoptionApplication: AdoptionApplication)

    @Insert
    suspend fun insert(shelterSignUpApplication: ShelterSignUpApplication)

    @Insert
    fun insert(favourite: Favourites)

    @Insert
    fun insert(shelter: Shelter)

    // Update methods
    @Update
    suspend fun update(animal: Animal)

    @Update
    fun update(adoptionApplication: AdoptionApplication)

    @Update
    suspend fun update(shelter: Shelter)

    // Delete methods
    @Delete
    fun delete(animal: Animal)

    @Delete
    fun delete(favourite: Favourites)

    // Query methods
    @Query("SELECT * from animal_table WHERE id = :id LIMIT 1")
    fun getAnimal(id: Long): Animal?

    @Query("SELECT * from animal_table")
    fun getAllAnimals(): List<Animal>

    @Query("SELECT * from animal_table WHERE id = (SELECT MAX(id) FROM animal_table) LIMIT 1")
    fun getLastAnimal(): Animal?

    @Query("SELECT * from shelter_table WHERE id = :id LIMIT 1")
    fun getShelter(id: Long): Shelter

    @Query("SELECT DISTINCT city FROM shelter_table")
    fun getUniqueCities(): List<String>

    @Query("SELECT * from animal_table WHERE id IN (SELECT animal_id from favourites_table WHERE user_id = :userId)")
    fun getFavourites(userId: Long): LiveData<List<Animal>>

    @Query("SELECT * from adoption_application_table WHERE id = :id LIMIT 1")
    fun getApplication(id: Long): AdoptionApplication?

    @Query("SELECT * from adoption_application_table WHERE animal_id IN (SELECT id from animal_table WHERE shelter_id = :shelterId)")
    fun getApplications(shelterId: Long): LiveData<List<AdoptionApplication>>

    @Query("SELECT * from user_table WHERE id = :id LIMIT 1")
    fun getUser(id: Long): User

    @Query("SELECT * from user_table WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * from user_table WHERE phone_number = :phoneNumber LIMIT 1")
    fun getUserByPhone(phoneNumber: String): User?

    @Query("SELECT * from user_table WHERE email = :email AND password = :password LIMIT 1")
    fun getUserByEmailPassword(email: String, password: String): User?

    // Methods for filtering animals
    @Query("SELECT * from animal_table WHERE shelter_id = :shelterId ORDER BY species")
    fun getAnimalsByShelter(shelterId: Long): LiveData<List<Animal>>

    @Query("SELECT * from animal_table WHERE species = :species AND shelter_id = :shelterId")
    fun getAnimalsBySpecies(species: String, shelterId: Long): LiveData<List<Animal>>

    @Query("SELECT * from animal_table WHERE species <> :excludeSpeciesFirst AND species <> :excludeSpeciesSecond AND shelter_id = :shelterId")
    fun getExoticAnimals(excludeSpeciesFirst: String, excludeSpeciesSecond: String, shelterId: Long): LiveData<List<Animal>>

    @Query("SELECT * from favourites_table WHERE animal_id = :animalId AND user_id = :userId")
    fun getFavouritesEntry(userId: Long, animalId: Long): Favourites?

    @Query("SELECT * from adoption_application_table WHERE animal_id = :animalId AND user_id = :userId ORDER BY application_status, date")
    fun getApplicationByUserAnimal(userId: Long, animalId: Long): AdoptionApplication?

    @Query("SELECT DISTINCT breed FROM animal_table")
    fun getUniqueBreeds(): List<String>
}