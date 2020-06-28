package com.subzero.coviddiary.DataObjects

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(FirebaseDataObject::class), version = 1, exportSchema = false)
public abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDataDao(): locationDataDao

    companion object{
        @Volatile
        private var INSTANCE : LocationDatabase?=null

        fun getDatabase(context: Context): LocationDatabase{
            val tempInstance = INSTANCE
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationDatabase::class.java,
                    "location_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}