package com.evince.evincepracticaltask.utils

data class Resources<out T>(val status: Status, val data: T?, val msg: String?) {
    companion object {
        fun <T> success(data: T): Resources<T> = Resources(status = Status.SUCCESS, data = data, msg = null)

        fun <T> error(data: T?, message: String): Resources<T> = Resources(status = Status.ERROR, data = data, msg = message)

        fun <T> loading(data: T?): Resources<T> = Resources(status = Status.LOADING, data = data, msg = null)
    }
}
