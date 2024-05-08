package com.eagletech.happynote.viewml

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.eagletech.happynote.database.repo.DataRepository
import com.eagletech.happynote.entities.Data
import com.eagletech.happynote.statedata.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DataViewModel(application: Application) : ViewModel() {
    private val dataRepository: DataRepository = DataRepository(application)

    private val dataSateFlow: MutableStateFlow<DataState> = MutableStateFlow(
        DataState.Empty(
            emptyList()
        )
    )

    val _dataSateFlow: StateFlow<DataState> = dataSateFlow

    fun insertData(data: Data) = viewModelScope.launch { dataRepository.insertData(data) }
    fun updateData(data: Data) = viewModelScope.launch { dataRepository.updateData(data) }
    fun deleteData(data: Data) = viewModelScope.launch { dataRepository.deleteData(data) }

    fun getAllData() = viewModelScope.launch {
        dataSateFlow.value = DataState.Loading
        dataRepository.getAllData()
            .catch { e ->
                dataSateFlow.value = DataState.Failure(e)
            }.collect { data ->
                if (data.isEmpty()) {
                    dataSateFlow.value = DataState.Empty(data)
                } else {
                    dataSateFlow.value = DataState.Success(data)
                }

            }
    }

    class DataViewModelFactory(private val application: Application) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DataViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DataViewModel(application) as T
            }
            throw IllegalArgumentException("Unable construct viewModel")
        }

    }
}