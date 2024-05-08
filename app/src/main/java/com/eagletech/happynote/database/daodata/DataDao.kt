package com.eagletech.happynote.database.daodata

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eagletech.happynote.entities.Data
import kotlinx.coroutines.flow.Flow


@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: Data)

    @Update
    suspend fun updateData(data: Data)

    @Delete
    suspend fun deleteData(data: Data)

    @Query("SELECT * FROM datas ORDER BY idData DESC")
    fun getAllData(): Flow<List<Data>>
}