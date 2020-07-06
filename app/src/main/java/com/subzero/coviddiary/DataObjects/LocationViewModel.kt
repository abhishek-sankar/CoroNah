package com.subzero.coviddiary.DataObjects

import android.app.Application
import android.location.Location
import android.os.LocaleList
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : LocationRepository
    val ActivityTag = "Activity-LocationViewModel"

    val _dontStartTillImReady = MutableLiveData<Boolean>()
    val dontStartTillImReady : LiveData<Boolean>
    get() = _dontStartTillImReady
    lateinit var mLocation : Location
    lateinit var LocationList : List<LocationRecord>
    var filteredLocationList : MutableList<LocationRecord> = ArrayList<LocationRecord>()
    var mapList : MutableList<LocationRecord> = ArrayList<LocationRecord>()
    var uniqueDateList : MutableList<String> =  ArrayList<String>()
    val allLocations : LiveData<List<LocationRecord>>
    init {
        val locationDataDao = LocationDatabase.getDatabase(application, viewModelScope).locationDataDao()
        repository = LocationRepository(locationDataDao)
        allLocations = repository.allLocations
        _dontStartTillImReady.value = false
        LocationList = emptyList()
    }
    fun insert(locationRecord: LocationRecord) = viewModelScope.launch (Dispatchers.IO){
        repository.insert(locationRecord)
    }

    fun findUniqueDates(LocationList : List<LocationRecord>) {
        Log.i(ActivityTag,"Inside fundUniqueDates() LocationViewModel")
        for (location in LocationList){
            var dateString = location.month+" "+location.date+" "+location.day
            if(!uniqueDateList.contains(dateString)){
                uniqueDateList.add(dateString)
            }
        }
    }

     fun findSelectedDateLocationEntries(selectedDay : Int, selectedMonth :Int) {
        mapList.clear()
        Log.i(ActivityTag,"Inside findSelectedData Ensure MapList is cleared : mapList.isEmpty(): "+mapList.isEmpty())
        Log.i(ActivityTag,"Inside findSelectedData LocationList :"+LocationList.isEmpty()+" Day/Month"+selectedDay+" "+selectedMonth)

         for (location in LocationList){
            if(location.date == selectedDay.toString() && location.month == selectedMonth.toString()){
                mapList.add(location)
                Log.i(ActivityTag,"InfindSelectedDateLocationEntries Selected Day/Month"+selectedDay+"/"+selectedMonth+" Location : "+location.latitude)
            }
        }
        mapList.sortBy { it.timeStamp }
        mapList = mapList.asReversed()
//        if(mapList.size>20)
//        deleteCloseByRecords(mapList)

        _dontStartTillImReady.value = _dontStartTillImReady.value != true
    }
    fun deleteCloseByRecords(mapList : MutableList<LocationRecord>){
        Log.i(ActivityTag,"Inside deleteCloseByRecords LocationViewModel")
        var prevLatitude : Double = 0.0
        var prevLongitude : Double = 0.0
        for(location in mapList){
            if(location.latitude.toDouble()-prevLatitude>0.00001 || location.longitude.toDouble()-prevLongitude>0.00001) {
                filteredLocationList.add(location)
            }
            prevLatitude = location.latitude.toDouble()
            prevLongitude = location.longitude.toDouble()
        }
        this.mapList = filteredLocationList
    }
}