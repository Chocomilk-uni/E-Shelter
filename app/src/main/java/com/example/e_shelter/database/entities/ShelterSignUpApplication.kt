package com.example.e_shelter.database.entities

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shelter_sign_up_application_table")
data class ShelterSignUpApplication(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "city")
    val city: String = "",

    @ColumnInfo(name = "address")
    val address: String = "",

    @ColumnInfo(name = "social_links", defaultValue = "NULL")
    @Nullable
    val socialLinks: String? = null,

    @ColumnInfo(name = "email")
    val email: String = ""
)