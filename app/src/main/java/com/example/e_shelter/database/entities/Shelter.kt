package com.example.e_shelter.database.entities

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shelter_table")
data class Shelter(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "city")
    var city: String = "",

    @ColumnInfo(name = "address")
    var address: String = "",

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String = "",

    @ColumnInfo(name = "logo_pic_path", defaultValue = "NULL")
    @Nullable
    var logoPic: String? = null
)