package com.subzero.coviddiary.DataObjects

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface locationDataDao {
    @Query("SELECT * from location_record_local_table ORDER BY id ASC")
    fun getAllLocationUpdates() : LiveData<List<LocationRecord>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data : LocationRecord)
    @Query("DELETE FROM location_record_local_table")
    suspend fun deleteAllLocationEntries()
}