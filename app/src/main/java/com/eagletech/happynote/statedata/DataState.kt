package com.eagletech.happynote.statedata

import com.eagletech.happynote.entities.Data

sealed class DataState{
    data object Loading : DataState()
    class Failure(val msg: Throwable) : DataState()
    class Success(val data: List<Data>) : DataState()
    class Empty(val data: List<Data>) : DataState()
}
