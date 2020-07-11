package com.subzero.coviddiary.Map

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.subzero.coviddiary.DataObjects.LocationRecord
import com.subzero.coviddiary.DataObjects.LocationViewModel
import com.subzero.coviddiary.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.subzero.coviddiary.databinding.FragmentMapBinding
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.android.synthetic.main.map_date_picker.view.*
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var viewModel : LocationViewModel
    private val activityTag = "Activity-ProfileFragment"
    private var selectedMonth = Calendar.getInstance().get(Calendar.MONTH)
    private var selectedDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private lateinit var polyLineFinal : Polyline
    private lateinit var binding : FragmentMapBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.progressLoader.isIndeterminate = true
        binding.progressLoader.visibility = View.VISIBLE
        var maxDate : LocationRecord?
        var minDate : LocationRecord?
        Log.i(activityTag,
            "Inside ProfileFragment.onCreateView() Selected Date : $selectedDay Selected month : $selectedMonth"
        )
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        viewModel.allLocations.observe(viewLifecycleOwner, Observer { locationList ->
            binding.progressLoader.visibility = View.GONE
            Log.i(activityTag,"LocationList Observer Fired, it.isEmpty()"+locationList.isEmpty().toString())
            viewModel.LocationList = locationList
            viewModel.findSelectedDateLocationEntries(selectedDay,selectedMonth)
            viewModel._dontStartTillImReady.value = viewModel._dontStartTillImReady.value != true
            viewModel.findUniqueDates(viewModel.LocationList)
             maxDate  = viewModel.LocationList.maxBy {
                it.timeStamp
            }
            minDate  = viewModel.LocationList.minBy {
                it.timeStamp
            }
            Log.i(activityTag,"Min date :"+minDate?.timeStamp+"Max date : "+maxDate?.timeStamp)
//            if(minDate!=null && maxDate!=null) {
//                binding.calendarView.maxDate = maxDate!!.timeStamp
//                binding.calendarView.minDate = minDate!!.timeStamp
//            }
        })
        viewModel.findUniqueDates(LocationList = viewModel.LocationList)
        val user = FirebaseAuth.getInstance().currentUser
//        binding.calendarView.setOnDateChangeListener { _, i, i2, i3 ->
//            Log.i(activityTag, "OnDateChange Strings are : $i $i2 $i3")
//            selectedDay = i3
//            selectedMonth = i2
//            Log.i(activityTag, "OnDateChange Strings are : $selectedDay $selectedMonth")
//            viewModel.findSelectedDateLocationEntries(selectedDay, selectedMonth)
//        }
        binding.datePickerRecyclerView.apply {
            Log.i(activityTag,"inDatePickerrecyclerView, uniqueDateList.size() = "+viewModel.uniqueDateList.size.toString())
            layoutManager = LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false)
            adapter =  DatePickerAdapter(viewModel.uniqueDateList, { date: Date -> dateItemClicked(date) })
        }
        var snapHelper : LinearSnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.datePickerRecyclerView)
        binding.datePickerRecyclerView.onFlingListener = snapHelper
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)//onViewCreated migrate
        return binding.root
    }


    fun dateItemClicked(date: Date) {
        selectedDay = date.date
        selectedMonth = date.month
        binding.datePickerRecyclerView.smoothScrollToPosition(viewModel.uniqueDateList.indexOf(date))
//        binding.datePickerRecyclerView.getChildAt(viewModel.uniqueDateList.indexOf(date)).date_picker_layout.setBackgroundColor(resources.getColor(R.color.colorAccent))
        Log.i(activityTag,"Item Clicked, date is Date : "+ date.day +" Month is : "+date.month)
        viewModel.findSelectedDateLocationEntries(selectedDay,selectedMonth)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.datePickerRecyclerView.smoothScrollToPosition(viewModel.uniqueDateList.size.minus(1))
//
//    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
//        mapFragment?.view?.isClickable = true
//        mapFragment?.view?.setOnClickListener{
//            Log.i(activityTag,"MapViewClicked")
//            activity?.supportFragmentManager?.beginTransaction()
//                ?.replace(R.id.map, MapFullscreenFragment())
//                ?.addSharedElement(mapFragment.requireView(), map.enterTransition.toString())
//                ?.commit()
//        }
        val polylineOptions = PolylineOptions()
        polylineOptions.add(LatLng(8.toDouble(),72.toDouble()))
        polyLineFinal = googleMap.addPolyline(polylineOptions)
        Log.i(activityTag,"Inside onMapReady Before findSelectedDateLocationEntries")
        viewModel.findSelectedDateLocationEntries(selectedDay, selectedMonth)
        Log.i(activityTag,"Inside onMapReady After findSelectedDateLocationEntries")
        viewModel.dontStartTillImReady.observe(viewLifecycleOwner, Observer {
            binding.datePickerRecyclerView.adapter?.notifyDataSetChanged()
            var prevLatitude  = 0.0
            var prevLongitude  = 0.0
            Log.i(activityTag,"Inside onMapReady Inside Observer dontStartTillImReady")
            Log.i(activityTag,"onMapReady : mapList.size()"+viewModel.mapList.size.toString())
            val polylineOptions = PolylineOptions()
            googleMap.clear()
            var flag = true
            if(viewModel.mapList.isNotEmpty())
            {prevLatitude = viewModel.mapList[0].latitude.toDouble()
            prevLongitude = viewModel.mapList[0].longitude.toDouble()}
            for(item in viewModel.mapList.indices){
                if(flag){
                    flag = false
                    continue
                }
                polylineOptions.add(LatLng(prevLatitude,prevLongitude),
                    LatLng(viewModel.mapList[item].latitude.toDouble(), viewModel.mapList[item].longitude.toDouble()))
                prevLatitude = viewModel.mapList[item].latitude.toDouble()
                prevLongitude = viewModel.mapList[item].longitude.toDouble()
                Log.i(activityTag,"InPolylineCreation :"+viewModel.mapList[item].latitude)
                polylineOptions.width(4F).color(Color.BLUE).geodesic(true)
                polyLineFinal = googleMap.addPolyline(polylineOptions)
        }
            if(viewModel.mapList.isNotEmpty())
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(viewModel.mapList[0].latitude.toDouble(),viewModel.mapList[0].longitude.toDouble()), 16f))
        })
    }
}