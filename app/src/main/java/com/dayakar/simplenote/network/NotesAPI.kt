package com.dayakar.simplenote.network

import com.dayakar.simplenote.model.NoteSyncModel
import com.dayakar.simplenote.model.User
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    @FormUrlEncoded
    @POST("v1/users/login")
    suspend fun loginUser(@Field("email") email:String, @Field("password") password:String): Response<User>

    @FormUrlEncoded
    @POST("v1/users/create")
    suspend fun createUser(@Field("displayName") displayName:String, @Field("email") email:String, @Field("password") password:String): Response<User>

    @GET("v1/notes")
    suspend fun getmyNotes(): Response<ArrayList<NoteNetworkEntity>>

    @POST("v1/notes/addNote")
    suspend fun addNote(@Body noteSyncModel: NoteSyncModel): Response<NoteNetworkEntity>

    @PATCH("v1/notes")
    suspend fun updateNote(@Body noteSyncModel: NoteSyncModel): Response<NoteNetworkEntity>

    @DELETE("v1/notes/{id}")
    suspend fun deleteNote(@Path("id") id:Int): Response<Unit>

    @POST("v1/notes")
    suspend fun syncNotes(@Body notes:List<NoteSyncModel>): Response<ArrayList<NoteNetworkEntity>>


}