package com.service.codingtest.model.response

import com.google.gson.annotations.SerializedName

data class JsonData(
        @SerializedName("documents")
        val items: List<ItemsEntity>
)