package com.evince.evincepracticaltask.network

import com.evince.evincepracticaltask.activity.model.UserModel
import com.evince.evincepracticaltask.utils.CommanModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {

    /**
     * Authenticate call.
     *
     * @param loginRequest the login request Ex:- <String,String>
     * @return the call
     */
    /*IAuthenticate this API Call Interface Is use to Login*/


    @GET("users?page")
    suspend fun getUserList(
        @Query("pgNo") pgNo : Int) :
            Response<UserModel>

    @GET("users/{user_id}")
    suspend fun deleteUser(@Path("user_id") userId : Int)

}