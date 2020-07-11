package com.subzero.coviddiary.DataObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travel_record")
data class record(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "start_time")
    val start_time : String,
    @ColumnInfo(name = "end_time")
    val end_time : String,
    @ColumnInfo(name = "added_time")
    val added_time : String

    )