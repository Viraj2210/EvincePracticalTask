package com.evince.evincepracticaltask.utils

import retrofit2.Response

abstract class BaseDataSource {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resources<T> {

        try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Resources.success(body)
                }
            }
            else{
                return Resources.error(null, response.message())
            }

            return Resources.error(null, "Something went wrong, try again")

        } catch (e: Exception) {
            return Resources.error(null, "Something went wrong, $e")
        }
    }
}
