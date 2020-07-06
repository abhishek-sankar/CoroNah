package com.subzero.coviddiary.DataObjects

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.format.DateTimeFormatter

@Database(entities = arrayOf(LocationRecord::class), version = 2, exportSchema = false)
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
//                    var locationData = FirebaseDataObject()
//                    locationData.timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
//                    var LocationRecordVar = LocationRecord(locationData.timestamp, locationData.latitude, locationData.longitude, locationData.uploadedToDatabase)
//                    locationDataDao.insert(LocationRecordVar)
                }

            }
        }
    }

    companion object{
        val MIGRATION_1_2 = object : Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE location_record_local_table ADD COLUMN accuracy TEXT NOT NULL DEFAULT 'null'")
                database.execSQL("ALTER TABLE location_record_local_table ADD COLUMN isMock INTEGER NOT NULL DEFAULT 'null'")
            }
        }
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
                ).addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance

            }
        }
    }
}