package com.dayakar.simplenote.network

import com.google.gson.annotations.SerializedName

data class NoteNetworkEntity (
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("note")
    val note: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatetime")
    val updatetime: String,
    @SerializedName("synced")
    val isSynced: Boolean


)