package com.subzero.coviddiary.DataObjects

import androidx.lifecycle.LiveData

class LocationRepository(private val locationDataDao: locationDataDao) {

    val allLocations : LiveData<List<FirebaseDataObject>> = locationDataDao.getAllLocationUpdates()
    suspend fun insert(firebaseDataObject : FirebaseDataObject){
        locationDataDao.insert(firebaseDataObject)
    }
}