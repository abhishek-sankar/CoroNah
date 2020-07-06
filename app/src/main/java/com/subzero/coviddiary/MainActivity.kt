package com.subzero.coviddiary

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.subzero.coviddiary.DataObjects.LocationRecord
import com.subzero.coviddiary.DataObjects.LocationViewModel
import com.subzero.coviddiary.databinding.ActivityMainBinding
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "PermissionDemo"
    val ActivityTag = "Activity-MainActivity"
    private val requestingLocationUpdates = true
    private val RECORD_REQUEST_CODE = 1
    private val REQUEST_CHECK_SETTINGS = 2
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var ViewModel: LocationViewModel
    lateinit var mLocation : Location
    private lateinit var locationCallback: LocationCallback
    val locationRequest = LocationRequest.create()?.apply {
        interval = 100000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private lateinit var database : DatabaseReference
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Setup Permissions
            //In case its not there, request
            //Request Internet Access too
            //If no internet first time, head to error page, button => Back to login
        //Setup ViewModel
        //Ensure Permission and start requesting updates
            //Implement button to pause
            //Implement slider to change rate
        //Check for data and Get data from DB
        //Create Login fragment

        setupPermissions()
        setupViewModel()
        setupLocationListener()
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    fun setupLocationListener() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            setupPermissions()
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    mLocation = location
                    ViewModel.mLocation = location
                }
                Log.i(ActivityTag," Initialising mLocation => Latitude : " + mLocation.latitude + " Longitude : " + mLocation.longitude)
            }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            if(locationSettingsResponse.locationSettingsStates.isLocationPresent)
            startLocationUpdates()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        this@MainActivity,
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
                    mLocation.latitude = location.latitude
                    mLocation.longitude = location.longitude
                    Log.i(ActivityTag,"In onLocationResult Latitude : " + location.latitude + " Longitude : " + location.longitude)
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
                        ActivityTag,"Accuracy : " + location.accuracy + " IsMock : " + location.isFromMockProvider)
                    ViewModel.insert(locationRecordNew)
                }
            }
        }
    }

    fun setupViewModel() {
        ViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        ViewModel.allLocations.observe(this, androidx.lifecycle.Observer {
            ViewModel.LocationList = it
        })
        Log.i(ActivityTag, "ViewModel.allLocations : " + ViewModel.allLocations.toString())
    }

     fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            setupPermissions()
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }
    private fun setupPermissions() {
        val locationCoarsePermission = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        val locationFinePermission = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION)
        val locationBackgroundPermission = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        var mIndex: Int = -1
        var requestList: Array<String> = Array(10, { "" } )
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mIndex ++
            requestList[mIndex] = Manifest.permission.ACCESS_FINE_LOCATION
        }
        if(locationBackgroundPermission != PackageManager.PERMISSION_GRANTED){
            Log.i(ActivityTag, "Permission to access background Location denied")
            mIndex++
            requestList[mIndex] = Manifest.permission.ACCESS_BACKGROUND_LOCATION
        }else{
            Log.i(ActivityTag,"Permission to access background Location Granted")
        }
        if(mIndex!=-1){
            ActivityCompat.requestPermissions(this, requestList, RECORD_REQUEST_CODE)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                    }
                } else {
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}