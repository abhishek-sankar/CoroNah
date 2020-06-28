package com.subzero.coviddiary.DataObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_record_local_table")
class LocationRecord(
    @PrimaryKey @ColumnInfo(name="locationData") val locationData : FirebaseDataObject
)
