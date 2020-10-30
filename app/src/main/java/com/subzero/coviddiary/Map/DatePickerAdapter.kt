package com.subzero.coviddiary.Map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.subzero.coviddiary.R
import kotlinx.android.synthetic.main.map_date_picker.view.*
import java.util.*

class DatePickerAdapter(var dateList: MutableList<Date>, val clickListener: (Date)->Unit) : RecyclerView.Adapter<DatePickerAdapter.ViewHolder>() {
    val tag = "Activity-DatePickerAdapter"
    class ViewHolder (view : View) : RecyclerView.ViewHolder(view){
       fun bind (date : Date, clickListener: (Date) -> Unit){
           itemView.date_text.text = date.date.toString()
           when(date.month){
               0->itemView.month_text.text="Jan"
               1->itemView.month_text.text="Feb"
               2->itemView.month_text.text="March"
               3->itemView.month_text.text="April"
               4->itemView.month_text.text="May"
               5->itemView.month_text.text="June"
               6->itemView.month_text.text="July"
               7->itemView.month_text.text="Aug"
               8->itemView.month_text.text="Sep"
               9->itemView.month_text.text="Oct"
               10->itemView.month_text.text="Nov"
               11->itemView.month_text.text="Dec"
           }
           when(date.date){
               1->itemView.th_text.text = "st"
               2->itemView.th_text.text = "nd"
               3->itemView.th_text.text = "rd"
               21->itemView.th_text.text = "st"
               22->itemView.th_text.text = "nd"
               23->itemView.th_text.text = "rd"
               else-> itemView.th_text.text = "th"
           }
           itemView.setOnClickListener{clickListener(date)}

       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.map_date_picker, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dateList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(tag, "InOnBindViewHolder")
        holder.bind(dateList[position],clickListener)
    }
}

