package com.eagletech.happynote.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eagletech.happynote.database.daodata.DataDao
import com.eagletech.happynote.entities.Data

@Database(entities = [Data::class], version = 1)
abstract class DataDatabase: RoomDatabase() {

    abstract fun getDataDao(): DataDao

    companion object{
        @Volatile
        private var instance: DataDatabase? = null

        fun getInstance(context: Context): DataDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, DataDatabase::class.java, "DataDatabase")
                    .build()
            }
            return instance!!
        }
    }
}