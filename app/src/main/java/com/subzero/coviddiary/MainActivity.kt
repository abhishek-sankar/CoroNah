package com.subzero.coviddiary

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.subzero.coviddiary.DataObjects.LocationViewModel
import com.subzero.coviddiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val tag = "Activity-MainActivity"
    private lateinit var navController: NavController
    private lateinit var viewModel: LocationViewModel

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setupViewModel()
        val progressDialog = ProgressDialog(this)
        progressDialog.show()
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        viewModel.setupPermissions(this, this)
        viewModel.allLocations.observe(this, Observer {
            viewModel.LocationList = it
            progressDialog.dismiss()
        })
        viewModel.setupLocationListener(this, this)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.bottom_nav)

        binding.bottomNavigationView.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        viewModel.allLocations.observe(this, androidx.lifecycle.Observer {
            viewModel.LocationList = it
        })
        Log.i(tag, "ViewModel.allLocations : " + viewModel.allLocations.toString())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) ==
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Log.i(tag, "Permission Granted")
                    }
                } else {
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}