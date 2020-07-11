package com.subzero.coviddiary.DataObjects

import androidx.room.*

@Dao
interface TravelDataDao{
    @Query("SELECT * FROM travel_record")
    fun getAllRecords(): List<record>
    @Insert
    fun insert(record: record)
    @Delete
    fun delete(record: record)
    @Update
    fun update(record: record)
}