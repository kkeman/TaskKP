package com.service.codingtest.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Items")
data class ItemsEntity(

        @PrimaryKey
        @SerializedName("isbn")
        val isbn: String,

        @SerializedName("title")
        val title: String,

        @SerializedName("thumbnail")
        val thumbnail: String,

        @SerializedName("contents")
        val contents: String,


        @SerializedName("sale_price")
        val sale_price: Int,


        @SerializedName("datetime")
        val datetime: String,


        @SerializedName("url")
        val url: String,


        @SerializedName("publisher")
        val publisher: String,

        var searchWord: String,

        var isFavorite: Boolean
): Parcelable