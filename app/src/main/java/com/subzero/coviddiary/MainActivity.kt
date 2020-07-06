package com.subzero.coviddiary

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
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
    private lateinit var ViewModel: LocationViewModel
    private lateinit var database : DatabaseReference
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            //Setup Permissions => DONE
            //In case its not there, request => DONE
        //Request Internet Access too
        //If no internet first time, head to error page, button => Back to login
            //Setup ViewModel => DONE
            //Ensure Permission and start requesting updates => DONE
        //Implement button to pause
        //Implement slider to change rate
        //Check for data and Get data from DB
            //Create Login fragment => DONE
        setupViewModel()
        ViewModel.setupPermissions(this, this)
        ViewModel.setupLocationListener(this,this)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    fun setupViewModel() {
        ViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        ViewModel.allLocations.observe(this, androidx.lifecycle.Observer {
            ViewModel.LocationList = it
        })
        Log.i(ActivityTag, "ViewModel.allLocations : " + ViewModel.allLocations.toString())
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