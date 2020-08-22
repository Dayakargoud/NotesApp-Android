package com.dayakar.simplenote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NoteSyncModel(
    @SerializedName("id")
    @Expose
    val id: Int,
    @SerializedName("userId")
    @Expose
    val userId: Int,
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("note")
    @Expose
    val note: String,
    @SerializedName("updatetime")
    @Expose
    val updatetime: String
)