package com.sid.stolker.persistence

import android.annotation.SuppressLint
import android.arch.persistence.room.Room
import android.content.Context

@SuppressLint("StaticFieldLeak")
object DatabaseManager {
    private var appDatabaseInstance: AppDatabase? = null
    lateinit var appContext: Context

    fun getAppDatabaseInstance(): AppDatabase {
        if (appDatabaseInstance?.isOpen != true)
            appDatabaseInstance = Room.databaseBuilder(appContext, AppDatabase::class.java, "stolker")
                    .fallbackToDestructiveMigration()
                    .build()
        return appDatabaseInstance!!
    }
}