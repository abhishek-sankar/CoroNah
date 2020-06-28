package com.subzero.coviddiary.DataObjects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : LocationRepository
    val allLocations : LiveData<List<LocationRecord>>
    init {
        val locationDataDao = LocationDatabase.getDatabase(application, viewModelScope).locationDataDao()
        repository = LocationRepository(locationDataDao)
        allLocations = repository.allLocations
    }
    fun insert(locationRecord: LocationRecord) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(locationRecord)
    }

}