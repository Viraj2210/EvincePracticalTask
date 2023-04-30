package com.d2k.losapp.ui.connectivityobserver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.evince.evincepracticaltask.R
import kotlinx.coroutines.flow.collect

class NoInternetConnectivity : AppCompatActivity() {
    private lateinit var connectivityObserver: ConnectivityObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet_connectivity)

        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        lifecycleScope.launchWhenCreated {
            connectivityObserver.observe().collect {
                if (it.equals(ConnectivityObserver.Status.Available)){
                    finish()
                }
            }
        }
    }
}