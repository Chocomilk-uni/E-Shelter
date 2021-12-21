package com.example.e_shelter.database.entities

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_table", foreignKeys = [ForeignKey(
        entity = Shelter::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("shelter_id"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
data class User(
    @PrimaryKey
    val uid: String = "",

    @ColumnInfo(name = "password")
    val password: String = "",

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = null,

    @ColumnInfo(name = "email")
    var email: String = "",

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "shelter_id")
    @Nullable
    var shelterId: Long? = null
)