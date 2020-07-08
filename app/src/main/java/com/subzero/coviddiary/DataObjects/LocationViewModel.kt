package com.subzero.coviddiary.DataObjects

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.LocaleList
import android.os.Looper
import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import com.subzero.coviddiary.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LocationRepository
    val ActivityTag = "Activity-LocationViewModel"
    lateinit var fusedLocationClient: FusedLocationProviderClient
    private val RECORD_REQUEST_CODE = 1
    lateinit var locationCallback: LocationCallback
    val _dontStartTillImReady = MutableLiveData<Boolean>()
    val dontStartTillImReady: LiveData<Boolean>
        get() = _dontStartTillImReady
    private val REQUEST_CHECK_SETTINGS = 2
    lateinit var mLocation: Location
    var LocationList: List<LocationRecord>
    var filteredLocationList: MutableList<LocationRecord> = ArrayList<LocationRecord>()
    var mapList: MutableList<LocationRecord> = ArrayList<LocationRecord>()
    var uniqueDateList: MutableList<Date> = ArrayList<Date>()
    val allLocations: LiveData<List<LocationRecord>>

    init {
        val locationDataDao =
            LocationDatabase.getDatabase(application, viewModelScope).locationDataDao()
        repository = LocationRepository(locationDataDao)
        allLocations = repository.allLocations
        _dontStartTillImReady.value = false
        LocationList = emptyList()
    }

    fun insert(locationRecord: LocationRecord) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(locationRecord)
    }

    fun findUniqueDates(LocationList: List<LocationRecord>) {
        Log.i(ActivityTag, "Inside fundUniqueDates() LocationViewModel")
        for (location in LocationList) {
//            var dateString = location.month + " " + location.date + " " + location.day
            var dateNew = Date(120,location.month.toInt(),location.date.toInt())
            if (!uniqueDateList.contains(dateNew)) {
                uniqueDateList.add(dateNew)
            }
        }
    }

    fun findSelectedDateLocationEntries(selectedDay: Int, selectedMonth: Int) {
        mapList.clear()
        Log.i(
            ActivityTag,
            "Inside findSelectedData Ensure MapList is cleared : mapList.isEmpty(): " + mapList.isEmpty()
        )
        Log.i(
            ActivityTag,
            "Inside findSelectedData LocationList :" + LocationList.isEmpty() + " Day/Month" + selectedDay + " " + selectedMonth
        )

        for (location in LocationList) {
            Log.d("Location.date : "+ location.date+ " selectedDay.toString() : "+ selectedDay.toString(),"Location.month : "+ location.month+ " selectedMonth.toString() : "+ selectedMonth.toString())
            if (location.date == selectedDay.toString() && location.month == selectedMonth.toString()) {
                if (location.accuracy != null.toString() && location.accuracy.toFloat() < 50.0) {
                    mapList.add(location)
                    Log.i(
                        ActivityTag,
                        "InfindSelectedDateLocationEntries Selected Day/Month" + selectedDay + "/" + selectedMonth + " Location : " + location.latitude
                    )
                }
            }
        }
        mapList.sortBy { it.timeStamp }
        mapList = mapList.asReversed()
//        if(mapList.size>20)
//        deleteCloseByRecords(mapList)

        _dontStartTillImReady.value = _dontStartTillImReady.value != true
    }

    fun deleteCloseByRecords(mapList: MutableList<LocationRecord>) {
        Log.i(ActivityTag, "Inside deleteCloseByRecords LocationViewModel")
        var prevLatitude: Double = 0.0
        var prevLongitude: Double = 0.0
        for (location in mapList) {
            if (location.latitude.toDouble() - prevLatitude > 0.00001 || location.longitude.toDouble() - prevLongitude > 0.00001) {
                filteredLocationList.add(location)
            }
            prevLatitude = location.latitude.toDouble()
            prevLongitude = location.longitude.toDouble()
        }
        this.mapList = filteredLocationList
    }

    val locationRequest = LocationRequest.create()?.apply {
        interval = 100000
        fastestInterval = 5000
        smallestDisplacement = 17f
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun setupLocationListener(context: Context, activity: Activity) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            setupPermissions(context, activity)
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mLocation = location
                    Log.i(
                        ActivityTag,
                        " Initialising mLocation => Latitude : " + mLocation.latitude + " Longitude : " + mLocation.longitude
                    )
                }
            }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            if (locationSettingsResponse.locationSettingsStates.isLocationPresent)
                startLocationUpdates(context, activity)
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        activity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    mLocation = Location(location)
                    Log.i(
                        ActivityTag,
                        "In onLocationResult Latitude : " + location.latitude + " Longitude : " + location.longitude
                    )
                    var date = Calendar.getInstance()
                    var locationRecordNew = LocationRecord(
                        (location.time),
                        location.latitude.toString(),
                        location.longitude.toString(),
                        date.get(Calendar.MONTH).toString(),
                        date.get(Calendar.DAY_OF_MONTH).toString(),
                        date.get(Calendar.DAY_OF_WEEK).toString(),
                        false,
                        location.accuracy.toString(),
                        location.isFromMockProvider
                    )
                    Log.i(
                        ActivityTag,
                        "Accuracy : " + location.accuracy + " IsMock : " + location.isFromMockProvider
                    )
                    insert(locationRecordNew)
                }
            }
        }
    }

    fun setupPermissions(context: Context, activity: Activity) {

        val locationBackgroundPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
        var mIndex: Int = -1
        var requestList: Array<String> = Array(10, { "" })
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            mIndex++
            requestList[mIndex] = Manifest.permission.ACCESS_FINE_LOCATION
        }
        if (locationBackgroundPermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(ActivityTag, "Permission to access background Location denied")
            mIndex++
            requestList[mIndex] = Manifest.permission.ACCESS_BACKGROUND_LOCATION
        } else {
            Log.i(ActivityTag, "Permission to access background Location Granted")
        }
        if (mIndex != -1) {
            ActivityCompat.requestPermissions(activity, requestList, RECORD_REQUEST_CODE)
        }
    }

    fun startLocationUpdates(context: Context, activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            setupPermissions(context, activity)
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}