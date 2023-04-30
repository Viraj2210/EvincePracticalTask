package com.evince.evincepracticaltask.activity.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.d2k.shg.networking.ApiClient
import com.evince.evincepracticaltask.activity.model.UserModel
import com.evince.evincepracticaltask.network.IApiService
import com.evince.evincepracticaltask.utils.BaseDataSource
import com.evince.evincepracticaltask.utils.CommanModel
import com.evince.evincepracticaltask.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class UserRepo(var iApiService: IApiService) : BaseDataSource() {

    /*var liveData : MutableLiveData<UserModel> = MutableLiveData()
   *//* fun getUserList(id: Int) : Flow<Resources<CommanModel<UserModel>>>{
       *//**//* return flow {
            val result = safeApiCall { iApiService.getUserList(id) }
            emit(result)
        }.flowOn(Dispatchers.IO)*//**//*
    }*//*


    suspend fun getUserList(id : Int) : Flow<CommanModel<UserModel>>{
       val job = viewModelScop
    }

    fun userListRes() : LiveData<UserModel>{
        return liveData
    }*/
}