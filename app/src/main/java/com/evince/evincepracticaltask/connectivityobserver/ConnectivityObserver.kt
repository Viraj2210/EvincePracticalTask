package com.d2k.losapp.ui.connectivityobserver

import java.util.concurrent.Flow

interface ConnectivityObserver {
    fun observe() : kotlinx.coroutines.flow.Flow<Status>

    enum class Status {
        Available, Unavailable, Losing, Lost
    }
}