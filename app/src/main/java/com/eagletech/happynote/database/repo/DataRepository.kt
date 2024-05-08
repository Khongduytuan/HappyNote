package com.eagletech.happynote.database.repo

import android.app.Application
import com.eagletech.happynote.database.DataDatabase
import com.eagletech.happynote.database.daodata.DataDao
import com.eagletech.happynote.entities.Data
import kotlinx.coroutines.flow.Flow

class DataRepository(app: Application) {
    private val dataDao: DataDao

    init {
        val dataDatabase: DataDatabase = DataDatabase.getInstance(app)
        dataDao = dataDatabase.getDataDao()
    }

    suspend fun insertData(data: Data) = dataDao.insertData(data)

    suspend fun updateData(data: Data) = dataDao.updateData(data)

    suspend fun deleteData(data: Data) = dataDao.deleteData(data)

    fun getAllData(): Flow<List<Data>> = dataDao.getAllData()


}