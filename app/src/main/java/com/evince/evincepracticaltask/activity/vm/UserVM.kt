package com.evince.evincepracticaltask.activity.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d2k.shg.networking.ApiClient
import com.evince.evincepracticaltask.activity.model.UserModel
import com.evince.evincepracticaltask.activity.repo.UserRepo
import com.evince.evincepracticaltask.db.DatabaseInstance
import com.evince.evincepracticaltask.db.UserDao

import com.evince.evincepracticaltask.utils.CommanModel
import com.evince.evincepracticaltask.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.IDN

class UserVM(application: Application) : AndroidViewModel(application) {

    lateinit var userLiveData : MutableLiveData<UserModel>
    lateinit var userDao : UserDao
    init {
        userDao = DatabaseInstance.getDatabaseClient(application).userDao()
        userLiveData = MutableLiveData()
    }


     fun getUserList(idn: Int){
        viewModelScope.launch(Dispatchers.IO) {
            val res = ApiClient.aPIService.getUserList(idn)
            //userDao.insertIntoUser(res.body().data)
            userLiveData.postValue(res.body())
        }
    }

    fun insertIntoUser(userModel: List<UserModel.DataX>){
        userDao.insertIntoUser(userModel)
    }

    fun getUserListData() : LiveData<UserModel>{
        return userLiveData
    }


    fun getOfflineUserData() : LiveData<List<UserModel.DataX>>{
        return userDao.getUserData()
    }

    fun updateUser(userModel: UserModel.DataX) {
        userDao.updateUser(userModel)
    }

     fun delUser(userModel: UserModel.DataX) {
         viewModelScope.launch(Dispatchers.IO) {
             ApiClient.aPIService.deleteUser(userModel.id)
             userDao.deleteUser(userModel)
         }

    }


}