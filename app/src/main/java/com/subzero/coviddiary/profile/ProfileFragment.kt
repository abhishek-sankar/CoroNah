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
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import java.util.*


class ProfileFragment : Fragment(), OnMapReadyCallback {
    private lateinit var viewModel : LocationViewModel
    val ActivityTag = "Activity-ProfileFragment"
    var selectedMonth = Calendar.getInstance().get(Calendar.MONTH)
    var selectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    lateinit var polyLineFinal : Polyline
    lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var maxDate : LocationRecord?
        var minDate : LocationRecord?
        Log.i(ActivityTag,"Inside ProfileFragment.onCreateView() Selected Date : "+selectedDay+ " Selected month : "+selectedMonth)
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        viewModel.allLocations.observe(viewLifecycleOwner, Observer {
            Log.i(ActivityTag,"LocationList Observer Fired, it.isEmpty()"+it.isEmpty().toString())
            viewModel.LocationList = it
            viewModel.findSelectedDateLocationEntries(selectedDay,selectedMonth)
            viewModel._dontStartTillImReady.value = viewModel._dontStartTillImReady.value != true
            viewModel.findUniqueDates(viewModel.LocationList)
             maxDate  = viewModel.LocationList.maxBy {
                it.timeStamp
            }
            minDate  = viewModel.LocationList.minBy {
                it.timeStamp
            }
            Log.i(ActivityTag,"Min date :"+minDate?.timeStamp+"Max date : "+maxDate?.timeStamp)
            if(minDate!=null && maxDate!=null) {
                binding.calendarView.maxDate = maxDate!!.timeStamp.toLong()
                binding.calendarView.minDate = minDate!!.timeStamp.toLong()
            }
        })
        val user = FirebaseAuth.getInstance().currentUser
        binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)

        binding.calendarView.setOnDateChangeListener { calendarView, i, i2, i3 ->
            Log.i(ActivityTag,"OnDateChange Strings are : "+i+" "+i2+" "+i3)
            selectedDay = i3
            selectedMonth = i2
            Log.i(ActivityTag,"OnDateChange Strings are : "+selectedDay+" "+selectedMonth)
            viewModel.findSelectedDateLocationEntries(selectedDay, selectedMonth)
        }
        binding.greetingTextView.text =
            "Hey ${(user!!.displayName)?.split("\\s".toRegex())?.first()},\nAll the best.\nYou do the writing, we do the tracking :)"
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)//onViewCreated migrate
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        var polylineOptions = PolylineOptions()
        polylineOptions.add(LatLng(8.toDouble(),72.toDouble()))
        polyLineFinal = googleMap.addPolyline(polylineOptions)
        Log.i(ActivityTag,"Inside onMapReady Before findSelectedDateLocationEntries")
        viewModel.findSelectedDateLocationEntries(selectedDay, selectedMonth)
        Log.i(ActivityTag,"Inside onMapReady After findSelectedDateLocationEntries")
        viewModel.dontStartTillImReady.observe(viewLifecycleOwner, Observer {

            var prevLatitude = 9.9991589
            var prevLongitude = 76.3084481
            Log.i(ActivityTag,"Inside onMapReady Inside Observer dontStartTillImReady")
            Log.i(ActivityTag,"onMapReady : mapList.size()"+viewModel.mapList.size.toString())
            var polylineOptions = PolylineOptions()
            googleMap.clear()
            for(item in viewModel.mapList.indices){
//            var line = googleMap.addPolyline(
                polylineOptions.add(LatLng(prevLatitude,prevLongitude),
                    LatLng(viewModel.mapList[item].latitude.toDouble(), viewModel.mapList[item].longitude.toDouble()))
                prevLatitude = viewModel.mapList[item].latitude.toDouble()
                prevLongitude = viewModel.mapList[item].longitude.toDouble()
//                )
            Log.i(ActivityTag,"InPolylineCreation :"+viewModel.mapList[item].latitude)
                polylineOptions.width(4F).color(Color.BLUE).geodesic(true)
                polyLineFinal = googleMap.addPolyline(polylineOptions)

        }
            if(!viewModel.mapList.isEmpty())
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(viewModel.mapList[0].latitude.toDouble(),viewModel.mapList[0].longitude.toDouble()), 16f))
        })


    }
}