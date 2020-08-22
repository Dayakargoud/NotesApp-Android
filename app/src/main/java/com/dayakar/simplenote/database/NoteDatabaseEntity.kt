package com.dayakar.simplenote.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NoteDatabaseEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int,
    @ColumnInfo(name = "userId")
    val userId: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "note")
    val note: String,
    @ColumnInfo(name = "updateTime")
    val updatetime: String,
    @ColumnInfo(name = "synced")
    val isSynced:Boolean=true)
