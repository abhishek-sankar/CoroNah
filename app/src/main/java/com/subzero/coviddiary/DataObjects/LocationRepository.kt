package com.subzero.coviddiary.DataObjects

import androidx.lifecycle.LiveData

class LocationRepository(private val locationDataDao: locationDataDao) {

    val allLocations : LiveData<List<LocationRecord>> = locationDataDao.getAllLocationUpdates()
    suspend fun insert(locationRecord: LocationRecord){
        locationDataDao.insert(locationRecord)
    }
    suspend fun updateFirebase(locationRecord: LocationRecord){
        locationDataDao.updateFirebaseStatus(locationRecord)
    }
}