package com.subzero.coviddiary.DataObjects

import android.app.Application
import android.location.Location
import android.os.LocaleList
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : LocationRepository
    val _dontStartTillImReady = MutableLiveData<Boolean>()
    val dontStartTillImReady : LiveData<Boolean>
    get() = _dontStartTillImReady
    lateinit var mLocation : Location
    var LocationList : List<LocationRecord> = emptyList()
    var filteredLocationList : MutableList<LocationRecord> = ArrayList<LocationRecord>()
    var mapList : MutableList<LocationRecord> = ArrayList<LocationRecord>()
    var uniqueDateList : MutableList<String> =  ArrayList<String>()
    val allLocations : LiveData<List<LocationRecord>>
    init {
        val locationDataDao = LocationDatabase.getDatabase(application, viewModelScope).locationDataDao()
        repository = LocationRepository(locationDataDao)
        allLocations = repository.allLocations
        _dontStartTillImReady.value = false
    }
    fun insert(locationRecord: LocationRecord) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(locationRecord)
    }

    fun findUniqueDates(LocationList : List<LocationRecord>) {
        for (location in LocationList){
            var dateString = (location.month+1)+" "+location.date+" "+location.day
            if(!uniqueDateList.contains(dateString)){
                uniqueDateList.add(dateString)
            }
        }
    }

    fun findSelectedDateLocationEntries(selectedDay : Int, selectedMonth :Int) {
        mapList.clear()
         for (location in LocationList){
            if(location.date == selectedDay.toString() && location.month == selectedMonth.toString()){
                mapList.add(location)
                Log.i("InfindSelectedDateLocationEntries","Location : "+location.timeStamp)
            }
        }
        mapList.sortBy { it.timeStamp }
        mapList = mapList.asReversed()
        _dontStartTillImReady.value = true
    }
    fun deleteCloseByRecords(mapList : MutableList<LocationRecord>){
        var prevLatitude : Double = 0.0
        var prevLongitude : Double = 0.0
        for(location in mapList){
            if(location.latitude.toDouble()-prevLatitude>0.00001 || location.longitude.toDouble()-prevLongitude>0.00001) {
                filteredLocationList.add(location)
            }
            prevLatitude = location.latitude.toDouble()
            prevLongitude = location.longitude.toDouble()
        }
    }
}