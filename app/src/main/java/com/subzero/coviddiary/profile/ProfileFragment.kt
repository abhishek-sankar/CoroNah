package com.subzero.coviddiary.profile

import android.annotation.SuppressLint
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.subzero.coviddiary.DataObjects.LocationRecord
import com.subzero.coviddiary.DataObjects.LocationViewModel
import com.subzero.coviddiary.R
import com.subzero.coviddiary.databinding.FragmentProfileBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import java.util.*


class ProfileFragment : Fragment(), OnMapReadyCallback {
    private lateinit var viewModel : LocationViewModel
    var selectedMonth = Calendar.MONTH
    var selectedDay = Calendar.DAY_OF_MONTH
    lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var maxDate : LocationRecord?
        var minDate : LocationRecord?

        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        viewModel.allLocations.observe(viewLifecycleOwner, Observer {
            viewModel.LocationList = it
            viewModel.findUniqueDates(viewModel.LocationList)
             maxDate  = viewModel.LocationList.maxBy {
                it.timeStamp
            }
            minDate  = viewModel.LocationList.minBy {
                it.timeStamp
            }
            Log.i("Min date :"+minDate?.timeStamp,"Max date : "+maxDate?.timeStamp)
//            if(minDate!=null && maxDate!=null) {
////            binding.calendarView.maxDate = maxDate!!.timeStamp.toLong()
//                binding.calendarView.minDate = minDate!!.timeStamp.toLong()
//            }
        })
        val user = FirebaseAuth.getInstance().currentUser
        binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)

        binding.calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            Log.i("OnDateChange","Strings are : "+i+" "+i2+" "+i3)
            selectedDay = i3
            selectedMonth = i2
            Log.i("OnDateChange","Strings are : "+selectedDay+" "+selectedMonth)
            viewModel.findSelectedDateLocationEntries(selectedDay, selectedMonth)
        }
        binding.greetingTextView.text =
            "Hey ${(user!!.displayName)?.split("\\s".toRegex())?.first()},\nAll the best.\nYou do the writing, we do the tracking :)"
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        viewModel.findSelectedDateLocationEntries(selectedDay, selectedMonth)
        viewModel.dontStartTillImReady.observe(viewLifecycleOwner, Observer {
            val prevLatitude = 9.999825
            val prevLongitude = 76.30822
            for(item in viewModel.mapList.indices){
            var line = googleMap.addPolyline(
                PolylineOptions().add(LatLng(prevLatitude,prevLongitude),
                    LatLng(viewModel.mapList[item].latitude.toDouble(), viewModel.mapList[item].longitude.toDouble()))
                    .width(10F).color(Color.BLUE).geodesic(true)
                )
            Log.i("InPolylineCreation :",viewModel.mapList[item].latitude)
        }})
    }
}