package com.subzero.coviddiary

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.subzero.coviddiary.DataObjects.LocationViewModel
import com.subzero.coviddiary.Map.MapFragment
import com.subzero.coviddiary.Profile.ProfileFragment
import com.subzero.coviddiary.Settings.SettingsFragment
import com.subzero.coviddiary.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.fragment_map.*

class MainActivity : AppCompatActivity() {
    private val tag = "Activity-MainActivity"
    private lateinit var navController: NavController
    private lateinit var viewModel: LocationViewModel
//    var mapFragment = MapFragment()
//    var settingsFragment = SettingsFragment()
//    var profileFragment = ProfileFragment()
//    var fragmentManager = supportFragmentManager
//    var fragmentActive : Fragment = mapFragment
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
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
        //QR check if both person and place have had exposure
        setupViewModel()
        viewModel.setupPermissions(this, this)
        viewModel.setupLocationListener(this,this)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//        fragmentManager.beginTransaction().add(R.id.bottom_nav,settingsFragment).hide(settingsFragment).commit()
//        fragmentManager.beginTransaction().add(R.id.bottom_nav,profileFragment).hide(profileFragment).commit()
//        fragmentManager.beginTransaction().add(R.id.bottom_nav,mapFragment).commit()
        navController = Navigation.findNavController(this,R.id.bottom_nav)
        binding.bottomNavigationView.setupWithNavController(navController)

//        binding.bottomNavigationView.setOnNavigationItemReselectedListener {
//            fun onOptionsItemSelected(item: MenuItem): Boolean {
//                when(item.itemId){
//                    R.id.map->{fragmentManager.beginTransaction().hide(fragmentActive).show(mapFragment)
//                                fragmentActive = mapFragment
//                                return false
//                    }
//                    R.id.settings->{fragmentManager.beginTransaction().hide(fragmentActive).show(settingsFragment)
//                        fragmentActive = settingsFragment
//                        return false
//                    }
//                    R.id.profile->{fragmentManager.beginTransaction().hide(fragmentActive).show(profileFragment)
//                        fragmentActive = profileFragment
//                        return false
//                    }
//                }
//                return false
//            }
//        }
        binding.bottomNavigationView.setOnNavigationItemReselectedListener {  }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED)) {
                        Log.i(tag,"Permission Granted")
                    }
                } else {
                    Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}