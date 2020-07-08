package com.subzero.coviddiary.DataObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.*

@Entity(tableName = "location_record_local_table")
class LocationRecord(
    @PrimaryKey @ColumnInfo(name="timestamp") val timeStamp : Long,
    @ColumnInfo(name="latitude") val latitude : String,
    @ColumnInfo(name="longitude") val longitude : String,
    @ColumnInfo(name="month")val month :String,
    @ColumnInfo(name="date")val date : String,
    @ColumnInfo(name="day")val day :String,
    @ColumnInfo(name="uploadedToFirebaseDatabase") var uploadedToFirebaseDatabase : Boolean,
    @ColumnInfo(name="accuracy")val accuracy : String,
    @ColumnInfo(name="isMock")val isMock : Boolean
)
