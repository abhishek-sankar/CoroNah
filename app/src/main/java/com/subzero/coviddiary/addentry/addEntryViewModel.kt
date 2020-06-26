package com.subzero.coviddiary.addentry

import android.util.Log
import androidx.lifecycle.ViewModel

class addEntryViewModel : ViewModel() {
    var userInputText = ""
    val userGPS = ""
    var timeStamp = ""
    init{
        Log.i("Created Viewmodel","AddEntryFragment")
    }
}