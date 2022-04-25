package com.service.codingtest.model.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class ImageRemoteKeyEntity(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val searchWord: String,
    val pageCount: Int?
)