package com.subzero.coviddiary.DataObjects

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter

@Database(entities = arrayOf(LocationRecord::class), version = 1, exportSchema = false)
public abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDataDao(): locationDataDao

    private class locationDatabaseCallback(private val scope: CoroutineScope):RoomDatabase.Callback(){
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let{database ->
                scope.launch {
                    val locationDataDao = database.locationDataDao()

                    //Delete All Content
                    locationDataDao.deleteAllLocationEntries()
                    var locationData = FirebaseDataObject()
                    locationData.timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
                    var LocationRecordVar = LocationRecord(locationData.timestamp, locationData.latitude, locationData.longitude, locationData.uploadedToDatabase)
                    locationDataDao.insert(LocationRecordVar)
                }

            }
        }
    }
    companion object{
        @Volatile
        private var INSTANCE : LocationDatabase?=null

        fun getDatabase(context: Context, scope: CoroutineScope): LocationDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            return INSTANCE?:synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "location_database"
                ).addCallback(locationDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }
}