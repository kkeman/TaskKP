package com.service.codingtest.model.response

import com.google.gson.annotations.SerializedName

data class Meta(
        @SerializedName("is_end")
        val is_end: Boolean
)