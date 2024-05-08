package com.eagletech.happynote.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "datas")
data class Data(
    @PrimaryKey(autoGenerate = true)
    val idData: Int,
    val dataTitle: String,
    val dataContent: String
): Serializable
