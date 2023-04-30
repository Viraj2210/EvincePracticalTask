package com.evince.evincepracticaltask.activity.ui

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.d2k.losapp.ui.connectivityobserver.ConnectivityObserver
import com.d2k.losapp.ui.connectivityobserver.NetworkConnectivityObserver
import com.d2k.losapp.ui.connectivityobserver.NoInternetConnectivity
import com.evince.evincepracticaltask.*
import com.evince.evincepracticaltask.activity.adapter.UserListAdapter
import com.evince.evincepracticaltask.activity.model.UserModel
import com.evince.evincepracticaltask.activity.vm.UserVM
import com.evince.evincepracticaltask.databinding.ActivityMainBinding
import com.evince.evincepracticaltask.extension.launchActivity

class MainActivity : AppCompatActivity(),UserListAdapter.OnItemClick {
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var userVM: UserVM
    lateinit var layoutManager : LinearLayoutManager
    lateinit var connectivityObserver: ConnectivityObserver
    lateinit var userList : MutableList<UserModel.DataX>
    var id : Int = 1
    var totalPage = 0
    var total_Pages = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)


        init()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun init(){

        userVM = ViewModelProvider(this).get(UserVM::class.java)

        connectivityObserver = NetworkConnectivityObserver(this)

        userList = mutableListOf()
        layoutManager = LinearLayoutManager(this)
        activityMainBinding.rvUser.setHasFixedSize(true)
        activityMainBinding.rvUser.layoutManager = layoutManager



        networkCall(id)
        observers()
       // listener()
    }

    fun observers(){
        userVM.getUserListData().observe(this, Observer<UserModel> {
            if (it!=null){
                val adapter = UserListAdapter(this@MainActivity,
                    it.data,
                    this
                )
                userList.addAll(it.data)
                userVM.insertIntoUser(userList)
                activityMainBinding.rvUser.adapter = adapter
                total_Pages = it.total_pages
                totalPage = it.total
            }
        })

        lifecycleScope.launchWhenCreated {
            connectivityObserver.observe().collect{
                if (it.equals(ConnectivityObserver.Status.Lost)){
                    getOffLineUser()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun networkCall(id : Int){
        userVM.getUserList(id)
    }

    @RequiresApi(Build.VERSION_CODES.M)
/*
    fun listener(){
        activityMainBinding.rvUser.setOnScrollChangeListener(object : View.OnScrollChangeListener{
            override fun onScrollChange(p0: View?, p1: Int, p2: Int, p3: Int, p4: Int) {
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                    firstVisibleItemPosition >= 0){

                    if (total_Pages < totalPage) {
                        networkCall(id++)
                    }else{
                        Toast.makeText(this@MainActivity,"No More Data available", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }
*/

    fun getOffLineUser(){
        userVM.getOfflineUserData().observe(this, Observer {
            val adapter = UserListAdapter(this@MainActivity,
                it,
                this
            )
            activityMainBinding.rvUser.adapter = adapter

        })
    }

    override fun onItemClick(userModel: UserModel.DataX) {
        launchActivity<UserProfileActivity> {
            putExtra("userModel",userModel)
        }
    }

    override fun onResume() {
        super.onResume()
        getOffLineUser()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(){
        val name = "Notify Channel"
        val desc = "Image Downloaded"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId,name,importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }



}