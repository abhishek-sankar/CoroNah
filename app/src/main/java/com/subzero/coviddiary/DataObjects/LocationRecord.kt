package com.subzero.coviddiary.DataObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_record_local_table")
class LocationRecord(
    @PrimaryKey @ColumnInfo(name="timestamp") val timeStamp : String,
    @ColumnInfo(name="latitude") val latitude : String,
    @ColumnInfo(name="longitude") val longitude : String,
    @ColumnInfo(name="uploadedToFirebaseDatabase") val uploadedToFirebaseDatabase : Boolean
)
