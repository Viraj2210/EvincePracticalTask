package com.evince.evincepracticaltask.activity.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserModel(
    val `data`: List<DataX>,
    val page: Int,
    val per_page: Int,
    val support: Support,
    val total: Int,
    val total_pages: Int
)
{
    data class Support(
        val text: String,
        val url: String
    )

    @Entity(tableName = "UserTable")
    data class DataX(
        val avatar: String,
        val email: String,
        val first_name: String,
        @PrimaryKey val id: Int,
        val last_name: String
    ):java.io.Serializable
}