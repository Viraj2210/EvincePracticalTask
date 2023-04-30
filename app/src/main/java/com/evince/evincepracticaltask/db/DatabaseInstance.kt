package com.evince.evincepracticaltask.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.evince.evincepracticaltask.activity.model.UserModel

@Database(entities = [UserModel.DataX::class],
    version = 1, exportSchema = true)
abstract class DatabaseInstance : RoomDatabase() {

    abstract fun userDao() : UserDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseInstance? = null

        fun getDatabaseClient(context: Context): DatabaseInstance {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(
                        context,
                        DatabaseInstance::class.java,
                        context.applicationInfo.loadLabel(context.packageManager).toString()
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                return INSTANCE!!
            }
        }
    }
}