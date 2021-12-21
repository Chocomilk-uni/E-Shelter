package com.example.e_shelter.database.entities

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "animal_table", foreignKeys = [ForeignKey(
        entity = Shelter::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("shelter_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)

data class Animal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String ="",

    @ColumnInfo(name = "age")
    var age: Int = 0,

    @ColumnInfo(name = "breed")
    var breed: String = "",

    @ColumnInfo(name = "is_sterilised")
    var isSterilised: Boolean = false,

    @ColumnInfo(name = "is_vaccinated")
    var isVaccinated: Boolean = false,

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "admission_date")
    var admissionDate: Long = 0L,

    @ColumnInfo(name = "found_home_date")
    var foundHomeDate: Long? = null,

    @ColumnInfo(name = "profile_pic_path", defaultValue = "NULL")
    @Nullable
    var profilePicPath: String? = null,

    @ColumnInfo(name = "species")
    var species: String = "",

    @ColumnInfo(name = "gender")
    var gender: String = "",

    @ColumnInfo(name = "animal_status")
    var status: String = "",

    @ColumnInfo(name = "shelter_id")
    var shelterId: Long = 0L
)