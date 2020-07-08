package com.subzero.coviddiary.Map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.subzero.coviddiary.R
import kotlinx.android.synthetic.main.map_date_picker.view.*
import java.util.*

class DatePickerAdapter(var dateList: MutableList<Date>) : RecyclerView.Adapter<DatePickerAdapter.ViewHolder>() {
    val tag = "Activity-DatePickerAdapter"
    class ViewHolder (view : View) : RecyclerView.ViewHolder(view){
        val month = view.month_text
        val date = view.date_text
        val th = view.th_text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.map_date_picker, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dateList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(tag, "InOnBindViewHolder")
        holder.date.text = dateList[position].date.toString()
        holder.month.text = dateList[position].month.toString()
        when(dateList[position].month){
            0->holder.month.text="Jan"
            1->holder.month.text="Feb"
            2->holder.month.text="March"
            3->holder.month.text="April"
            4->holder.month.text="May"
            5->holder.month.text="June"
            6->holder.month.text="July"
            7->holder.month.text="Aug"
            8->holder.month.text="Sep"
            9->holder.month.text="Oct"
            10->holder.month.text="Nov"
            11->holder.month.text="Dec"
        }
        when(dateList[position].date){
            1->holder.th.text = "st"
            2->holder.th.text = "nd"
            3->holder.th.text = "rd"
            21->holder.th.text = "st"
            22->holder.th.text = "nd"
            23->holder.th.text = "rd"
            else-> holder.th.text = "th"
        }
    }
}

