package com.subzero.coviddiary.DataObjects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : LocationRepository
    val allLocations : LiveData<List<FirebaseDataObject>>
    init {
        val locationDataDao = LocationDatabase.getDatabase(application).locationDataDao()
        repository = LocationRepository(locationDataDao)
        allLocations = repository.allLocations
    }
    fun insert(firebaseDataObject: FirebaseDataObject) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(firebaseDataObject)
    }

}