package com.evince.evincepracticaltask.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.evince.evincepracticaltask.activity.model.UserModel

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIntoUser(data: List<UserModel.DataX>)

    @Query("Select * from UserTable")
    fun getUserData() : LiveData<List<UserModel.DataX>>

    @Delete
    fun deleteUser(userModel: UserModel.DataX)

    @Update
    fun updateUser(userModel: UserModel.DataX)
}