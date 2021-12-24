package com.example.e_shelter.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.e_shelter.database.entities.*

@Dao
interface EShelterDatabaseDao {

    // Insert methods
    @Insert
    fun insert(user: User)

    @Insert
    fun insert(animal: Animal)

    @Insert
    fun insert(adoptionApplication: AdoptionApplication)

    @Insert
    fun insert(shelterSignUpApplication: ShelterSignUpApplication)

    @Insert
    fun insert(favourite: Favourites)

    @Insert
    fun insert(shelter: Shelter)

    // Update methods
    @Update
    fun update(animal: Animal)

    @Update
    fun update(adoptionApplication: AdoptionApplication)

    @Update
    fun update(signUpApplication: ShelterSignUpApplication)

    @Update
    fun update(shelter: Shelter)

    @Update
    fun update(user: User)

    @Update
    fun update(favourite: Favourites)

    // Delete methods
    @Delete
    fun delete(animal: Animal)

    @Delete
    fun delete(favourite: Favourites)

    // Query methods
    @Query("SELECT * from animal_table WHERE id = :id LIMIT 1")
    fun getAnimal(id: Long): Animal?

    @Query("SELECT * from adoption_application_table WHERE id = :id LIMIT 1")
    fun getAdoptionApplication(id: Long): AdoptionApplication?

    @Query("SELECT * from animal_table")
    fun getAllAnimals(): List<Animal>

    @Query("SELECT * from animal_table WHERE id = (SELECT MAX(id) FROM animal_table) LIMIT 1")
    fun getLastAnimal(): Animal?

    @Query("SELECT * from shelter_table WHERE id = (SELECT MAX(id) FROM shelter_table) LIMIT 1")
    fun getLastShelter(): Shelter?

    @Query("SELECT * from shelter_sign_up_application_table WHERE id = (SELECT MAX(id) FROM shelter_sign_up_application_table) LIMIT 1")
    fun getLastSignUpApplication(): ShelterSignUpApplication?

    @Query("SELECT * from adoption_application_table WHERE id = (SELECT MAX(id) FROM adoption_application_table) LIMIT 1")
    fun getLastAdoptionApplication(): AdoptionApplication?

    @Query("SELECT * from favourites_table WHERE id = (SELECT MAX(id) FROM favourites_table) LIMIT 1")
    fun getLastFavouritesEntry(): Favourites?

    @Query("SELECT * from shelter_table WHERE id = :id LIMIT 1")
    fun getShelter(id: Long): Shelter?

    @Query("SELECT * from shelter_sign_up_application_table WHERE id = :id LIMIT 1")
    fun getSignUpApplication(id: Long): ShelterSignUpApplication?

    @Query("SELECT DISTINCT city FROM shelter_table")
    fun getUniqueCities(): List<String>

    @Query("SELECT * from animal_table WHERE id IN (SELECT animal_id from favourites_table WHERE user_uid = :userUid)")
    fun getFavourites(userUid: String): LiveData<List<Animal>>

    @Query("SELECT * from animal_table WHERE id IN (SELECT animal_id from favourites_table WHERE user_uid = :userUid)")
    fun getFavouritesList(userUid: String): List<Animal>

    @Query("SELECT * from favourites_table WHERE id = :id")
    fun getFavouriteEntry(id: Long): Favourites?

    @Query("SELECT * from adoption_application_table WHERE id = :id LIMIT 1")
    fun getApplication(id: Long): AdoptionApplication?

    @Query("SELECT * from adoption_application_table WHERE animal_id IN (SELECT id from animal_table WHERE shelter_id = :shelterId)")
    fun getApplications(shelterId: Long): LiveData<List<AdoptionApplication>>

    @Query("SELECT * from user_table WHERE uid = :uid LIMIT 1")
    fun getUser(uid: String): User?

    @Query("SELECT * from user_table WHERE email = :email LIMIT 1")
    fun getUserByEmail(email: String): User?

    @Query("SELECT * from user_table WHERE phone_number = :phoneNumber LIMIT 1")
    fun getUserByPhone(phoneNumber: String): User?

    @Query("SELECT * from user_table WHERE email = :email AND password = :password LIMIT 1")
    fun getUserByEmailPassword(email: String, password: String): User?

    // Methods for filtering animals
    @Query("SELECT * from animal_table WHERE shelter_id = :shelterId ORDER BY  animal_status, species")
    fun getAnimalsByShelter(shelterId: Long): LiveData<List<Animal>>

    @Query("SELECT * from animal_table WHERE species = :species AND shelter_id = :shelterId")
    fun getAllAnimalsBySpecies(species: String, shelterId: Long): LiveData<List<Animal>>

    @Query("SELECT * from animal_table WHERE species = :species AND shelter_id = :shelterId")
    fun getAllAnimalsBySpeciesList(species: String, shelterId: Long): List<Animal>

    @Query("SELECT * from animal_table WHERE species <> :excludeSpeciesFirst AND species <> :excludeSpeciesSecond AND shelter_id = :shelterId")
    fun getAllExoticAnimalsList(
        excludeSpeciesFirst: String,
        excludeSpeciesSecond: String,
        shelterId: Long
    ): List<Animal>

    @Query("SELECT * from animal_table WHERE species <> :excludeSpeciesFirst AND species <> :excludeSpeciesSecond AND shelter_id = :shelterId")
    fun getAllExoticAnimals(
        excludeSpeciesFirst: String,
        excludeSpeciesSecond: String,
        shelterId: Long
    ): LiveData<List<Animal>>

    @Query("SELECT * from animal_table WHERE found_home_date BETWEEN :dateFrom AND :dateTo AND shelter_id = :shelterId")
    fun getAnimalsFoundHomeInDatePeriod(dateFrom: Long, dateTo: Long, shelterId: Long): List<Animal>

    @Query("SELECT * from favourites_table WHERE animal_id = :animalId AND user_uid = :userUid")
    fun getFavouritesEntry(userUid: String, animalId: Long): Favourites?

    @Query("SELECT * from favourites_table WHERE animal_id = :animalId")
    fun getFavouritesByAnimal(animalId: Long): List<Favourites>

    @Query("SELECT * from adoption_application_table WHERE animal_id = :animalId AND user_uid = :userUid ORDER BY application_status, date")
    fun getApplicationByUserAnimal(userUid: String, animalId: Long): AdoptionApplication?

    @Query("SELECT * from adoption_application_table WHERE animal_id = :animalId ORDER BY application_status, date")
    fun getApplicationsByAnimal(animalId: Long): List<AdoptionApplication>

    @Query("SELECT DISTINCT breed FROM animal_table")
    fun getUniqueBreeds(): List<String>

    @Query("SELECT DISTINCT uid FROM user_table")
    fun getUniqueUids(): List<String>

    @Query("DELETE FROM user_table WHERE uid = :uid")
    fun deleteUserById(uid: String)

    @Query("DELETE FROM shelter_table WHERE id = :id")
    fun deleteShelterById(id: Long)

    @Query("DELETE FROM animal_table WHERE id = :id")
    fun deleteAnimalById(id: Long)

    @Query("DELETE FROM shelter_sign_up_application_table WHERE id = :id")
    fun deleteSignUpApplicationById(id: Long)

    @Query("DELETE FROM favourites_table WHERE id = :id")
    fun deleteFavouritesById(id: Long)
}