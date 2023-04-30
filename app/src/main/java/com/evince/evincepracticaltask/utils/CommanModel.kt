package com.evince.evincepracticaltask.utils

data class CommanModel<T> (var state:Int, var message:String, var data:T, val results: Results,var statusCode : Int?){
    data class Results(
        val msg: String,
        val status: Int
    )
}