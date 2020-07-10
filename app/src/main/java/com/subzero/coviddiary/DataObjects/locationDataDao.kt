package com.subzero.coviddiary.DataObjects

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface locationDataDao {
    @Query("SELECT * from location_record_local_table ORDER BY timestamp ASC")
    fun getAllLocationUpdates() : LiveData<List<LocationRecord>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data : LocationRecord)
    @Query("DELETE FROM location_record_local_table")
    suspend fun deleteAllLocationEntries()
    @Update()
    fun updateFirebaseStatus(data: LocationRecord)

}