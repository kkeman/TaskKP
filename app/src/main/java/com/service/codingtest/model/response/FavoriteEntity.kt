package com.service.codingtest.model.response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class FavoriteEntity(

        @PrimaryKey(autoGenerate = true)
        val id: Int,

        val isbn: String,

        val title: String,

        val thumbnail: String,

        val searchWord: String,
)