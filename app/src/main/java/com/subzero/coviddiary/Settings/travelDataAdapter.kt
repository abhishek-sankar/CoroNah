package com.subzero.coviddiary.Settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.subzero.coviddiary.Map.DatePickerAdapter
import com.subzero.coviddiary.R
import kotlinx.android.synthetic.main.travel_data_card.view.*
import org.w3c.dom.Text
import java.sql.Time
import java.util.*

class travelDataAdapter(val timeList: MutableList<travelDataItem>) : RecyclerView.Adapter<travelDataAdapter.ViewHolder>(){


    class ViewHolder (view : View) : RecyclerView.ViewHolder(view){

        val fromLocationTime:TextView=view.recycler_text1
        val toLocationTime:TextView = view.recycler_text2
        val fromLocation:TextView = view.from_location
        val toLocation:TextView = view.to_location

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.travel_data_card,parent,false)


        return ViewHolder(view)
    }

    override fun getItemCount()= timeList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = timeList[position]
        holder.fromLocationTime.text=currentItem.startTime
        holder.toLocationTime.text = currentItem.destiationTime
        holder.fromLocation.text = currentItem.startLocationString
        holder.toLocationTime.text = currentItem.startTime

    }
}