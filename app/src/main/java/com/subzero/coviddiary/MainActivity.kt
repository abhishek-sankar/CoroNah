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
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.subzero.coviddiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "PermissionDemo"
    private val requestingLocationUpdates = true
    private val RECORD_REQUEST_CODE = 1
    private val RECORD_REQUEST_CODE_FINE = 2
    private val RECORD_REQUEST_CODE_BG = 3
    private val REQUEST_CHECK_SETTINGS = 4
    private lateinit var fusedLocationClient: FusedLocationProviderClient
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
        setupPermissions()
        database = Firebase.database.reference
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                if(location!=null)
                mLocation = location
                Log.i("Initialising mLocation : ","Latitude : "+mLocation.latitude+" Longitude : "+mLocation.longitude)
            }
//        fun createLocationRequest() {

            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest!!)
            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
            task.addOnSuccessListener { locationSettingsResponse ->
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException){
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        exception.startResolutionForResult(this@MainActivity,
                            REQUEST_CHECK_SETTINGS)
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                    // ...
                    mLocation.latitude = location.latitude
                    mLocation.longitude = location.longitude
                    Log.i("In onLocationResult","Latitude : "+location.latitude+" Longitude : "+location.longitude)
                }
            }
        }
//        } locationRequestCreate
        startLocationUpdates()
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//        TODO("https://developer.android.com/training/location/request-updates")
    }
    override fun onResume() {
        super.onResume()
//        if (requestingLocationUpdates) startLocationUpdates()
    }
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
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
        if (locationCoarsePermission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to access coarse Location denied")
            makeRequestCoarseLocation()
        }else{
            Log.i(TAG,"Permission to access coarse Location Granted")
        }
        if(locationFinePermission != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission to access fine Location denied")
            makeRequestCoarseLocation()
        }else{
            Log.i(TAG,"Permission to access fine Location Granted")
        }
        if(locationBackgroundPermission != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "Permission to access background Location denied")
            makeRequestBackgroundLocation()
        }else{
            Log.i(TAG,"Permission to access background Location Granted")
        }
    }

    private fun makeRequestCoarseLocation() {

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            RECORD_REQUEST_CODE)
    }
    private fun makeRequestBackgroundLocation() {

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
            RECORD_REQUEST_CODE_BG)
    }
    private fun makeRequestFineLocation() {

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            RECORD_REQUEST_CODE_FINE)
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
                        Toast.makeText(this, "Permission Coarse Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Coarse Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            2 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Fine Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Fine Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
            3 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Background Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Background Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}