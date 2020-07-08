package com.subzero.coviddiary

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.subzero.coviddiary.DataObjects.LocationViewModel
import com.subzero.coviddiary.databinding.ActivityMainBinding
import com.subzero.coviddiary.login.LoginFragment





class MainActivity : AppCompatActivity() {
    private val tag = "Activity-MainActivity"
    private lateinit var navController: NavController
    private lateinit var viewModel: LocationViewModel
    val user = Firebase.auth.currentUser
    var fm: FragmentManager = supportFragmentManager

    @SuppressLint("MissingPermission")
    override fun onStart() {
        super.onStart()
        if(user==null){Log.wtf("Login","To continue")
        val fragment = LoginFragment()
        fm.beginTransaction().add(R.id.loginFragment, fragment).commit();
//            Navigation.findNavController(this.)
////                .navigate(R.id.action_loginFragment_to_profileFragment)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

            //Setup Permissions => DONE
            //In case its not there, request => DONE
        //Request Internet Access too
        //If no internet first time, head to error page, butt   on => Back to login
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
//        fragmentManager.beginTransaction().add(R.id.bottom_nav,profileFragment).replace(profileFragment).commit()
//        fragmentManager.beginTransaction().add(R.id.bottom_nav,mapFragment).commit()
        navController = Navigation.findNavController(this,R.id.bottom_nav)
        binding.bottomNavigationView.setupWithNavController(navController)

//        binding.bottomNavigationView.setOnNavigationItemReselectedListener {
//            fun onOptionsItemSelected(item: MenuItem): Boolean {
//                when(item.itemId){
//                    R.id.map->{fragmentManager.beginTransaction().hide(fragmentActive).show(mapFragment)
//                                fragmentActive = mapFragment
//                                return true
//                    }
//                    R.id.settings->{fragmentManager.beginTransaction().hide(fragmentActive).show(settingsFragment)
//                        fragmentActive = settingsFragment
//                        return true
//                    }
//                    R.id.profile->{fragmentManager.beginTransaction().hide(fragmentActive).show(profileFragment)
//                        fragmentActive = profileFragment
//                        return true
//                    }
//                }
//                return false
//            }
//        }
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