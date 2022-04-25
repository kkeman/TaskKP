package com.service.codingtest.model.response

import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Items")
data class ItemsEntity(

        @PrimaryKey
        @SerializedName("isbn")
        val isbn: String,

        @SerializedName("title")
        val title: String,

        @SerializedName("thumbnail")
        val thumbnail: String,

        var searchWord: String,

        var isFavorite: Boolean
)