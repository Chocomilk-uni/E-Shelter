package com.example.e_shelter.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "adoption_application_table", foreignKeys = [ForeignKey(
        entity = Animal::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("animal_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    ), ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("user_uid"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class AdoptionApplication(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String = "",

    @ColumnInfo(name = "date")
    val date: Long = 0L,

    @ColumnInfo(name = "animal_id")
    val animalId: Long = 0L,

    @ColumnInfo(name = "user_uid")
    val userUid: String = "",

    @ColumnInfo(name = "application_status")
    var applicationStatus: String = "Не обработана"
)