package com.subzero.coviddiary

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.DatabaseUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.subzero.coviddiary.LocationBackgroundService.LocationUtils
import com.subzero.coviddiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG = "PermissionDemo"
    private val RECORD_REQUEST_CODE = 1
    private val     RECORD_REQUEST_CODE_FINE = 2

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPermissions()
        var location = LocationUtils()
        location.getInstance(this)
        Log.i("LocationUtils : ",location.getInstance(this).toString())
        location.getLocation(this).observe(this, Observer {
            var locationUpdated = it!!
            Log.i("In addEntryFrag","Received location "+locationUpdated.latitude+" "+locationUpdated.longitude)
        })
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)


    }
    private fun setupPermissions() {
        val locationCoarsePermission = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        val locationFinePermission = ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION)
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
    }

    private fun makeRequestCoarseLocation() {

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            RECORD_REQUEST_CODE)
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
        }
    }
}