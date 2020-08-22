package com.dayakar.simplenote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Note(
    val id: Int,
    val userId: Int,
    val title: String,
    val note: String,
    val updatetime: String,
    val isSynced:Boolean
) : Parcelable {

    fun formatDate(time:String):String{
        return try{
            val sdf = SimpleDateFormat("hh:mm a MMM dd,yyyy")
            val resultdate = Date(time.toLong())
            sdf.format(resultdate)
        }catch (e:Exception){
            time
        }

    }
}