package com.subzero.coviddiary.Settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.subzero.coviddiary.DataObjects.LocationViewModel
import com.subzero.coviddiary.R
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {
    private lateinit var viewModel : LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        viewModel.findTravelData(viewModel.LocationList)
        viewModel.allLocations.observe(viewLifecycleOwner, Observer { locationList ->
            viewModel.LocationList = locationList
            viewModel._dontStartTillImReady.value = viewModel._dontStartTillImReady.value != true
            viewModel.findTravelData(viewModel.LocationList.reversed())
            populateData()
        })



        Log.wtf("Size ",viewModel.travelDataList.size.toString())
        Log.wtf("Sizeli ",viewModel.LocationList.size.toString())

        return inflater.inflate(R.layout.fragment_settings, container, false)


    }
    fun populateData(){
        if(viewModel.travelDataList.size>0){
            travel_data_recycler.apply {
                travel_data_recycler.adapter=travelDataAdapter(viewModel.travelDataList)
                layoutManager=LinearLayoutManager(activity)
                travel_data_recycler.setHasFixedSize(true)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.wtf("Size ",viewModel.travelDataList.size.toString())
        Log.wtf("Sizelist ",viewModel.LocationList.size.toString())
        Log.wtf("Sizelist ",viewModel.uniqueDateList.size.toString())



    }
}